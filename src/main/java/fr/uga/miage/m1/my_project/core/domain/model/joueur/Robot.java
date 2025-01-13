package fr.uga.miage.m1.my_project.core.domain.model.joueur;

import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_JOUEUR;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.strategie.Strategie;
import java.util.List;

public class Robot extends Joueur {

    public Robot(String id, String nom, int score) {
        super(id, nom);
        this.score = score;
    }

    public Robot(String id, String nom, int score, Strategie strategie, ETAT_JOUEUR etat) {
        this(id, nom, score);
        this.strategieAutomatique = strategie;
        this.etat = etat;
    }

    @Override
    public TYPE_ACTION jouer(List<TYPE_ACTION> historiqueAdversaire, int dernierResultat) {
        // Si la stratégie est interne
        if (strategieAutomatique != null) {
            return strategieAutomatique.getAction(historiqueAdversaire, dernierResultat);
        }
        return TYPE_ACTION.COOPERER; // Valeur par défaut
    }
}