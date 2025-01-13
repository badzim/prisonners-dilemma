package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.util.List;
import java.security.SecureRandom;

public class PavlovAleatoireStrategie extends Strategie {
    private static final double RANDOM_PROBABILITY = 0.2;
    private TYPE_ACTION lastAction = TYPE_ACTION.COOPERER;

    public PavlovAleatoireStrategie(SecureRandom random) {
        super(random);
    }

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        if (getRandom().nextDouble() < RANDOM_PROBABILITY) {
            // Choisir aléatoirement une action
            lastAction = getRandom().nextBoolean() ? TYPE_ACTION.COOPERER : TYPE_ACTION.TRAHIR;
        } else if (dernierResultat == 5 || dernierResultat == 3) {
            // Répéter le dernier choix si 5 ou 3 points ont été obtenus
            // lastAction reste inchangé
        } else {
            // Inverser l'action si autre résultat
            lastAction = (lastAction == TYPE_ACTION.COOPERER) ? TYPE_ACTION.TRAHIR : TYPE_ACTION.COOPERER;
        }
        return lastAction;
    }
}
