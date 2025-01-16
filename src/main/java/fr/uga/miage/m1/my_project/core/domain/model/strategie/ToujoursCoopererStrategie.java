package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;

import java.util.List;
// Toujours coopérer sans tenir en compte des réponses de l'adverse

public class ToujoursCoopererStrategie extends Strategie {
    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        return TYPE_ACTION.COOPERER;
    }
}
