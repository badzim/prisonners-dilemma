package fr.uga.miage.m1.my_project.server;

import fr.uga.miage.m1.my_project.server.models.Humain;
import fr.uga.miage.m1.my_project.server.models.Joueur;
import fr.uga.miage.m1.my_project.server.models.Robot;
import fr.uga.miage.m1.my_project.server.models.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.server.models.enums.TypeStrategie;
import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;
import fr.uga.miage.m1.my_project.server.models.strategies.*;
import lombok.Data;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Data
public class Rencontre extends Thread {
    private int idRencontre;
    private Joueur initiateur;
    private Joueur adversaire;
    private int nombreTours;
    private List<TypeAction> historiqueJ1;
    private List<TypeAction> historiqueJ2;
    private List<Tour> tours;
    private static final Logger logger = Logger.getLogger(Rencontre.class.getName());


    // Liste statique pour les rencontres en attente
    private static List<Rencontre> rencontresEnAttente = new ArrayList<>();
    private static int nombreRencontreEnAttente = 1;

    public static void incrementNombreRencontreEnAttente() {
        nombreRencontreEnAttente++;
    }

    public static void decrementNombreRencontreEnAttente(Rencontre rencontre) {
        rencontresEnAttente.remove(rencontre);
        if (nombreRencontreEnAttente > 0) {
            nombreRencontreEnAttente--;
        }
    }

    Rencontre() {
        this.idRencontre = nombreRencontreEnAttente;
        this.initiateur = null;
        this.adversaire = null;
        this.nombreTours = 0;
        this.historiqueJ1 = new ArrayList<>();
        this.historiqueJ2 = new ArrayList<>();
        this.tours = new ArrayList<>();
        incrementNombreRencontreEnAttente();
    }
    
    Rencontre(Joueur initiateur,int nombreTours) {
        this();
        this.initiateur = initiateur;
        this.nombreTours = nombreTours;
    }
    
    Rencontre(Joueur initiateur, Joueur adversaire, int nombreTours) {
        this();
        this.initiateur = initiateur;
        this.adversaire = adversaire;
        this.nombreTours = nombreTours;
    }
    // Méthode synchronisée pour ajouter une rencontre en attente
    public static synchronized void addToRencontreEnAttente(Rencontre rencontre) {
        rencontresEnAttente.add(rencontre);
    }

    public static List<Rencontre> getRencontresEnAttente() {
        return rencontresEnAttente;
    }

