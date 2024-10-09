package fr.uga.miage.m1.my_project.models;

import fr.uga.miage.m1.my_project.enums.TypeAction;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("DonnantDonnantAleatoire")
public class DonnantDonnantAleatoireEntity extends StrategieEntity {
    
    public TypeAction getAction(List<TypeAction> historiqueAdversaire) {
        return null;
    }
}