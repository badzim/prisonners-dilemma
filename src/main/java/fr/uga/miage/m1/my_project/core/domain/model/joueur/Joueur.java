package fr.uga.miage.m1.my_project.core.domain.model.joueur;

import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_JOUEUR;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.strategie.Strategie;
import lombok.Data;
import java.util.List;

@Data
public abstract class Joueur {
    public static final String NOT_SUPPORTED_YET = "Not supported yet.";
    protected String id;
    protected String nom;
    protected int score;
    protected Strategie strategieAutomatique;
    protected ETAT_JOUEUR etat;

    protected Joueur(String id, String nom) {
        this.id = id;
        this.nom = nom;
        this.score = 0;
    }

    public void addScore(int s) {
        this.score += s;
    }

    public abstract TYPE_ACTION jouer(List<TYPE_ACTION> historiqueAdversaire, int dernierResultat);
}