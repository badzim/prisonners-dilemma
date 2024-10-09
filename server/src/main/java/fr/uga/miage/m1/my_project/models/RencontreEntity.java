package fr.uga.miage.m1.my_project.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class RencontreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rencontreId;

    @ManyToOne
    @JoinColumn(name = "joueur1_id")
    private JoueurEntity joueur1;

    @ManyToOne
    @JoinColumn(name = "joueur2_id")
    private JoueurEntity joueur2;

    private int nombreTours;


    // Unidirectionnel : Rencontre connaît Tour, mais Tour ne connaît pas Rencontre
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "rencontre_id")  // Cette annotation place la clé étrangère dans la table Tour
    private List<TourEntity> tours;

    // Relation Many-to-One - Une rencontre est initiée par un seul joueur
    @ManyToOne
    @JoinColumn(name = "joueur_initiateur_id", nullable = false)
    private JoueurEntity joueurInitie;

    // Relation bidirectionnelle - Une rencontre peut être rejointe par un joueur
    @ManyToOne
    @JoinColumn(name = "joueur_rejoint_id")
    private JoueurEntity joueurRejoint;

    public void demarrer() {
        // Logique pour démarrer
    }

    public void jouerTour() {
        // Logique pour jouer un tour
    }
}