package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.model.strategie.Strategie;
import fr.uga.strats.g5_2.enums.Decision;
import fr.uga.strats.g5_2.models.Tour;
import java.util.List;

public class StrategieAdaptateurGr2E5 extends Strategie {
    fr.uga.strats.g5_2.models.Strategie strategieExterne;
    List<Tour> tours;

    private StrategieAdaptateurGr2E5(){}

    public StrategieAdaptateurGr2E5(fr.uga.strats.g5_2.models.Strategie strategie) {
        strategieExterne = strategie;
    }

    public TypeAction getAction(List<TypeAction> actions, int dernierResultat) {
        return  TypeAction.COOPERER;
    }

    public TypeAction getAction(Tour [] actions, int idJoueur, int idJoueurAdversaire) {
        // Passer de decision à TypeAction aussi
        Decision decision = this.strategieExterne.deciderTour(actions, idJoueur, idJoueurAdversaire);
        return StrategieDecisionAdaptateurGr2E5.adapter(decision);
    }
}
