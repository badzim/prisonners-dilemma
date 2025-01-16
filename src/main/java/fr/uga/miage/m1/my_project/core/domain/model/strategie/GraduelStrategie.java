package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;

import java.util.List;

public class GraduelStrategie extends Strategie {

    private int trahisonsARendre = 0;
    private int cooperationsARendre = 0;
    private boolean enVengeance = false;

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actionsAdversaire, int dernierResultat) {
        // Si c'est le premier tour, coopérer
        if (actionsAdversaire.isEmpty()) {
            return TYPE_ACTION.COOPERER;
        }

        // Vérifier si l'adversaire a trahi au dernier tour et que nous ne sommes pas déjà en vengeance
        if (!enVengeance && actionsAdversaire.get(actionsAdversaire.size() - 1) == TYPE_ACTION.TRAHIR) {
            trahisonsARendre = calculerNombreDeTrahisons(actionsAdversaire);
            cooperationsARendre = 2; // Après la vengeance, coopérer deux fois
            enVengeance = true;
        }

        if (enVengeance) {
            if (trahisonsARendre > 0) {
                trahisonsARendre--;
                return TYPE_ACTION.TRAHIR;
            } else if (cooperationsARendre > 0) {
                cooperationsARendre--;
                return TYPE_ACTION.COOPERER;
            } else {
                enVengeance = false; // Fin de la vengeance
            }
        }

        return TYPE_ACTION.COOPERER;
    }

    private int calculerNombreDeTrahisons(List<TYPE_ACTION> actionsAdversaire) {
        int nombreDeTrahisons = 0;
        for (TYPE_ACTION action : actionsAdversaire) {
            if (action == TYPE_ACTION.TRAHIR) {
                nombreDeTrahisons++;
            }
        }
        return nombreDeTrahisons;
    }
}
