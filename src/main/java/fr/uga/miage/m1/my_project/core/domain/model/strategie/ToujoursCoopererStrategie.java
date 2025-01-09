package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;

import java.util.List;
// Toujours coopérer sans tenir en compte des réponses de l'adverse

public class ToujoursCoopererStrategie extends Strategie {
    @Override
    public TypeAction getAction(List<TypeAction> actions, int dernierResultat) {
        return TypeAction.COOPERER;
    }
}
