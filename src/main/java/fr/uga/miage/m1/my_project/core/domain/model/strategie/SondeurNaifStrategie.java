package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import java.util.List;
import java.security.SecureRandom;

public class SondeurNaifStrategie extends Strategie {

    private static final double PROBABILITE_TRAHISON = 0.2; // 20% de chance de trahir même si l'adversaire a coopéré

    public SondeurNaifStrategie(SecureRandom random) {
        super(random);
    }

    @Override
    public TypeAction getAction(List<TypeAction> actions, int dernierResultat) {
        // Si aucune action précédente, coopère par défaut
        if (actions.isEmpty()) {
            return TypeAction.COOPERER;
        }

        // Récupère la dernière action de l'adversaire
        TypeAction derniereAction = actions.get(actions.size() - 1);

        // Avec une probabilité, trahit même si l'adversaire a coopéré
        if (this.getRandom().nextDouble() < PROBABILITE_TRAHISON) {
            return TypeAction.TRAHIR;
        }

        // Sinon, répète l'action de l'adversaire
        return derniereAction;
    }
}
