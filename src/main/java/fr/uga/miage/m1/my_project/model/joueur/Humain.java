package fr.uga.miage.m1.my_project.model.joueur;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import java.util.List;

public class Humain extends Joueur {
    public Humain(String id, String nom) {
        super(id, nom);
    }

    @Override
    public TypeAction jouer(List<TypeAction> historiqueAdversaire, int dernierResultat)  {
        return TypeAction.COOPERER;
    }
}