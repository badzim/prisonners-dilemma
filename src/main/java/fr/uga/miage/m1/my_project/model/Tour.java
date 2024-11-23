package fr.uga.miage.m1.my_project.model;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import lombok.Data;

@Data
public class Tour {
    private TypeAction actionInitiateur;
    private TypeAction actionAdversaire;
    private int scoreInitiateur;
    private int scoreAdversaire;

    public Tour(TypeAction actionInitiateur, TypeAction actionAdversaire) {
        this.actionInitiateur = actionInitiateur;
        this.actionAdversaire = actionAdversaire;
        calculerScore();
    }

    private void calculerScore() {
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
    }
    
}