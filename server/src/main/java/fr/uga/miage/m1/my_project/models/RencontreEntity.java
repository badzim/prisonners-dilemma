package fr.uga.miage.m1.my_project.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class RencontreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "joueur1_id")
    private JoueurEntity joueur1;

    @ManyToOne
    @JoinColumn(name = "joueur2_id")
    private JoueurEntity joueur2;

    private int nombreTours;

    @OneToMany(mappedBy = "rencontre", cascade = CascadeType.ALL)
    private List<TourEntity> tours;

    public void demarrer() {
        // Logique pour démarrer
    }

    public void jouerTour() {
        // Logique pour jouer un tour
    }
}