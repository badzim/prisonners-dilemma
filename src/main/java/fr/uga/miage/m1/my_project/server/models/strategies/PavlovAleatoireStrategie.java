package fr.uga.miage.m1.my_project.server.models.strategies;

import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;
import java.util.List;
import java.security.SecureRandom;

public class PavlovAleatoireStrategie extends Strategie {
    private static final double RANDOM_PROBABILITY = 0.2;
    private TypeAction lastAction = TypeAction.COOPERER;

    public PavlovAleatoireStrategie(SecureRandom random) {
        super(random);
    }

    @Override
    public TypeAction getAction(List<TypeAction> actions, int dernierResultat) {
        if (getRandom().nextDouble() < RANDOM_PROBABILITY) {
            // Choisir aléatoirement une action
            lastAction = getRandom().nextBoolean() ? TypeAction.COOPERER : TypeAction.TRAHIR;
        } else if (dernierResultat == 5 || dernierResultat == 3) {
            // Répéter le dernier choix si 5 ou 3 points ont été obtenus
            // lastAction reste inchangé
        } else {
            // Inverser l'action si autre résultat
            lastAction = (lastAction == TypeAction.COOPERER) ? TypeAction.TRAHIR : TypeAction.COOPERER;
        }
        return lastAction;
    }
}
