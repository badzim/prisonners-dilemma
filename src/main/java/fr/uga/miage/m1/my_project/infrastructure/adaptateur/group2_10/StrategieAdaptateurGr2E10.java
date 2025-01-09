package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_10;

import fr.uga.m1miage.pc.strategy.Strategy;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.core.domain.model.strategie.Strategie;
import java.util.ArrayList;
import java.util.List;

public class StrategieAdaptateurGr2E10 extends Strategie {

    Strategy strategyExtern;
    List<TypeAction> actionsRobot = new ArrayList<>();

    private StrategieAdaptateurGr2E10() {}

    public StrategieAdaptateurGr2E10(Strategy strategyExtern, List<TypeAction> actionsRobot) {
        this.strategyExtern = strategyExtern;
        this.actionsRobot = actionsRobot;
    }


    @Override
    public TypeAction getAction(List<TypeAction> actions, int lastResult) {
        TypeAction result = getAction(StrategieTypeActionAdaptateurGr2E10.adapter(actionsRobot), StrategieTypeActionAdaptateurGr2E10.adapter(actions));
        actionsRobot.add(result);
        return result;
    }

    public TypeAction getAction(List<String> myPreviousMoves, List<String> opponentPreviousMoves) {
        String stringTypeAction = strategyExtern.playNextMove(myPreviousMoves, opponentPreviousMoves);
        return StrategieTypeActionAdaptateurGr2E10.adapter(stringTypeAction);
    }
}
