package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.security.SecureRandom;
import java.util.List;

public class PacificateurNaifStrategie extends Strategie {

    private static final double PROBABILITE_COOPERATION = 0.2; // 20% de chance de coopérer même si l'adversaire a trahi

    public PacificateurNaifStrategie(SecureRandom random) {
        super(random);
    }

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        // Si aucune action précédente, coopère par défaut
        if (actions.isEmpty()) {
            return TYPE_ACTION.COOPERER;
        }

        // Récupère la dernière action de l'adversaire
        TYPE_ACTION derniereAction = actions.get(actions.size() - 1);

        // Si l'adversaire a trahi, il y a une probabilité de coopérer malgré cela
        if (derniereAction == TYPE_ACTION.TRAHIR && getRandom().nextDouble() < PROBABILITE_COOPERATION) {
            return TYPE_ACTION.COOPERER; // Faire la paix, coopérer malgré la trahison
        }

        // Sinon, répète l'action de l'adversaire
        return derniereAction;
    }
}
