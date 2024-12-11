package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_10;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategieTypeActionAdaptateurGr2E10 {
    private static final Map<String, TypeAction> correspondance = new HashMap<>();
    private static final Map<TypeAction, String> correspondanceInverse = new EnumMap<>(TypeAction.class);

    static {
        correspondance.put("c", TypeAction.COOPERER);
        correspondance.put("t", TypeAction.TRAHIR);

        correspondance.forEach((s, typeAction) -> correspondanceInverse.put(typeAction, s));
    }

    private StrategieTypeActionAdaptateurGr2E10() {}

    public static TypeAction adapter(String externeTypeAction) {
        TypeAction action = correspondance.get(externeTypeAction);
        if (action == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + externeTypeAction);
        }
        return action;
    }

    public static String adapter(TypeAction typeAction) {
        String decision = correspondanceInverse.get(typeAction);
        if (decision == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + typeAction);
        }
        return decision;
    }

    public static List<String> adapter(List<TypeAction> typeActions) {
        return typeActions.stream().map(StrategieTypeActionAdaptateurGr2E10::adapter).toList();
    }

}
