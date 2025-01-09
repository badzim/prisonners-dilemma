package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.core.domain.model.strategie.Strategie;
import fr.uga.strats.g5_2.enums.Decision;
import fr.uga.strats.g5_2.models.Tour;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

public class StrategieAdaptateurGr2E5 extends Strategie {
    fr.uga.strats.g5_2.models.Strategie strategieExterne;
    @Getter
    private List<TypeAction> actionsRobot = new ArrayList<>();
    private boolean estInitiateur = false;


    private StrategieAdaptateurGr2E5(){}

    public StrategieAdaptateurGr2E5(fr.uga.strats.g5_2.models.Strategie strategie) {
        strategieExterne = strategie;
    }

    public StrategieAdaptateurGr2E5(fr.uga.strats.g5_2.models.Strategie strategie,List<TypeAction> actionRobots, boolean estInitiateur) {
        strategieExterne = strategie;
        this.actionsRobot = actionRobots;
        this.estInitiateur = estInitiateur;
    }

    public TypeAction getAction(List<TypeAction> actions, int dernierResultat) {
        Tour[] tours = StrategieTourAdapterGr2E5.construireTours(actionsRobot, actions);
        TypeAction resultat = getAction(tours, (estInitiateur) ? 1 : 2, (estInitiateur) ? 2 : 1);
        actionsRobot.add(resultat);
        return  resultat;
    }

    public TypeAction getAction(Tour [] actions, int idJoueur, int idJoueurAdversaire) {
        // Passer de decision à TypeAction aussi
        Decision decision = this.strategieExterne.deciderTour(actions, idJoueur, idJoueurAdversaire);
        return StrategieDecisionAdaptateurGr2E5.adapter(decision);
    }

}
