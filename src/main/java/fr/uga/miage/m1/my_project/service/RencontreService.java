package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.exception.rest.*;
import fr.uga.miage.m1.my_project.model.*;
import fr.uga.miage.m1.my_project.model.enums.*;
import fr.uga.miage.m1.my_project.model.joueur.*;
import fr.uga.miage.m1.my_project.model.strategie.*;
import fr.uga.miage.m1.my_project.restapi.dto.RencontreDto;
import fr.uga.miage.m1.my_project.restapi.mapper.RencontreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RencontreService {

    private final TourService tourService;
    private final SseService sseService;
    private final RencontreManagerService rencontreManagerService;
    private final JoueurService joueurService;
    private final StrategieFactoryService strategieFactoryService;

    /**
     * Initialise une nouvelle rencontre.
     */
    public boolean initierRencontre(String clientId, int nombreTours) {
        verifyClientConnected(clientId);
        joueurService.joueurEstEnMenu(clientId);
        Joueur initiateur = joueurService.getHumain(clientId);
        initiateur.setEtat(EtatJoueur.EN_ATTENTE);
        Rencontre rencontre = new Rencontre();
        rencontre.setNombreTours(nombreTours);
        rencontre.setInitiateur(initiateur);
        rencontreManagerService.incrementNombreRencontreEnAttente();
        rencontreManagerService.addToRencontreEnAttente(rencontre);
        sseService.sendMessage(clientId, "Rencontre initiée. En attente d'un autre joueur.");
        log.info("Rencontre initiée par le client {} avec {} tours.", clientId, nombreTours);
        return true;
    }

    /**
     * Permet à un joueur de rejoindre une rencontre existante.
     */
    public void rejoindreRencontre(String clientId, String idRencontre) {
        verifyClientConnected(clientId);
        joueurService.joueurEstEnMenu(clientId);
        Rencontre rencontre = validateRejoindreRencontre(clientId, idRencontre);
        Joueur adversaire = joueurService.getHumain(clientId);
        adversaire.setEtat(EtatJoueur.EN_PARTIE_ADVERSAIRE);
        rencontre.setAdversaire(adversaire);
        Joueur initiateur = rencontre.getInitiateur();
        initiateur.setEtat(EtatJoueur.EN_PARTIE_INITIATEUR);
        rencontreManagerService.decrementNombreRencontreEnAttente(rencontre);
        rencontreManagerService.addToRencontreMap(clientId, rencontre);
        rencontreManagerService.addToRencontreMap(initiateur.getId(), rencontre);
        rencontre.setCurrentTour(new Tour(1));
        sseService.sendMessage(initiateur.getId(), "Un joueur a rejoint la rencontre. La partie commence !");
        sseService.sendMessage(clientId, "Vous avez rejoint la rencontre. La partie commence !");
    }

    /**
     * Enregistre le choix d'un joueur pour un tour.
     */
    public synchronized void enregistrerChoix(String clientId, TypeAction action, TypeStrategie strategie) {
        verifyClientConnected(clientId);
        Rencontre rencontre = getRencontreByClientId(clientId);
        Joueur joueur = getJoueurFromRencontre(rencontre, clientId);
        Joueur joueurOppose = getJoueurOppose(rencontre ,joueur);
        if (action == TypeAction.ABONDONNER) {
            sseService.sendMessage(joueur.getId(), "Vous avez abandonné. Vous avez été remplacé par un robot.");
            String nomHumain = joueur.getNom();
            Strategie strategieChoisie = (strategie != null)
                    ? strategieFactoryService.getStrategie(strategie)
                    : strategieFactoryService.getStrategie(TypeStrategie.DONNANTDONNANT);
            joueur.setStrategieAutomatique(strategieChoisie);
            joueur = handleAbandon(rencontre, joueur);
            action = joueur.jouer(getHistoriqueJoueur(rencontre, joueurOppose), getDernierResultatJoueur(rencontre, joueurOppose));
            if (joueurOppose instanceof Humain)
                sseService.sendMessage(joueurOppose.getId(), String.format("Le joueur %s a abandonné et a été remplacé par %s.", nomHumain, joueur.getNom()));
        }
        tourService.setActionJoueur(joueur, rencontre, action);
        if (estTourPret(rencontre)) {
            processTour(rencontre);
        }
    }

    /**
     * Valide si un joueur peut rejoindre une rencontre.
     */
    public Rencontre validateRejoindreRencontre(String clientId, String idRencontre) {
        verifyClientConnected(clientId);

        Rencontre rencontre = rencontreManagerService.findRencontreEnAttenteById(idRencontre);

        if (rencontre.getInitiateur().getId().equals(clientId)) {
            throw new InvalidActionRestException("Vous ne pouvez pas rejoindre votre propre partie.");
        }

        if (rencontre.getAdversaire() != null) {
            throw new InvalidActionRestException("La rencontre est déjà complète.");
        }
        return rencontre;
    }



    /**
     * Traite un tour une fois que les deux joueurs ont fait leur choix.
     */
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

    /**
     * Gère l'abandon d'un joueur.
     */
    private Joueur handleAbandon(Rencontre rencontre, Joueur joueur) {
        Joueur robot = remplacerJoueurParRobot(joueur);
        if (robot.getEtat() == EtatJoueur.EN_PARTIE_INITIATEUR) {
            rencontre.setInitiateur(robot);
        } else if (robot.getEtat() == EtatJoueur.EN_PARTIE_ADVERSAIRE) {
            rencontre.setAdversaire(robot);
        }
        joueur.setEtat(EtatJoueur.EN_MENU);
        return robot;
    }

    private int getDernierResultatJoueur(Rencontre rencontre, Joueur joueur) {
        List <Tour> tours = rencontre.getTours();
        if (tours.isEmpty()) return 0;
        Tour dernierTour = tours.get(tours.size() - 1);
        if (joueur.getEtat() == EtatJoueur.EN_PARTIE_INITIATEUR) {
            return dernierTour.getScoreInitiateur();
        }
        return dernierTour.getScoreAdversaire();
    }

    private Joueur getJoueurOppose(Rencontre rencontre, Joueur joueur) {
        Joueur adversaire = rencontre.getAdversaire();
        if (joueur == adversaire) return rencontre.getInitiateur();
        return adversaire;
    }

    private List<TypeAction> getHistoriqueJoueur(Rencontre rencontre, Joueur joueur) {
        if (joueur.getEtat() == EtatJoueur.EN_PARTIE_INITIATEUR) {
            return rencontre.getHistoriqueInitiateur();
        }
        return rencontre.getHistoriqueAdversaire();
    }

    /**
     * Remplace un joueur par un robot utilisant la stratégie spécifiée.
     */
    private Joueur remplacerJoueurParRobot(Joueur joueur) {
        Strategie strategie = joueur.getStrategieAutomatique();
        return new Robot(joueur.getId() + "_ai", joueur.getNom() + "_ai", joueur.getScore(), strategie, joueur.getEtat());
    }

    /**
     * Notifie les joueurs des résultats du tour.
     */
    private void notifierScoresAuxJoueurs(Rencontre rencontre, Tour tour) {
        notifierJoueur(rencontre.getInitiateur(), tour.getNumeroTour(), tour.getActionInitiateur(), tour.getActionAdversaire(), tour.getScoreInitiateur());
        notifierJoueur(rencontre.getAdversaire(), tour.getNumeroTour(), tour.getActionAdversaire(), tour.getActionInitiateur(), tour.getScoreAdversaire());
    }

    private void notifierJoueur(Joueur joueur, int numeroTour, TypeAction actionJoueur, TypeAction actionAdversaire, int scoreTour) {
        if (joueur instanceof Humain) sseService.sendMessage(joueur.getId(), String.format(
                "Tour %d terminé. Vous avez %s, votre adversaire a %s. Score ce tour : %d. Score total : %d.",
                numeroTour, actionJoueur, actionAdversaire, scoreTour, joueur.getScore()
        ));
    }

    /**
     * Vérifie si le tour est prêt à être traité.
     */
    public boolean estTourPret(Rencontre rencontre) {
        Tour tour = rencontre.getCurrentTour();
        return tour.getActionInitiateur() != null && tour.getActionAdversaire() != null;
    }

    /**
     * Passe au tour suivant ou termine la rencontre si tous les tours ont été joués.
     */
    private void passerAuTourSuivant(Rencontre rencontre) {
        Tour previousTour = rencontre.getCurrentTour();
        rencontre.getTours().add(previousTour);

        if (previousTour.getNumeroTour() < rencontre.getNombreTours()) {
            int nextTourNumber = previousTour.getNumeroTour() + 1;
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
        } else {
            terminerRencontre(rencontre);
        }
    }

    /**
     * Gère l'action du robot au début d'un nouveau tour.
     */
    private void handleRobotActionAtTourStart(Rencontre rencontre, Joueur joueur, boolean isInitiateur) {
        if (joueur instanceof Robot) {
            TypeAction action = joueur.jouer(
                    isInitiateur ? rencontre.getHistoriqueAdversaire() : rencontre.getHistoriqueInitiateur(),
                    joueur.getScore()
            );
            if (isInitiateur) {
                rencontre.getCurrentTour().setActionInitiateur(action);
            } else {
                rencontre.getCurrentTour().setActionAdversaire(action);
            }
        }
    }

    /**
     * Invite les joueurs humains à faire leur choix pour le tour en cours.
     */
    private void promptHumanPlayers(Rencontre rencontre) {
        Tour currentTour = rencontre.getCurrentTour();
        int tourNumber = currentTour.getNumeroTour();

        if (currentTour.getActionInitiateur() == null && !(rencontre.getInitiateur() instanceof Robot)) {
            sseService.sendMessage(rencontre.getInitiateur().getId(), "Veuillez faire votre choix pour le tour " + tourNumber);
        }

        if (currentTour.getActionAdversaire() == null && !(rencontre.getAdversaire() instanceof Robot)) {
            sseService.sendMessage(rencontre.getAdversaire().getId(), "Veuillez faire votre choix pour le tour " + tourNumber);
        }
    }

    /**
     * Termine la rencontre et notifie les joueurs du résultat final.
     */
    private void terminerRencontre(Rencontre rencontre) {
        Joueur initiateur = rencontre.getInitiateur();
        Joueur adversaire = rencontre.getAdversaire();
        String resultatInitiateur = determinerResultat(initiateur, adversaire);
        String resultatAdversaire = determinerResultat(adversaire, initiateur);
        sseService.sendMessage(initiateur.getId(), String.format(
                "Rencontre terminée. Vous avez %s. Score final : %d.",
                resultatInitiateur, initiateur.getScore()
        ));
        sseService.sendMessage(adversaire.getId(), String.format(
                "Rencontre terminée. Vous avez %s. Score final : %d.",
                resultatAdversaire, adversaire.getScore()
        ));
        logRencontreInfo(rencontre);
        initiateur.setEtat(EtatJoueur.EN_MENU);
        adversaire.setEtat(EtatJoueur.EN_MENU);
        initiateur.setScore(0);
        adversaire.setScore(0);
    }

    /**
     * Détermine le résultat (gagné, perdu, match nul) pour un joueur donné.
     */
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

    /**
     * Récupère la liste des rencontres disponibles.
     */
    public List<RencontreDto> getRencontresDisponibles() {
        return rencontreManagerService.getRencontresEnAttente()
                .stream()
                .map(RencontreMapper::toDto)
                .toList();
    }

    /**
     * Journalise les informations détaillées d'une rencontre.
     */
    private void logRencontreInfo(Rencontre rencontre) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Résumé de la Rencontre ===\n");
        sb.append("ID de la rencontre : ").append(rencontre.getIdRencontre()).append("\n");
        sb.append("Nombre de tours : ").append(rencontre.getNombreTours()).append("\n\n");

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
            sb.append("Tour ").append(i + 1).append(" :\n");
            sb.append("  Action initiateur : ").append(tour.getActionInitiateur()).append("\n");
            sb.append("  Action adversaire : ").append(tour.getActionAdversaire()).append("\n");
            sb.append("  Score initiateur ce tour : ").append(tour.getScoreInitiateur()).append("\n");
            sb.append("  Score adversaire ce tour : ").append(tour.getScoreAdversaire()).append("\n");
            sb.append("\n");
        }
        sb.append("=== Fin du Résumé ===");

        log.info("{}", sb);
    }

    /**
     * Vérifie si le client est connecté via SSE.
     */
    private void verifyClientConnected(String clientId) {
        if (!sseService.getSseEmitters().containsKey(clientId)) {
            throw new RencontreNotFoundRestException("Le client n'est pas connecté.", clientId);
        }
    }

    /**
     * Récupère la rencontre associée à un client.
     */
    private Rencontre getRencontreByClientId(String clientId) {
        Rencontre rencontre = rencontreManagerService.getRencontreMap().get(clientId);
        if (rencontre == null) {
            throw new RencontreNotFoundRestException("Rencontre non trouvée pour le client.", clientId);
        }
        return rencontre;
    }

    /**
     * Récupère le joueur d'une rencontre à partir de son ID.
     */
    private Joueur getJoueurFromRencontre(Rencontre rencontre, String clientId) {
        if (clientId.equals(rencontre.getInitiateur().getId())) {
            return rencontre.getInitiateur();
        } else if (clientId.equals(rencontre.getAdversaire().getId())) {
            return rencontre.getAdversaire();
        } else {
            throw new InvalidActionRestException("Le joueur ne fait pas partie de cette rencontre.");
        }
    }
}
