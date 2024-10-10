package fr.uga.miage.m1.my_project.models;

import fr.uga.miage.m1.my_project.enums.TypeAction;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
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




    public void afficherScores() {
        System.out.println(this.rencontre.getJoueur1().getScore() + " " + this.rencontre.getJoueur1().getScore());
    }

    // Getters, Setters, et autres méthodes
}