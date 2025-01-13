package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DonnantPourDeuxDonnantsStrategieTest {

    private DonnantPourDeuxDonnantsStrategie strategie;

    @BeforeEach
    void setUp() {
        strategie = new DonnantPourDeuxDonnantsStrategie();
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
    void testCooperateAfterTwoCooperations() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER);
        actions.add(TYPE_ACTION.COOPERER);

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie coopère si l'adversaire a coopéré deux fois de suite
        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer si l'adversaire a coopéré deux fois.");
    }

    @Test
    void testBetrayAfterBetrayal() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.TRAHIR);
        actions.add(TYPE_ACTION.COOPERER);

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie trahit si l'adversaire a trahi
        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit trahir si l'adversaire a trahi.");
    }

    @Test
    void testFollowActionAfterMixedActions() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER);
        actions.add(TYPE_ACTION.TRAHIR);
        actions.add(TYPE_ACTION.COOPERER);
        actions.add(TYPE_ACTION.TRAHIR);

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie suit l'adversaire correctement en fonction des dernières actions
        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit trahir si l'adversaire a trahi.");
    }
}
