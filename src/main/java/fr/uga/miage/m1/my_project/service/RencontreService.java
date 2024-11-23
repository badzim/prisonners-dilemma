package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.model.*;
import fr.uga.miage.m1.my_project.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.model.enums.TypeStrategie;
import fr.uga.miage.m1.my_project.model.joueur.Humain;
import fr.uga.miage.m1.my_project.model.joueur.Joueur;
import fr.uga.miage.m1.my_project.model.joueur.Robot;
import fr.uga.miage.m1.my_project.model.strategie.Strategie;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RencontreService {
    private final TourService tourService;

    public void initialiserRencontre(Rencontre rencontre) {
        Joueur initiateur = rencontre.getInitiateur();
        Joueur adversaire = rencontre.getAdversaire();
        initiateur.setEtat(EtatJoueur.EN_PARTIE);
        adversaire.setEtat(EtatJoueur.EN_PARTIE);
        initiateur.sendMessage("Rencontre commencée avec " + adversaire.getNom());
        adversaire.sendMessage("Rencontre commencée avec " + initiateur.getNom());
    }

    public void gererTour(Rencontre rencontre, int numeroTour) {
        Joueur initiateur = rencontre.getInitiateur();
        Joueur adversaire = rencontre.getAdversaire();

        sendTourMessage(initiateur, numeroTour);
        sendTourMessage(adversaire, numeroTour);

        int previousScoreInitiateur = getPreviousScore(rencontre, initiateur);
        int previousScoreAdversaire = getPreviousScore(rencontre, adversaire);

        TypeAction action1 = getPlayerAction(rencontre, initiateur, previousScoreInitiateur);
        initiateur = rencontre.getInitiateur();
        TypeAction action2 = getPlayerAction(rencontre, adversaire, previousScoreAdversaire);
        adversaire = rencontre.getAdversaire();

        rencontre.getHistoriqueInitiateur().add(action1);
        rencontre.getHistoriqueAdversaire().add(action2);

        Tour tour = new Tour(action1, action2);
        tourService.calculerScore(tour);
        rencontre.getTours().add(tour);

        updatePlayerScore(rencontre, initiateur, tour.getScoreInitiateur());
        updatePlayerScore(rencontre, adversaire, tour.getScoreAdversaire());

        sendTourResults(initiateur, action1, action2, tour.getScoreInitiateur());
        sendTourResults(adversaire, action2, action1, tour.getScoreAdversaire());
    }

    public void terminerRencontre(Rencontre rencontre) {
        Joueur initiateur = rencontre.getInitiateur();
        Joueur adversaire = rencontre.getAdversaire();

        sendFinResult(initiateur, adversaire);
        sendFinResult(adversaire, initiateur);
        logRencontreInfo(rencontre);
    }

    // Méthodes privées pour les actions spécifiques
    private void sendTourMessage(Joueur joueur, int tourNumber) {
        if (joueur instanceof Humain) {
            joueur.sendMessage("Tour " + tourNumber);
        }
    }

    private int getPreviousScore(Rencontre rencontre, Joueur joueur) {
        List<Tour> tours = rencontre.getTours();
        if (!tours.isEmpty()) {
            Tour lastTour = tours.get(tours.size() - 1);
            return (joueur == rencontre.getInitiateur()) ? lastTour.getScoreInitiateur() : lastTour.getScoreAdversaire();
        }
        return 0;
    }

    private TypeAction getPlayerAction(Rencontre rencontre, Joueur joueur, int previousScore) {
        List<TypeAction> historique = (joueur == rencontre.getInitiateur()) ? rencontre.getHistoriqueAdversaire() : rencontre.getHistoriqueInitiateur();
        TypeAction action = joueur.jouer(historique, previousScore);
        if (action == TypeAction.ABONDONNER) {
            synchronized (rencontre) {
                Joueur robot = handleAbandon(joueur);
                if (rencontre.getInitiateur() == joueur) {
                    rencontre.setInitiateur(robot);
                }
                else {
                    rencontre.setAdversaire(robot);
                }
                return robot.jouer(historique, previousScore);
            }
        }

        return action;
    }

    private Joueur handleAbandon(Joueur joueur) {
        Strategie strategie = getStrategie(joueur);
        joueur.close();
        return new Robot(joueur.getId() + "_ai", joueur.getNom() + "_ai", joueur.getScore(), strategie);
    }

    private void updatePlayerScore(Rencontre rencontre, Joueur joueur, int score) {
        joueur.addScore(score);
    }

    private void sendTourResults(Joueur joueur, TypeAction playerAction, TypeAction opponentAction, int scoreThisTour) {
        if (joueur instanceof Humain) {
            joueur.sendMessage("Résultat du tour: Vous avez " + playerAction + ", l'adversaire a " + opponentAction + ".");
            joueur.sendMessage("Votre score pour ce tour: " + scoreThisTour + ". Score total: " + joueur.getScore() + ".");
        }
    }

    private void sendFinResult(Joueur joueur, Joueur adversaire) {
        if (joueur instanceof Humain) {
            String resultat = determinerResultat(joueur, adversaire);
            joueur.sendMessage("La partie est terminée. Vous avez " + resultat + " !");
            joueur.sendMessage("Votre score final est de " + joueur.getScore() + ".");
            joueur.sendMessage("Bye");
        }
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

    private Strategie getStrategie(Joueur joueur) {
        joueur.sendMessage("choisir une strategie automatique parmi : ");
        TypeStrategie typeStrategie = (TypeStrategie) joueur.receiveMessage();
        return new StrategieFactoryService().getStrategie(typeStrategie);
    }

    private void logRencontreInfo(Rencontre rencontre) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Résumé de la Rencontre ===\n");
        sb.append("ID de la rencontre : ").append(rencontre.getIdRencontre()).append("\n");
        sb.append("Nombre de tours : ").append(rencontre.getNombreTours()).append("\n\n");

        Joueur initiateur = rencontre.getInitiateur();
        Joueur adversaire = rencontre.getAdversaire();

        sb.append("initiateur : ").append(initiateur.getNom())
                .append(" (Score : ").append(initiateur.getScore()).append(")\n");
        sb.append("adversaire : ").append(adversaire.getNom())
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

        LoggerFactory.getLogger(Server.class.getName()).info(sb.toString());

    }
}
