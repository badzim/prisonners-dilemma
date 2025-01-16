package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DonnantDonnantSoupconneuxStrategieTest {

    private DonnantDonnantSoupconneuxStrategie strategie;

    @BeforeEach
    void setUp() {
        strategie = new DonnantDonnantSoupconneuxStrategie();
    }

    @Test
    void testFirstMoveTrahir() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        assertEquals(TYPE_ACTION.TRAHIR, strategie.getAction(actions, 0), "Le premier coup doit être TRAHIR.");
    }

    @Test
    void testImitateAfterFirstMove() {
        List<TYPE_ACTION> actions = new ArrayList<>();

        // Premier coup
        strategie.getAction(actions, 0);

        // L'adversaire coopère au tour suivant
        actions.add(TYPE_ACTION.COOPERER);
        assertEquals(TYPE_ACTION.COOPERER, strategie.getAction(actions, 0), "La stratégie devrait imiter et coopérer.");

        // L'adversaire trahit au tour suivant
        actions.add(TYPE_ACTION.TRAHIR);
        assertEquals(TYPE_ACTION.TRAHIR, strategie.getAction(actions, 0), "La stratégie devrait imiter et trahir.");
    }
}
