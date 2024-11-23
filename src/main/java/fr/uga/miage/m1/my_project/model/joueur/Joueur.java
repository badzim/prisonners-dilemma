package fr.uga.miage.m1.my_project.model.joueur;

import fr.uga.miage.m1.my_project.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.model.strategie.Strategie;
import lombok.Data;
import java.util.List;

@Data
public abstract class Joueur {
    public static final String NOT_SUPPORTED_YET = "Not supported yet.";
    protected String id;
    protected String nom;
    protected int score;
    protected Strategie strategieAutomatique;
    protected EtatJoueur etat;

    protected Joueur(String id, String nom) {
        this.id = id;
        this.nom = nom;
        this.score = 0;
    }

    public void addScore(int s) {
        this.score += s;
    }

    public abstract TypeAction jouer(List<TypeAction> historiqueAdversaire, int dernierResultat);

    public void sendMessage(Object obj)  {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    public Object receiveMessage() {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }

    public void close() {
        throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
    }
}