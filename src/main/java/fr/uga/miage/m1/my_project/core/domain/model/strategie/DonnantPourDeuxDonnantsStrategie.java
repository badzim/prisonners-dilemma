package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.util.List;

public class DonnantPourDeuxDonnantsStrategie extends Strategie {

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        // Si aucune action précédente, coopère par défaut
        if (actions.isEmpty()) {
            return TYPE_ACTION.COOPERER;
        }

        // Si l'adversaire a coopéré deux fois consécutives, coopère
        if (actions.size() > 1 && actions.get(actions.size() - 1) == TYPE_ACTION.COOPERER
                && actions.get(actions.size() - 2) == TYPE_ACTION.COOPERER) {
            return TYPE_ACTION.COOPERER;
        }

        // Sinon, trahit
        return TYPE_ACTION.TRAHIR;
    }
}
