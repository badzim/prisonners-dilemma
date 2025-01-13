package fr.uga.miage.m1.my_project.core.domain.model;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Tour {
    private TYPE_ACTION actionInitiateur;
    private TYPE_ACTION actionAdversaire;
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