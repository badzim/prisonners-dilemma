package fr.uga.miage.m1.my_project.core.domain.model.joueur;

import fr.uga.miage.m1.my_project.core.domain.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import java.util.List;

public class Humain extends Joueur {
    public Humain(String id, String nom) {
        super(id, nom);
        this.etat = EtatJoueur.EN_MENU;
    }

    @Override
    public TypeAction jouer(List<TypeAction> historiqueAdversaire, int dernierResultat)  {
        return TypeAction.COOPERER;
    }
}