package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import fr.uga.strats.g5_2.enums.Decision;
import java.util.EnumMap;
import java.util.Map;

public class StrategieDecisionAdaptateurGr2E5 {
    private static final Map<Decision, TypeAction> correspondance = new EnumMap<>(Decision.class);
    private static final Map<TypeAction, Decision> correspondanceDecision = new EnumMap<>(TypeAction.class);

    static {
        correspondance.put(Decision.COOPERER, TypeAction.COOPERER);
        correspondance.put(Decision.TRAHIR, TypeAction.TRAHIR);

        correspondance.forEach((decision, typeAction) -> correspondanceDecision.put(typeAction, decision));
    }

    private StrategieDecisionAdaptateurGr2E5(){}


    public static TypeAction adapter(Decision externeEnum) {
        TypeAction action = correspondance.get(externeEnum);
        if (action == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + externeEnum);
        }
        return action;
    }

    public static Decision adapter(TypeAction externeEnum) {
        Decision decision = correspondanceDecision.get(externeEnum);
        if (decision == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + externeEnum);
        }
        return decision;
    }
}