package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.util.List;

public class DonnantDonnantStrategie extends Strategie {

    // Jouer comme le dernier coup de l'adversaire
    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        if (actions.isEmpty()) {
            return TYPE_ACTION.COOPERER;
        }
        return getLastAction(actions);
    }
}
