package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.util.List;

public class RancunierStrategie extends Strategie{

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        // On vérifie le dernier coup de l'adverssaire
        if (!actions.isEmpty() && getLastAction(actions) == TYPE_ACTION.TRAHIR) {
            return TYPE_ACTION.TRAHIR;
        }
        // Sinon, nous coopérons
        return TYPE_ACTION.COOPERER;
    }
}
