package fr.uga.miage.m1.my_project.service;


import fr.uga.miage.m1.my_project.core.exception.rest.InvalidActionRestException;
import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;
import fr.uga.miage.m1.my_project.core.domain.model.Tour;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Humain;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;
import org.springframework.stereotype.Service;

@Service
public class TourService {
    public void calculerScore(Tour tour) {
        TypeAction actionInitiateur = tour.getActionInitiateur();
        TypeAction actionAdversaire = tour.getActionAdversaire();
        int scoreInitiateur;
        int scoreAdversaire;
        if (actionInitiateur == TypeAction.COOPERER && actionAdversaire == TypeAction.COOPERER) {
            scoreInitiateur = 3;
            scoreAdversaire = 3;
        } else if (actionInitiateur == TypeAction.COOPERER && actionAdversaire == TypeAction.TRAHIR) {
            scoreInitiateur = 0;
            scoreAdversaire = 5;
        } else if (actionInitiateur == TypeAction.TRAHIR && actionAdversaire == TypeAction.COOPERER) {
            scoreInitiateur = 5;
            scoreAdversaire = 0;
        } else { // Both betray
            scoreInitiateur = 1;
            scoreAdversaire = 1;
        }
        tour.setScoreInitiateur(scoreInitiateur);
        tour.setScoreAdversaire(scoreAdversaire);
    }

    public void setActionJoueur(Joueur joueur, Rencontre rencontre, TypeAction actionJoueur) {
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
