package fr.uga.miage.m1.my_project.models;

import fr.uga.miage.m1.my_project.enums.TypeAction;
import jakarta.persistence.*;

@Entity
public class TourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rencontre_id")
    private RencontreEntity rencontre;

    @Enumerated(EnumType.STRING)
    private TypeAction actionJoueur1;

    @Enumerated(EnumType.STRING)
    private TypeAction actionJoueur2;

    public int calculerScore() {
        // Logique pour calculer les scores
        return 0;
    }

    public void afficherScores() {
        // Logique pour afficher les scores
    }

    // Getters, Setters, et autres méthodes
}