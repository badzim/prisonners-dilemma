package fr.uga.miage.m1.my_project.server.models.strategies;

import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;
import java.util.List;

public class DonnantDonnantSoupconneuxStrategie extends Strategie {

    private boolean isFirstMove = true; // Variable pour vérifier si c'est le premier coup

    @Override
    public TypeAction getAction(List<TypeAction> actions, int dernierResultat) {
        // Si c'est le premier coup, trahir
        if (isFirstMove) {
            isFirstMove = false;
            return TypeAction.TRAHIR;
        }

        // Après le premier coup, imiter le dernier coup de l'adversaire
        if (actions != null && !actions.isEmpty()) {
            return actions.get(actions.size() - 1); // Reproduit le dernier coup de l'adversaire
        }

        // Par défaut, si aucune action passée n'est disponible, coopérer
        return TypeAction.COOPERER;
    }
}
