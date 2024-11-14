package fr.uga.miage.m1.my_project.server.models.strategies;

import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;

import java.util.List;

public class GraduelStrategie extends Strategie {

    private int trahisonsARendre = 0;
    private int cooperationsARendre = 0;
    private boolean enVengeance = false;

    @Override
    public TypeAction getAction(List<TypeAction> actionsAdversaire, int dernierResultat) {
        // Si c'est le premier tour, coopérer
        if (actionsAdversaire.isEmpty()) {
            return TypeAction.COOPERER;
        }

        // Vérifier si l'adversaire a trahi au dernier tour et que nous ne sommes pas déjà en vengeance
        if (!enVengeance && actionsAdversaire.get(actionsAdversaire.size() - 1) == TypeAction.TRAHIR) {
            trahisonsARendre = calculerNombreDeTrahisons(actionsAdversaire);
            cooperationsARendre = 2; // Après la vengeance, coopérer deux fois
            enVengeance = true;
        }

        if (enVengeance) {
            if (trahisonsARendre > 0) {
                trahisonsARendre--;
                return TypeAction.TRAHIR;
            } else if (cooperationsARendre > 0) {
                cooperationsARendre--;
                return TypeAction.COOPERER;
            } else {
                enVengeance = false; // Fin de la vengeance
            }
        }

        return TypeAction.COOPERER;
    }

    private int calculerNombreDeTrahisons(List<TypeAction> actionsAdversaire) {
        int nombreDeTrahisons = 0;
        for (TypeAction action : actionsAdversaire) {
            if (action == TypeAction.TRAHIR) {
                nombreDeTrahisons++;
            }
        }
        return nombreDeTrahisons;
    }
}
