package fr.uga.miage.m1.my_project.core.domain.adaptater.group2_10;

import fr.uga.m1miage.pc.strategy.Strategy;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.strategie.Strategie;
import java.util.ArrayList;
import java.util.List;

public class StrategieAdaptateurGr2E10 extends Strategie {

    Strategy strategyExtern;
    List<TYPE_ACTION> actionsRobot = new ArrayList<>();

    private StrategieAdaptateurGr2E10() {}

    public StrategieAdaptateurGr2E10(Strategy strategyExtern, List<TYPE_ACTION> actionsRobot) {
        this.strategyExtern = strategyExtern;
        this.actionsRobot = actionsRobot;
    }


    @Override
    public TYPE_ACTION getAction(List<TYPE_ACTION> actions, int lastResult) {
        TYPE_ACTION result = getAction(StrategieTypeActionAdaptateurGr2E10.adapter(actionsRobot), StrategieTypeActionAdaptateurGr2E10.adapter(actions));
        actionsRobot.add(result);
        return result;
    }

    public TYPE_ACTION getAction(List<String> myPreviousMoves, List<String> opponentPreviousMoves) {
        String stringTypeAction = strategyExtern.playNextMove(myPreviousMoves, opponentPreviousMoves);
        return StrategieTypeActionAdaptateurGr2E10.adapter(stringTypeAction);
    }
}
