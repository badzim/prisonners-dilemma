package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.util.List;
import java.security.SecureRandom;

public class SondeurNaifStrategie extends Strategie {

    private static final double PROBABILITE_TRAHISON = 0.2; // 20% de chance de trahir même si l'adversaire a coopéré

    public SondeurNaifStrategie(SecureRandom random) {
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

        // Avec une probabilité, trahit même si l'adversaire a coopéré
        if (this.getRandom().nextDouble() < PROBABILITE_TRAHISON) {
            return TYPE_ACTION.TRAHIR;
        }

        // Sinon, répète l'action de l'adversaire
        return derniereAction;
    }
}
