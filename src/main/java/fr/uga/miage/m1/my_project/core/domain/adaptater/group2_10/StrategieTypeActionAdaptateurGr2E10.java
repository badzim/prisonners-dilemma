package fr.uga.miage.m1.my_project.core.domain.adaptater.group2_10;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StrategieTypeActionAdaptateurGr2E10 {
    private static final Map<String, TYPE_ACTION> correspondance = new HashMap<>();
    private static final Map<TYPE_ACTION, String> correspondanceInverse = new EnumMap<>(TYPE_ACTION.class);

    static {
        correspondance.put("c", TYPE_ACTION.COOPERER);
        correspondance.put("t", TYPE_ACTION.TRAHIR);

        correspondance.forEach((s, typeAction) -> correspondanceInverse.put(typeAction, s));
    }

    private StrategieTypeActionAdaptateurGr2E10() {}

    public static TYPE_ACTION adapter(String externeTypeAction) {
        TYPE_ACTION action = correspondance.get(externeTypeAction);
        if (action == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + externeTypeAction);
        }
        return action;
    }

    public static String adapter(TYPE_ACTION typeAction) {
        String decision = correspondanceInverse.get(typeAction);
        if (decision == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + typeAction);
        }
        return decision;
    }

    public static List<String> adapter(List<TYPE_ACTION> typeActions) {
        return typeActions.stream().map(StrategieTypeActionAdaptateurGr2E10::adapter).toList();
    }

}
