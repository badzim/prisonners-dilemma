package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.util.List;

public class RancunierDouxStrategie extends Strategie {

    private boolean isPunishing = false;  // Indique si la stratégie est en mode punition
    private int punishmentCounter = 0;    // Compteur pour suivre la séquence punitive

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        // Vérifie si l'adversaire a trahi au dernier tour et démarre la punition si ce n'est pas déjà le cas
        if (!isPunishing && actions != null && !actions.isEmpty()
                && actions.get(actions.size() - 1) == TYPE_ACTION.TRAHIR) {
            isPunishing = true;
            punishmentCounter = 0;
        }

        // Exécuter la séquence punitive si on est en mode punition
        if (isPunishing) {
            TYPE_ACTION action = (punishmentCounter < 5) ? TYPE_ACTION.TRAHIR : TYPE_ACTION.COOPERER;
            punishmentCounter++;
            if (punishmentCounter >= 7) { // Fin de la séquence punitive après 7 actions
                isPunishing = false;  // Sortie du mode punition
            }
            return action;
        }

        // Coopérer par défaut en dehors de la punition
        return TYPE_ACTION.COOPERER;
    }
}
