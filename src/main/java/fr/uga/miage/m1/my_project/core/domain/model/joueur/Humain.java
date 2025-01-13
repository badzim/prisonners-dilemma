package fr.uga.miage.m1.my_project.core.domain.model.joueur;

import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_JOUEUR;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.util.List;

public class Humain extends Joueur {
    public Humain(String id, String nom) {
        super(id, nom);
        this.etat = ETAT_JOUEUR.EN_MENU;
    }

    @Override
    public TYPE_ACTION jouer(List<TYPE_ACTION> historiqueAdversaire, int dernierResultat)  {
        return TYPE_ACTION.COOPERER;
    }
}