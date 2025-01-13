package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import java.util.List;
import java.security.SecureRandom;

public class AleatoireStrategie extends Strategie {

    public AleatoireStrategie(SecureRandom random) {
        super(random);
    }

    private TYPE_ACTION getRandomAction() {
        return this.getRandom().nextBoolean() ? TYPE_ACTION.COOPERER : TYPE_ACTION.TRAHIR;
    }

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        // Choisir aléatoirement entre traîr ou coopérer
        return getRandomAction();
    }
}
