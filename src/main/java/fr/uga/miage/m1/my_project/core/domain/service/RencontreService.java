package fr.uga.miage.m1.my_project.core.domain.service;

import fr.uga.m1miage.pc.strategy.Strategy;
import fr.uga.m1miage.pc.strategy.StrategyFactory;
import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;
import fr.uga.miage.m1.my_project.core.domain.model.Tour;
import fr.uga.miage.m1.my_project.core.exception.rest.InvalidActionRestException;
import fr.uga.miage.m1.my_project.core.exception.rest.RencontreNotFoundRestException;
import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_10.StrategieAdaptateurGr2E10;
import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_10.StrategieEnumAdaptateurGr2E10;
import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5.StrategieAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5.StrategieEnumAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.core.domain.model.enums.*;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.*;
import fr.uga.miage.m1.my_project.core.domain.model.strategie.*;
import fr.uga.miage.m1.my_project.core.port.input.JoueurServicePort;
import fr.uga.miage.m1.my_project.core.port.input.RencontreServicePort;
import fr.uga.miage.m1.my_project.core.port.input.TourServicePort;
import fr.uga.miage.m1.my_project.core.port.output.EventEmitter;
import fr.uga.miage.m1.my_project.core.port.output.RencontreRepository;
import fr.uga.miage.m1.my_project.core.port.output.StrategieRepository;
import fr.uga.miage.m1.my_project.web.restapi.response.RencontreResponse;
import fr.uga.miage.m1.my_project.web.restapi.mapper.RencontreMapper;
import fr.uga.strats.g5_2.factory.StrategieFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RencontreService implements RencontreServicePort {

    @Qualifier("inMemoryRencontreRepository")
    private final RencontreRepository rencontreRepository;
    @Qualifier("inMemoryStrategieRepository")
    private final StrategieRepository strategieRepository;
    @Qualifier("joueurService")
    private final JoueurServicePort joueurService;
    @Qualifier("tourService")
    private final TourServicePort tourService;
    @Qualifier("sseServiceImpl")
    private final EventEmitter sseService;



    /* =====================================================
       Méthodes Publiques (Interfaces du Service)
       ===================================================== */

    public List<RencontreResponse> getRencontresEnAttente() {
        updateRencontresDisponibles();
        return rencontreRepository.getRencontresEnAttente()
                .stream()
                .map(RencontreMapper::toDto)
                .toList();
    }

    public boolean initierRencontre(String clientId, int nombreTours) {
        verifyClientConnected(clientId);
        Joueur initiateur = joueurService.getJoueurById(clientId);
        if (initiateur.getEtat() != ETAT_JOUEUR.EN_MENU) {
            throw new InvalidActionRestException("Le joueur doit être dans le menu");
        }
        initiateur.setEtat(ETAT_JOUEUR.EN_ATTENTE);
        Rencontre rencontre = createNewRencontre(nombreTours, initiateur);
        rencontreRepository.addRencontre(clientId, rencontre);
        sseService.sendEvent(clientId, "game-initiated", "Rencontre initiée. En attente d'un autre joueur.");
        sseService.broadcast("broadcast-game-initiated-all", "une rencontre à été initié par : " + clientId);
        log.info("Rencontre initiée par le client {} avec {} tours.", clientId, nombreTours);

        return true;
    }

    public void rejoindreRencontre(String clientId, String idRencontre) {
        verifyClientConnected(clientId);
        Joueur adversaire = joueurService.getJoueurById(clientId);
        if (adversaire.getEtat() != ETAT_JOUEUR.EN_MENU) {
            throw new InvalidActionRestException("Le joueur doit être dans le menu");
        }
        Rencontre rencontre = validateRejoindreRencontre(clientId, idRencontre);
        initializeJoinedRencontre(rencontre, adversaire, clientId);
        handleInitiateurDeconnecteSiBesoin(rencontre);
    }

    public synchronized void enregistrerChoix(String clientId, TYPE_ACTION action, TYPE_STRATEGIE strategie, String groupId) {
        verifyClientConnected(clientId);
        Rencontre rencontre = getRencontreEnCoursByClientId(clientId);
        Joueur joueur = getJoueurFromRencontre(rencontre, clientId);
        Joueur joueurOppose = getJoueurOppose(rencontre, joueur);

        if (action == TYPE_ACTION.ABONDONNER) {
            action = handlePlayerAbandon(rencontre, joueur, strategie, joueurOppose, groupId);
            joueur = getJoueurOppose(rencontre, joueurOppose);
        }

        tourService.setActionJoueur(joueur, rencontre, action);

        if (estTourPret(rencontre)) {
            processTour(rencontre);
        }
    }

    public Rencontre validateRejoindreRencontre(String clientId, String idRencontre) {
        verifyClientConnected(clientId);
        Rencontre rencontre = rencontreRepository.findRencontreById(idRencontre);

        if (rencontre.getInitiateur().getId().equals(clientId)) {
            throw new InvalidActionRestException("Vous ne pouvez pas rejoindre votre propre partie.");
        }

        if (rencontre.getAdversaire() != null) {
            throw new InvalidActionRestException("La rencontre est déjà complète.");
        }

        return rencontre;
    }

    public boolean estTourPret(Rencontre rencontre) {
        Tour tour = rencontre.getCurrentTour();
        return tour.getActionInitiateur() != null && tour.getActionAdversaire() != null;
    }

    /* =====================================================
       Méthodes Privées - Gestion des Rencontres Disponibles
       ===================================================== */

    private void updateRencontresDisponibles() {
        sseService.handleDisconnectedPlayers();
        removeDisconnectedRencontres();
    }

    private void removeDisconnectedRencontres() {
        List<Rencontre> rencontresNonActives = rencontreRepository.getRencontresEnAttente()
                .stream()
                .filter(Predicate.not(this::isInitiateurConnected))
                .collect(Collectors.toCollection(ArrayList::new));
        for (Rencontre rencontre : rencontresNonActives) {
            if (rencontre.getAdversaire() != null && rencontre.getInitiateur() != null) continue;
            rencontreRepository.changerEtatRencontre(rencontre.getIdRencontre(), ETAT_RENCONTRE.TERMINEE);
        }
    }

    private boolean isInitiateurConnected(Rencontre rencontre) {
        Joueur initiateur = rencontre.getInitiateur();
        boolean isConnected = sseService.getSseEmitters().containsKey(initiateur.getId());
        if (!isConnected) {
            initiateur.setEtat(ETAT_JOUEUR.EN_MENU);
        }
        return isConnected;
    }


    /* =====================================================
       Méthodes Privées - Initialisation de Rencontre
       ===================================================== */

    private Rencontre createNewRencontre(int nombreTours, Joueur initiateur) {
        Rencontre rencontre = new Rencontre();
        rencontre.setNombreTours(nombreTours);
        rencontre.setInitiateur(initiateur);
        return rencontre;
    }

    private void initializeJoinedRencontre(Rencontre rencontre, Joueur adversaire, String clientId) {
        // Préparation des joueurs
        adversaire.setEtat(ETAT_JOUEUR.EN_PARTIE_ADVERSAIRE);
        Joueur initiateur = rencontre.getInitiateur();
        initiateur.setEtat(ETAT_JOUEUR.EN_PARTIE_INITIATEUR);

        // Mise à jour de la rencontre
        rencontre.setAdversaire(adversaire);
        rencontreRepository.changerEtatRencontre(rencontre.getIdRencontre(), ETAT_RENCONTRE.EN_COURS);
        rencontreRepository.addRencontreParClient(clientId, rencontre);
        rencontre.setCurrentTour(new Tour(1));

        // Notifications
        sseService.sendEvent(initiateur.getId(), "game-started", "Un joueur a rejoint la rencontre. La partie commence !");
        sseService.sendEvent(clientId, "game-started", "Vous avez rejoint la rencontre. La partie commence !");
        sseService.broadcast("broadcast-game-taken", "une rencontre à été lancé par : " + clientId);
    }

    private void handleInitiateurDeconnecteSiBesoin(Rencontre rencontre) {
        Joueur initiateur = rencontre.getInitiateur();
        if (sseService.getSseEmitters().get(initiateur.getId()) == null) {
            initiateur.setStrategieAutomatique(strategieRepository.getStrategie(TYPE_STRATEGIE.DONNANTDONNANTALEATOIRE));
            initiateur = handleAbandon(rencontre, initiateur);

            Joueur adversaire = rencontre.getAdversaire();
            TYPE_ACTION action = initiateur.jouer(
                    getHistoriqueJoueur(rencontre, adversaire),
                    getDernierResultatJoueur(rencontre, adversaire)
            );
            tourService.setActionJoueur(initiateur, rencontre, action);

        }
    }

    public void handleDesconnectedPlayer(String clientId) {
        Joueur joueur = joueurService.getJoueurById(clientId);
        Rencontre rencontre;
        try {
            rencontre = getRencontreEnCoursByClientId(clientId);
        } catch (RencontreNotFoundRestException e) {
            log.warn("rencontre non disponible");
            return;
        }

        if (sseService.getSseEmitters().get(joueur.getId()) == null) {
            joueur.setStrategieAutomatique(strategieRepository.getStrategie(TYPE_STRATEGIE.DONNANTDONNANTALEATOIRE));

            joueur = handleAbandon(rencontre, joueur);

            Joueur adversaire = rencontre.getAdversaire();
            TYPE_ACTION action = joueur.jouer(
                    getHistoriqueJoueur(rencontre, adversaire),
                    getDernierResultatJoueur(rencontre, adversaire));
            tourService.setActionJoueur(joueur, rencontre, action);

            if (estTourPret(rencontre)) {
                processTour(rencontre);
            }
        }

    }

    /* =====================================================
       Méthodes Privées - Gestion Abandon / Actions Joueur
       ===================================================== */

    public TYPE_ACTION handlePlayerAbandon(Rencontre rencontre, Joueur joueur, TYPE_STRATEGIE strategie, Joueur joueurOppose, String groupId) {
        sseService.sendEvent(joueur.getId(), "player-abondonne", "Vous avez abandonné. Vous avez été remplacé par un robot.");
        String nomHumain = joueur.getNom();

        Strategie strategieChoisie;
        try {
            strategieChoisie = switch (groupId) {
                case "G2_5" -> {
                    fr.uga.strats.g5_2.models.Strategie strategieExternG2E5 = StrategieFactory.creeStrategie(StrategieEnumAdaptateurGr2E5.adapterInverse(strategie));
                    yield new StrategieAdaptateurGr2E5(strategieExternG2E5, getHistoriqueJoueur(rencontre, joueur), joueur.getEtat() == ETAT_JOUEUR.EN_PARTIE_INITIATEUR);
                }
                case "G2_10" -> {
                    Strategy strategieExternG2E10 = StrategyFactory.createStrategy(StrategieEnumAdaptateurGr2E10.adapterInverse(strategie));
                    yield new StrategieAdaptateurGr2E10(strategieExternG2E10, getHistoriqueJoueur(rencontre, joueur));
                }
                default -> (strategie != null)
                        ? strategieRepository.getStrategie(strategie)
                        : strategieRepository.getStrategie(TYPE_STRATEGIE.DONNANTDONNANT);
            };
        } catch (NullPointerException e) {
            strategieChoisie =(strategie != null) ? strategieRepository.getStrategie(strategie)
                    : strategieRepository.getStrategie(TYPE_STRATEGIE.DONNANTDONNANT);

        }


        joueur.setStrategieAutomatique(strategieChoisie);

        Joueur robot = handleAbandon(rencontre, joueur);
        TYPE_ACTION action = robot.jouer(getHistoriqueJoueur(rencontre, joueurOppose), getDernierResultatJoueur(rencontre, joueurOppose));

        joueur.setEtat(ETAT_JOUEUR.EN_MENU);
        if (joueurOppose instanceof Humain) {
            sseService.sendEvent(joueurOppose.getId(), "opposite-player-abondonne", String.format(
                    "Le joueur %s a abandonné et a été remplacé par %s.", nomHumain, robot.getNom()
            ));
        }
        return action;
    }

    private Joueur handleAbandon(Rencontre rencontre, Joueur joueur) {
        Joueur robot = remplacerJoueurParRobot(joueur);
        if (robot.getEtat() == ETAT_JOUEUR.EN_PARTIE_INITIATEUR) {
            rencontre.setInitiateur(robot);
        } else if (robot.getEtat() == ETAT_JOUEUR.EN_PARTIE_ADVERSAIRE) {
            rencontre.setAdversaire(robot);
        }
        joueur.setEtat(ETAT_JOUEUR.EN_MENU);
        return robot;
    }

    private Joueur remplacerJoueurParRobot(Joueur joueur) {
        Strategie strategie = joueur.getStrategieAutomatique();
        return new Robot(joueur.getId() + "_ai", joueur.getNom() + "_ai", joueur.getScore(), strategie, joueur.getEtat());
    }

    /* =====================================================
       Méthodes Privées - Gestion des Tours
       ===================================================== */

    private void processTour(Rencontre rencontre) {
        Tour tour = rencontre.getCurrentTour();
        tourService.calculerScore(tour);

        Joueur initiateur = rencontre.getInitiateur();
        Joueur adversaire = rencontre.getAdversaire();

        initiateur.addScore(tour.getScoreInitiateur());
        adversaire.addScore(tour.getScoreAdversaire());

        notifierScoresAuxJoueurs(rencontre, tour);
        passerAuTourSuivant(rencontre);
    }

    private void passerAuTourSuivant(Rencontre rencontre) {
        Tour previousTour = rencontre.getCurrentTour();
        rencontre.getTours().add(previousTour);

        if (previousTour.getNumeroTour() < rencontre.getNombreTours()) {
            preparerNouveauTour(rencontre, previousTour.getNumeroTour() + 1);
        } else {
            terminerRencontre(rencontre);
        }
    }

    private void preparerNouveauTour(Rencontre rencontre, int nextTourNumber) {
        Tour newTour = new Tour(nextTourNumber);
        rencontre.setCurrentTour(newTour);

        Joueur initiateur = rencontre.getInitiateur();
        Joueur adversaire = rencontre.getAdversaire();

        handleRobotActionAtTourStart(rencontre, initiateur, true);
        handleRobotActionAtTourStart(rencontre, adversaire, false);

        if (estTourPret(rencontre)) {
            processTour(rencontre);
        } else {
            promptHumanPlayers(rencontre);
        }
    }

    private void handleRobotActionAtTourStart(Rencontre rencontre, Joueur joueur, boolean isInitiateur) {
        if (joueur instanceof Robot) {
            List<TYPE_ACTION> historiqueAdverse = isInitiateur ? rencontre.getHistoriqueAdversaire() : rencontre.getHistoriqueInitiateur();
            TYPE_ACTION action = joueur.jouer(historiqueAdverse, joueur.getScore());

            Tour currentTour = rencontre.getCurrentTour();
            if (isInitiateur) {
                currentTour.setActionInitiateur(action);
            } else {
                currentTour.setActionAdversaire(action);
            }
        }
    }

    /* =====================================================
       Méthodes Privées - Notifications
       ===================================================== */

    private void notifierScoresAuxJoueurs(Rencontre rencontre, Tour tour) {
        notifierJoueur(
                rencontre.getInitiateur(),
                tour.getNumeroTour(),
                tour.getActionInitiateur(),
                tour.getActionAdversaire(),
                tour.getScoreInitiateur()
        );
        notifierJoueur(
                rencontre.getAdversaire(),
                tour.getNumeroTour(),
                tour.getActionAdversaire(),
                tour.getActionInitiateur(),
                tour.getScoreAdversaire()
        );
    }

    private void notifierJoueur(Joueur joueur, int numeroTour, TYPE_ACTION actionJoueur, TYPE_ACTION actionAdversaire, int scoreTour) {
        if (joueur instanceof Humain) {
            sseService.sendEvent(
                    joueur.getId(),
                    "tour-finished",
                    String.format(
                            "Tour %d terminé. Vous avez %s, votre adversaire a %s. Score ce tour : %d. Score total : %d.",
                            numeroTour, actionJoueur, actionAdversaire, scoreTour, joueur.getScore()
                    )
            );
        }
    }

    private void promptHumanPlayers(Rencontre rencontre) {
        Tour currentTour = rencontre.getCurrentTour();
        int tourNumber = currentTour.getNumeroTour();

        inviteHumainSiNecessaire(rencontre.getInitiateur(), currentTour.getActionInitiateur(), tourNumber);
        inviteHumainSiNecessaire(rencontre.getAdversaire(), currentTour.getActionAdversaire(), tourNumber);
    }

    private void inviteHumainSiNecessaire(Joueur joueur, TYPE_ACTION action, int tourNumber) {
        if (action == null && joueur instanceof Humain) {
            sseService.sendEvent(joueur.getId(), "make-choice", "Veuillez faire votre choix pour le tour " + tourNumber);
        }
    }

    /* =====================================================
       Méthodes Privées - Fin de Rencontre
       ===================================================== */

    private void terminerRencontre(Rencontre rencontre) {
        Joueur initiateur = rencontre.getInitiateur();
        Joueur adversaire = rencontre.getAdversaire();

        String resultatInitiateur = determinerResultat(initiateur, adversaire);
        String resultatAdversaire = determinerResultat(adversaire, initiateur);

        sseService.sendEvent(initiateur.getId(), "game-finished", String.format(
                "Rencontre terminée. Vous avez %s. Score final : %d.",
                resultatInitiateur, initiateur.getScore()
        ));
        sseService.sendEvent(adversaire.getId(), "game-finished", String.format(
                "Rencontre terminée. Vous avez %s. Score final : %d.",
                resultatAdversaire, adversaire.getScore()
        ));

        logRencontreInfo(rencontre);
        resetPlayersScoreAndState(initiateur, adversaire);
    }

    private void resetPlayersScoreAndState(Joueur initiateur, Joueur adversaire) {
        initiateur.setEtat(ETAT_JOUEUR.EN_MENU);
        adversaire.setEtat(ETAT_JOUEUR.EN_MENU);
        initiateur.setScore(0);
        adversaire.setScore(0);
    }

    private String determinerResultat(Joueur joueur, Joueur adversaire) {
        int scoreJoueur = joueur.getScore();
        int scoreAdversaire = adversaire.getScore();

        if (scoreJoueur > scoreAdversaire) {
            return "gagné";
        } else if (scoreJoueur < scoreAdversaire) {
            return "perdu";
        } else {
            return "fait match nul";
        }
    }

    private void logRencontreInfo(Rencontre rencontre) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Résumé de la Rencontre ===\n")
                .append("ID de la rencontre : ").append(rencontre.getIdRencontre()).append("\n")
                .append("Nombre de tours : ").append(rencontre.getNombreTours()).append("\n\n");

        Joueur initiateur = rencontre.getInitiateur();
        Joueur adversaire = rencontre.getAdversaire();

        sb.append("Initiateur : ").append(initiateur.getNom())
                .append(" (Score : ").append(initiateur.getScore()).append(")\n");
        sb.append("Adversaire : ").append(adversaire.getNom())
                .append(" (Score : ").append(adversaire.getScore()).append(")\n\n");
        sb.append("Détails des tours :\n");

        List<Tour> tours = rencontre.getTours();
        for (int i = 0; i < tours.size(); i++) {
            Tour tour = tours.get(i);
            sb.append("Tour ").append(i + 1).append(" :\n")
                    .append("  Action initiateur : ").append(tour.getActionInitiateur()).append("\n")
                    .append("  Action adversaire : ").append(tour.getActionAdversaire()).append("\n")
                    .append("  Score initiateur ce tour : ").append(tour.getScoreInitiateur()).append("\n")
                    .append("  Score adversaire ce tour : ").append(tour.getScoreAdversaire()).append("\n\n");
        }
        sb.append("=== Fin du Résumé ===");

        log.info("{}", sb);
    }

    /* =====================================================
       Méthodes Privées - Utilitaires
       ===================================================== */

    private void verifyClientConnected(String clientId) {
        if (!sseService.getSseEmitters().containsKey(clientId)) {
            throw new RencontreNotFoundRestException("Le client n'est pas connecté.", clientId);
        }
    }

    public Rencontre getRencontreEnCoursByClientId(String clientId) {
        Rencontre rencontre = rencontreRepository.findRencontreByClientIdAndEtatRencontre(clientId, ETAT_RENCONTRE.EN_COURS);
        if (rencontre == null) {
            throw new RencontreNotFoundRestException("Rencontre non trouvée pour le client.", clientId);
        }
        return rencontre;
    }

    public Joueur getJoueurFromRencontre(Rencontre rencontre, String clientId) {
        if (clientId.equals(rencontre.getInitiateur().getId())) {
            return rencontre.getInitiateur();
        } else if (clientId.equals(rencontre.getAdversaire().getId())) {
            return rencontre.getAdversaire();
        } else {
            throw new InvalidActionRestException("Le joueur ne fait pas partie de cette rencontre.");
        }
    }

    public Joueur getJoueurOppose(Rencontre rencontre, Joueur joueur) {
        return (joueur == rencontre.getAdversaire()) ? rencontre.getInitiateur() : rencontre.getAdversaire();
    }

    public List<TYPE_ACTION> getHistoriqueJoueur(Rencontre rencontre, Joueur joueur) {
        return (joueur.getEtat() == ETAT_JOUEUR.EN_PARTIE_INITIATEUR)
                ? rencontre.getHistoriqueInitiateur()
                : rencontre.getHistoriqueAdversaire();
    }

    public int getDernierResultatJoueur(Rencontre rencontre, Joueur joueur) {
        List<Tour> tours = rencontre.getTours();
        if (tours.isEmpty()) return 0;
        Tour dernierTour = tours.get(tours.size() - 1);
        return (joueur.getEtat() == ETAT_JOUEUR.EN_PARTIE_INITIATEUR)
                ? dernierTour.getScoreInitiateur()
                : dernierTour.getScoreAdversaire();
    }
}
