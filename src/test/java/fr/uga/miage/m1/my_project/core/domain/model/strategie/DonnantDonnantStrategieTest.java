package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DonnantDonnantStrategieTest {

    private DonnantDonnantStrategie strategie;

    @BeforeEach
    void setUp() {
        strategie = new DonnantDonnantStrategie();
    }

    @Test
    void testCooperateWhenNoPreviousActions() {
        // Liste vide
        List<TYPE_ACTION> actions = new ArrayList<>();
        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie coopère par défaut
        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer par défaut si aucune action précédente.");
    }

    @Test
    void testFollowLastAction() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER);
        actions.add(TYPE_ACTION.TRAHIR);

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie imite la dernière action (trahir ici)
        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit suivre la dernière action de l'adversaire.");
    }

    @Test
    void testFollowLastActionAfterMultipleRounds() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER);
        actions.add(TYPE_ACTION.COOPERER);
        actions.add(TYPE_ACTION.TRAHIR);
        actions.add(TYPE_ACTION.COOPERER);

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie imite la dernière action (coopérer ici)
        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit suivre la dernière action de l'adversaire.");
    }
}
