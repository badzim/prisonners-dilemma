package fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.strategie.Strategie;
import fr.uga.strats.g5_2.enums.Decision;
import fr.uga.strats.g5_2.models.Tour;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

public class StrategieAdaptateurGr2E5 extends Strategie {
    fr.uga.strats.g5_2.models.Strategie strategieExterne;
    @Getter
    private List<TYPE_ACTION> actionsRobot = new ArrayList<>();
    private boolean estInitiateur = false;


    private StrategieAdaptateurGr2E5(){}

    public StrategieAdaptateurGr2E5(fr.uga.strats.g5_2.models.Strategie strategie) {
        strategieExterne = strategie;
    }

    public StrategieAdaptateurGr2E5(fr.uga.strats.g5_2.models.Strategie strategie, List<TYPE_ACTION> actionRobots, boolean estInitiateur) {
        strategieExterne = strategie;
        this.actionsRobot = actionRobots;
        this.estInitiateur = estInitiateur;
    }

    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat) {
        Tour[] tours = StrategieTourAdapterGr2E5.construireTours(actionsRobot, actions);
        TYPE_ACTION resultat = getAction(tours, (estInitiateur) ? 1 : 2, (estInitiateur) ? 2 : 1);
        actionsRobot.add(resultat);
        return  resultat;
    }

    public TYPE_ACTION getAction(Tour [] actions, int idJoueur, int idJoueurAdversaire) {
        // Passer de decision à TypeAction aussi
        Decision decision = this.strategieExterne.deciderTour(actions, idJoueur, idJoueurAdversaire);
        return StrategieDecisionAdaptateurGr2E5.adapter(decision);
    }

}
