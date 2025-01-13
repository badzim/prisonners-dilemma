package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import lombok.Data;

import java.util.List;

@Data
public class AdaptatifStrategie extends Strategie {

    private int coupCount = 0; // Compteur pour suivre le nombre de tours
    private double scoreC = 0; // Score total pour 'COOPERER'
    private double scoreT = 0; // Score total pour 'TRAHIR'
    private int countC = 0; // Nombre de fois où 'COOPERER' a été choisi
    private int countT = 0; // Nombre de fois où 'TRAHIR' a été choisi
    private final double[] sequenceInitiale = {
            0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 // La séquence initiale c, c, c, c, c, c, t, t, t, t, t
    };

    private TYPE_ACTION ancienneAction;

    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        // Si la séquence initiale n'est pas encore terminée, on suit la séquence
        if (!actions.isEmpty()) updateScores(dernierResultat, ancienneAction);
        if (coupCount < sequenceInitiale.length) {
            TYPE_ACTION action = sequenceInitiale[coupCount] == 0 ? TYPE_ACTION.COOPERER : TYPE_ACTION.TRAHIR;
            // garder la trace de l'ancienne action
            ancienneAction = action;
            coupCount++;
            return action;
        }

        // Après la séquence initiale, on choisit l'action ayant le meilleur score moyen
        double moyenneC = countC > 0 ? scoreC / countC : 0;
        double moyenneT = countT > 0 ? scoreT / countT : 0;

        // Choisir l'action avec le meilleur score moyen
        return moyenneC >= moyenneT ? TYPE_ACTION.COOPERER : TYPE_ACTION.TRAHIR;
    }

    private void updateScores(int dernierResultat, TYPE_ACTION action) {
        // Met à jour les scores pour COOPERER ou TRAHIR
        if (action == TYPE_ACTION.COOPERER) {
            scoreC += dernierResultat;
            countC++;
        } else {
            scoreT += dernierResultat;
            countT++;
        }
    }
}
