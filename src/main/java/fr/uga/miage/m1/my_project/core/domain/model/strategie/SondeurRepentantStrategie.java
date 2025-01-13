// Implementation de la stratégie
package fr.uga.miage.m1.my_project.core.domain.model.strategie;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;

import java.security.SecureRandom;
import java.util.List;

public class SondeurRepentantStrategie extends Strategie {
    private boolean enTest = false;

    public SondeurRepentantStrategie(SecureRandom random) {
        super(random);
    }

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actionsAdversaire, int dernierResultat) {
        // Si aucune action précédente, coopère par défaut
        if (actionsAdversaire.isEmpty()) {
            return TYPE_ACTION.COOPERER;
        }

        // Vérifier si nous sommes en phase de test
        if (enTest) {
            enTest = false; // Réinitialiser l'état de test
            // Si l'adversaire a trahi en réponse à notre test, coopérer (repentance)
            if (actionsAdversaire.get(actionsAdversaire.size() - 1) == TYPE_ACTION.TRAHIR) {
                return TYPE_ACTION.COOPERER; // Repentance
            }
        }

        // Si l'adversaire a coopéré au dernier tour
        if (actionsAdversaire.get(actionsAdversaire.size() - 1) == TYPE_ACTION.COOPERER) {
            // Décider aléatoirement de trahir pour tester
            if (getRandom().nextInt(10) == 0) {
                enTest = true; // Nous entrons en phase de test
                return TYPE_ACTION.TRAHIR; // Test de trahison
            } else {
                return TYPE_ACTION.COOPERER;
            }
        } else {
            // Si l'adversaire a trahi, trahit aussi
            return TYPE_ACTION.TRAHIR;
        }
    }

}
