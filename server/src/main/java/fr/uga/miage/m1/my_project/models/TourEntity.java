package fr.uga.miage.m1.my_project.models;

import fr.uga.miage.m1.my_project.enums.TypeAction;
import jakarta.persistence.*;

@Entity
public class TourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Coefficients selon le dilemme du prisonnier
    private final int T = 5;  // Trahir et l'autre coopère
    private final int D = 0;  // Coopérer et l'autre trahit
    private final int C = 3;  // Les deux coopèrent
    private final int P = 1;  // Les deux trahissent

    @ManyToOne
    @JoinColumn(name = "rencontre_id")
    private RencontreEntity rencontre;

    @Enumerated(EnumType.STRING)
    private TypeAction actionJoueur1;

    @Enumerated(EnumType.STRING)
    private TypeAction actionJoueur2;


    // Méthode pour calculer les scores des deux joueurs
    public void calculerScore() {
        // Créer une matrice des scores pour toutes les combinaisons possibles
        int[][] scores = {
                {C, C}, // [c, c] : les deux coopèrent
                {T, D}, // [t, c] : Joueur 1 trahit, Joueur 2 coopère
                {D, T}, // [c, t] : Joueur 1 coopère, Joueur 2 trahit
                {P, P}  // [t, t] : les deux trahissent
        };

        int i = actionJoueur1 == TypeAction.COOPERER ? 0 : 1;
        int j = actionJoueur2 == TypeAction.COOPERER ? 0 : 1;

        // Met à jour les scores des deux joueurs dans la rencontre
        JoueurEntity joueur1 = this.rencontre.getJoueur1();
        JoueurEntity joueur2 = this.rencontre.getJoueur2();

        joueur1.setScore(joueur1.getScore() + scores[i * 2 + j][0]);
        joueur2.setScore(joueur2.getScore() + scores[i * 2 + j][1]);
    }

    public void afficherScores() {
        System.out.println(this.rencontre.getJoueur1().getScore() + " " + this.rencontre.getJoueur1().getScore());
    }

    // Getters, Setters, et autres méthodes
}