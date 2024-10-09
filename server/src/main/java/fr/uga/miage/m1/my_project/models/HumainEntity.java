package fr.uga.miage.m1.my_project.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
public class HumainEntity extends JoueurEntity {

    public void choisirStrategieAutomatique(StrategieEntity strategie) {
        this.setStrategieAutomatique(strategie);
    }

    public void abandonner() {
        // Logique pour abandonner
    }

    public RencontreEntity initier() {
        // Logique pour initier une rencontre
        return null;
    }

    public void rejoindre() {
        // Logique pour rejoindre une rencontre
    }
}
