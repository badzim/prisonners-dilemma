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

    public Tour(TypeAction actionInitiateur, TypeAction actionAdversaire) {
        this.actionInitiateur = actionInitiateur;
        this.actionAdversaire = actionAdversaire;
    }
}