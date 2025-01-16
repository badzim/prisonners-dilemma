package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.security.SecureRandom;
import java.util.List;

public class VraiPacificateurStrategie extends Strategie {

    private static final double PROBABILITE_COOPERATION = 0.2; // 20% de chance de coopérer même après deux trahisons successives

    public VraiPacificateurStrategie(SecureRandom random) {
        super(random);
    }

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        // Si aucune action précédente, coopère par défaut
        if (actions.isEmpty()) {
            return TYPE_ACTION.COOPERER;
        }

        // Récupère les deux dernières actions de l'adversaire
        TYPE_ACTION derniereAction = actions.get(actions.size() - 1);
        TYPE_ACTION avantDerniereAction = (actions.size() > 1) ? actions.get(actions.size() - 2) : null;

        // Si l'adversaire a trahi deux fois de suite, on trahit immédiatement
        if (avantDerniereAction == TYPE_ACTION.TRAHIR && derniereAction == TYPE_ACTION.TRAHIR) {
            // Avec une probabilité de coopérer malgré la trahison
            if (getRandom().nextDouble() < PROBABILITE_COOPERATION) {
                return TYPE_ACTION.COOPERER;
            }
            return TYPE_ACTION.TRAHIR;
        }

        // Sinon, on répète l'action de l'adversaire
        return derniereAction;
    }
}
