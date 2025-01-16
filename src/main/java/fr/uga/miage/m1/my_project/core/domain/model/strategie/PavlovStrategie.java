package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.util.List;



public class PavlovStrategie extends Strategie {

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        // Vérifier si la liste est nulle ou vide
        if (actions == null || actions.isEmpty()) {
            // Définir une action par défaut, par exemple, COOPERER
            return TYPE_ACTION.COOPERER;
        }

        TYPE_ACTION lastAction = actions.get(actions.size() - 1);

        if (dernierResultat == 5 || dernierResultat == 3) {
            return lastAction;
        } else {
            return (lastAction == TYPE_ACTION.TRAHIR) ? TYPE_ACTION.TRAHIR : TYPE_ACTION.COOPERER;
        }
    }
}