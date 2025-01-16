package fr.uga.miage.m1.my_project.core.domain.service;


import fr.uga.miage.m1.my_project.core.exception.rest.InvalidActionRestException;
import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;
import fr.uga.miage.m1.my_project.core.domain.model.Tour;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Humain;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;
import fr.uga.miage.m1.my_project.core.port.input.TourServicePort;
import org.springframework.stereotype.Service;

@Service
public class TourService implements TourServicePort {
    public void calculerScore(Tour tour) {
        TYPE_ACTION actionInitiateur = tour.getActionInitiateur();
        TYPE_ACTION actionAdversaire = tour.getActionAdversaire();
        int scoreInitiateur;
        int scoreAdversaire;
        if (actionInitiateur == TYPE_ACTION.COOPERER && actionAdversaire == TYPE_ACTION.COOPERER) {
            scoreInitiateur = 3;
            scoreAdversaire = 3;
        } else if (actionInitiateur == TYPE_ACTION.COOPERER && actionAdversaire == TYPE_ACTION.TRAHIR) {
            scoreInitiateur = 0;
            scoreAdversaire = 5;
        } else if (actionInitiateur == TYPE_ACTION.TRAHIR && actionAdversaire == TYPE_ACTION.COOPERER) {
            scoreInitiateur = 5;
            scoreAdversaire = 0;
        } else { // Both betray
            scoreInitiateur = 1;
            scoreAdversaire = 1;
        }
        tour.setScoreInitiateur(scoreInitiateur);
        tour.setScoreAdversaire(scoreAdversaire);
    }

    public void setActionJoueur(Joueur joueur, Rencontre rencontre, TYPE_ACTION actionJoueur) {
        Tour tour = rencontre.getCurrentTour();
        Joueur initiateur = rencontre.getInitiateur();
        Joueur adversaire = rencontre.getAdversaire();


        if (joueur.equals(initiateur)) {
            if (tour.getActionInitiateur() != null && initiateur instanceof Humain) {
                throw new InvalidActionRestException("Vous avez déjà fait votre choix pour ce tour.");
            }
            tour.setActionInitiateur(actionJoueur);
        } else if (joueur.equals(adversaire)) {
            if (tour.getActionAdversaire() != null && adversaire instanceof Humain) {
                throw new InvalidActionRestException("Vous avez déjà fait votre choix pour ce tour.");
            }
            tour.setActionAdversaire(actionJoueur);
        } else {
            throw new InvalidActionRestException("Le joueur ne fait pas partie de cette rencontre.");
        }
    }


}
