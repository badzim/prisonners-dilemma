package fr.uga.miage.m1.my_project.models;

import fr.uga.miage.m1.my_project.enums.TypeAction;
import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("DonnantDonnant")
public class DonnantDonnantEntity extends StrategieEntity {

    public TypeAction getAction(List<TypeAction> historiqueAdversaire) {
        return null;
    }
}