    @Override
    public void run() {
        try {
            initiateur.setEtat(EtatJoueur.EN_PARTIE);
            adversaire.setEtat(EtatJoueur.EN_PARTIE);
            initiateur.sendMessage("Rencontre commencée avec " + adversaire.getNom());
            adversaire.sendMessage("Rencontre commencée avec " + initiateur.getNom());

            for (int i = 1; i <= nombreTours; i++) {
                sendTourMessage(initiateur, i);
                sendTourMessage(adversaire, i);

                int previousScoreInitiateur = getPreviousScore(initiateur);
                int previousScoreAdversaire = getPreviousScore(adversaire);

                TypeAction action1 = initiateur.jouer(historiqueJ2, previousScoreInitiateur);
                TypeAction action2 = adversaire.jouer(historiqueJ1, previousScoreAdversaire);

                if (action1 == TypeAction.ABONDONNER) {
                    initiateur = handleAbandon(initiateur);
                    action1 = getPlayerAction(initiateur, historiqueJ2, previousScoreInitiateur);
                }
                if (action2 == TypeAction.ABONDONNER) {
                    adversaire = handleAbandon(adversaire);
                    action2 = getPlayerAction(adversaire, historiqueJ1, previousScoreAdversaire);
                }

                historiqueJ1.add(action1);
                historiqueJ2.add(action2);

                Tour tour = new Tour(action1, action2);
                tours.add(tour);

                updatePlayerScore(initiateur, tour.getScoreInitiateur());
                updatePlayerScore(adversaire, tour.getScoreAdversaire());

                sendTourResults(initiateur, action1, action2, tour.getScoreInitiateur());
                sendTourResults(adversaire, action2, action1, tour.getScoreAdversaire());
            }
            sendFinResult(initiateur);
            sendFinResult(adversaire);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void sendTourMessage(Joueur joueur, int tourNumber) {
        if (joueur instanceof Humain) {
            joueur.sendMessage("Tour " + tourNumber);
        }
    }

    private int getPreviousScore(Joueur joueur) {
        if (!tours.isEmpty()) {
            Tour lastTour = tours.get(tours.size() - 1);
            if (joueur == initiateur) {
                return lastTour.getScoreInitiateur();
            } else if (joueur == adversaire) {
                return lastTour.getScoreAdversaire();
            }
        }
        return 0;
    }

    private Joueur handleAbandon(Joueur joueur) throws Exception {
        Strategie strategie = getStrategie(joueur);
        joueur.close();
        return new Robot(joueur.getId() + "_ai", joueur.getNom() + "_ai", joueur.getScore(), strategie);
    }

    private TypeAction getPlayerAction(Joueur joueur, List<TypeAction> opponentHistory, int previousScore) {
        return joueur.jouer(opponentHistory, previousScore);
    }

    private void updatePlayerScore(Joueur joueur, int score) {
        if (joueur instanceof Humain) {
            joueur.addScore(score);
        }
    }

    private void sendTourResults(Joueur joueur, TypeAction playerAction, TypeAction opponentAction, int scoreThisTour) throws Exception {
        if (joueur instanceof Humain) {
            joueur.sendMessage("Résultat du tour: Vous avez " + playerAction + ", l'adversaire a " + opponentAction + ".");
            joueur.sendMessage("Votre score pour ce tour: " + scoreThisTour + ". Score total: " + joueur.getScore() + ".");
        }
    }

    private void sendFinResult(Joueur joueur) throws Exception {
        logger.info("FIN");
        if (joueur instanceof Humain) {
            String resultat = determinerResultat(joueur);
            joueur.sendMessage("La partie est terminée. Vous avez " + resultat + " !");
            joueur.sendMessage("Votre score final est de " + joueur.getScore() + ".");
            joueur.sendMessage("Bye");
        }
    }

    private String determinerResultat(Joueur joueur) {
        int scoreJoueur = joueur.getScore();
        int scoreAdversaire = (joueur == initiateur) ? adversaire.getScore() : initiateur.getScore();

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
        return switch (typeStrategie) {
            case DONNANTDONNANT -> new DonnantDonnantStrategie();
            case DONNANTDONNANTALEATOIRE -> new DonnantDonnantAleatoireStrategie(new SecureRandom());
            case DONNANTPOURDEUXDONNANTS -> new DonnantPourDeuxDonnantsStrategie();
            case DONNANTPOURDEUXDONNANTSALEATOIRE -> new DonnantPourDeuxDonnantsEtAleatoireStrategie(new SecureRandom(), new SecureRandom());
            case SONDEURNAIF -> new SondeurNaifStrategie(new SecureRandom());
            case SONDEURREPENTANT -> new SondeurRepentantStrategie(new SecureRandom());
            case PACIFICATEURNAIF -> new PacificateurNaifStrategie(new SecureRandom());
            case VRAIPACIFICATEUR -> new VraiPacificateurStrategie(new SecureRandom());
            case ALEATOIRE -> new AleatoireStrategie(new SecureRandom());
            case TOUJOURSTRAHIR -> new ToujoursTrahirStrategie();
            case TOUJOURSCOOPERER -> new ToujoursCoopererStrategie();
            case RANCUNIERSTRATEGIE -> new RancunierStrategie();
            case PAVLOVSTRATEGIE -> new PavlovStrategie();
            case PAVLOVALEATOIRE -> new PavlovAleatoireStrategie(new SecureRandom());
            case ADAPTATIF -> new AdaptatifStrategie();
            case GRADUEL -> new GraduelStrategie();
            case DONNANTDONNANTSOUPCONNEUX -> new DonnantDonnantSoupconneuxStrategie();
            case RANCUNIERDOUX -> new RancunierDouxStrategie();
        };
    }
}