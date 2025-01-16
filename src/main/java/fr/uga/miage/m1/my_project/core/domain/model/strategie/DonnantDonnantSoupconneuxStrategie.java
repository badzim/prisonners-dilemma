package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.util.List;

public class DonnantDonnantSoupconneuxStrategie extends Strategie {

    private boolean isFirstMove = true; // Variable pour vérifier si c'est le premier coup

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        // Si c'est le premier coup, trahir
        if (isFirstMove) {
            isFirstMove = false;
            return TYPE_ACTION.TRAHIR;
        }

        // Après le premier coup, imiter le dernier coup de l'adversaire
        if (actions != null && !actions.isEmpty()) {
            return actions.get(actions.size() - 1); // Reproduit le dernier coup de l'adversaire
        }

        // Par défaut, si aucune action passée n'est disponible, coopérer
        return TYPE_ACTION.COOPERER;
    }
}
