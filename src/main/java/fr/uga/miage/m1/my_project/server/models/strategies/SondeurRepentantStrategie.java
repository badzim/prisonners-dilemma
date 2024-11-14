// Implementation de la stratégie
package fr.uga.miage.m1.my_project.server.models.strategies;
import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;

import java.security.SecureRandom;
import java.util.List;

public class SondeurRepentantStrategie extends Strategie {
    private boolean enTest = false;

    public SondeurRepentantStrategie(SecureRandom random) {
        super(random);
    }

    @Override
    public TypeAction getAction(List<TypeAction> actionsAdversaire, int dernierResultat) {
        // Si aucune action précédente, coopère par défaut
        if (actionsAdversaire.isEmpty()) {
            return TypeAction.COOPERER;
        }

        // Vérifier si nous sommes en phase de test
        if (enTest) {
            enTest = false; // Réinitialiser l'état de test
            // Si l'adversaire a trahi en réponse à notre test, coopérer (repentance)
            if (actionsAdversaire.get(actionsAdversaire.size() - 1) == TypeAction.TRAHIR) {
                return TypeAction.COOPERER; // Repentance
            }
        }

        // Si l'adversaire a coopéré au dernier tour
        if (actionsAdversaire.get(actionsAdversaire.size() - 1) == TypeAction.COOPERER) {
            // Décider aléatoirement de trahir pour tester
            if (getRandom().nextInt(10) == 0) {
                enTest = true; // Nous entrons en phase de test
                return TypeAction.TRAHIR; // Test de trahison
            } else {
                return TypeAction.COOPERER;
            }
        } else {
            // Si l'adversaire a trahi, trahit aussi
            return TypeAction.TRAHIR;
        }
    }

}
