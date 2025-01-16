package fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.strats.g5_2.enums.Decision;
import java.util.EnumMap;
import java.util.Map;

public class StrategieDecisionAdaptateurGr2E5 {
    private static final Map<Decision, TYPE_ACTION> correspondance = new EnumMap<>(Decision.class);
    private static final Map<TYPE_ACTION, Decision> correspondanceDecision = new EnumMap<>(TYPE_ACTION.class);

    static {
        correspondance.put(Decision.COOPERER, TYPE_ACTION.COOPERER);
        correspondance.put(Decision.TRAHIR, TYPE_ACTION.TRAHIR);

        correspondance.forEach((decision, typeAction) -> correspondanceDecision.put(typeAction, decision));
    }

    private StrategieDecisionAdaptateurGr2E5(){}


    public static TYPE_ACTION adapter(Decision externeEnum) {
        TYPE_ACTION action = correspondance.get(externeEnum);
        if (action == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + externeEnum);
        }
        return action;
    }

    public static Decision adapter(TYPE_ACTION externeEnum) {
        Decision decision = correspondanceDecision.get(externeEnum);
        if (decision == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + externeEnum);
        }
        return decision;
    }
}