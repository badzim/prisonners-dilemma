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

    @ManyToOne
    @JoinColumn(name = "strategieId")
    private StrategieEntity strategieAutomatique;

    public void jouer(List<TypeAction> historiqueAdversaire) {
        return ;
    }

}
