package fr.uga.miage.m1.my_project.model.strategie;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import java.security.SecureRandom;
import java.util.List;

public class VraiPacificateurStrategie extends Strategie {

    private static final double PROBABILITE_COOPERATION = 0.2; // 20% de chance de coopérer même après deux trahisons successives

    public VraiPacificateurStrategie(SecureRandom random) {
        super(random);
    }

    @Override
    public TypeAction getAction(List<TypeAction> actions, int dernierResultat) {
        // Si aucune action précédente, coopère par défaut
        if (actions.isEmpty()) {
            return TypeAction.COOPERER;
        }

        // Récupère les deux dernières actions de l'adversaire
        TypeAction derniereAction = actions.get(actions.size() - 1);
        TypeAction avantDerniereAction = (actions.size() > 1) ? actions.get(actions.size() - 2) : null;

        // Si l'adversaire a trahi deux fois de suite, on trahit immédiatement
        if (avantDerniereAction == TypeAction.TRAHIR && derniereAction == TypeAction.TRAHIR) {
            // Avec une probabilité de coopérer malgré la trahison
            if (getRandom().nextDouble() < PROBABILITE_COOPERATION) {
                return TypeAction.COOPERER;
            }
            return TypeAction.TRAHIR;
        }

        // Sinon, on répète l'action de l'adversaire
        return derniereAction;
    }
}
