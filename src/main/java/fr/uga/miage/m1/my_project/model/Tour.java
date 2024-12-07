package fr.uga.miage.m1.my_project.model;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Tour {
    private TypeAction actionInitiateur;
    private TypeAction actionAdversaire;
    private int scoreInitiateur;
    private int scoreAdversaire;
    private int numeroTour;

    public Tour() {
        this.actionInitiateur = null;
        this.actionAdversaire = null;
        this.scoreInitiateur = 0;
        this.scoreAdversaire = 0;
        this.numeroTour = 0;
    }

    public Tour(int numeroTour) {
        this();
        this.numeroTour = numeroTour;
    }
}