package fr.uga.miage.m1.my_project.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "strategie_type", discriminatorType = DiscriminatorType.STRING)
public abstract class StrategieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long strategieId;

    private String nom;

}
