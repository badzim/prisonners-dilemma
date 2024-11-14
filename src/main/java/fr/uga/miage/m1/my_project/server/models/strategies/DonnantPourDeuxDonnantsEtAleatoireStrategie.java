package fr.uga.miage.m1.my_project.server.models.strategies;

import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;

import java.security.SecureRandom;
import java.util.List;

public class DonnantPourDeuxDonnantsEtAleatoireStrategie extends Strategie {



    public DonnantPourDeuxDonnantsEtAleatoireStrategie(SecureRandom random, SecureRandom secondRandom) {
        super(random, secondRandom);
    }

    @Override
    public TypeAction getAction(List<TypeAction> actions, int dernierResultat) {
        // Parfois, joue un coup au hasard (par exemple, 10% du temps)
        if (getRandom().nextInt(10) == 0) {
            return getSecondRandom().nextBoolean() ? TypeAction.COOPERER : TypeAction.TRAHIR;
        }

        // Si aucune action précédente, coopère par défaut
        if (actions.isEmpty()) {
            return TypeAction.COOPERER;
        }

        // Si l'adversaire a fait le même choix deux fois de suite, reproduit ce choix
        if (actions.size() > 1 && actions.get(actions.size() - 1) == actions.get(actions.size() - 2)) {
            return actions.get(actions.size() - 1);
        }

        // Sinon, trahit
        return TypeAction.TRAHIR;
    }
}
