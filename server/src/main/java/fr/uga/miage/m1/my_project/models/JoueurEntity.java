package fr.uga.miage.m1.my_project.models;


import fr.uga.miage.m1.my_project.enums.TypeAction;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class JoueurEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private int score;

    // Joueur côté proprio de la relation
    // Relation Many-to-Many unidirectionnelle
    @ManyToMany
    @JoinTable(
            name = "joueur_strategie",  // Table d'association entre Student et Course
            joinColumns = @JoinColumn(name = "joueur_id"),  // Clé étrangère vers Student
            inverseJoinColumns = @JoinColumn(name = "strategie_id")  // Clé étrangère vers Course
    )
    private List<StrategieEntity> strategieAutomatiques; // un joueur peut utiliser plusieur strategie dans une journé de partie

    // Relation bidirectionnelle - Joueur peut initier plusieurs rencontres
    @OneToMany(mappedBy = "joueurInitie")
    private List<RencontreEntity> rencontresInitiees;

    // Relation One-to-Mane - Joueur peut rejoindre plusieurs rencontre
    @OneToMany(mappedBy = "joueurRejoint")
    private List<RencontreEntity> rencontresRejoints;

    public void jouer(List<TypeAction> historiqueAdversaire) {
        return ;
    }

}
