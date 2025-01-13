package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;

import java.util.List;
// import java.util.Random; // attacker can guess random from this bib
import java.security.SecureRandom; // more secured random...

// Jouer comme le dernier coup de l'adversaire, mais jouer parfois un coup au hasard
public class DonnantDonnantAleatoireStrategie extends Strategie{
    // Méthode pour obtenir une action aléatoire (coopérer ou trahir)

    public DonnantDonnantAleatoireStrategie(SecureRandom random) {
        super(random);
    }

    private TYPE_ACTION getRandomAction() {
        return this.getRandom().nextBoolean() ? TYPE_ACTION.COOPERER : TYPE_ACTION.TRAHIR;
    }

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        //décider de la stratégie à utiliser
        boolean useRandomAction = this.getRandom().nextBoolean(); // Renvoie true ou false de façon aléatoire

        if (useRandomAction) {
            return getRandomAction();
        } else {
            if (actions.isEmpty()) return TYPE_ACTION.COOPERER;
            return getLastAction(actions); // Dernier élément de la liste
        }
    }




}
