package fr.uga.miage.m1.my_project.service;


import fr.uga.miage.m1.my_project.model.Tour;
import fr.uga.miage.m1.my_project.model.enums.TypeAction;
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
}
