package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import java.security.SecureRandom;
import java.util.List;

public class PacificateurNaifStrategie extends Strategie {

    private static final double PROBABILITE_COOPERATION = 0.2; // 20% de chance de coopérer même si l'adversaire a trahi

    public PacificateurNaifStrategie(SecureRandom random) {
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

        // Si l'adversaire a trahi, il y a une probabilité de coopérer malgré cela
        if (derniereAction == TypeAction.TRAHIR && getRandom().nextDouble() < PROBABILITE_COOPERATION) {
            return TypeAction.COOPERER; // Faire la paix, coopérer malgré la trahison
        }

        // Sinon, répète l'action de l'adversaire
        return derniereAction;
    }
}
