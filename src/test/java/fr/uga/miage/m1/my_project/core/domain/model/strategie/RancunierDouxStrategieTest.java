package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RancunierDouxStrategieTest {

    private RancunierDouxStrategie strategie;

    @BeforeEach
    void setUp() {
        strategie = new RancunierDouxStrategie();
    }

    @Test
    void testCooperateInitially() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        assertEquals(TYPE_ACTION.COOPERER, strategie.getAction(actions, 0), "La stratégie doit commencer en coopérant.");
    }

    @Test
    void testPunishmentSequenceAfterBetrayal() {
        List<TYPE_ACTION> actions = new ArrayList<>();

        // Initial cooperation
        assertEquals(TYPE_ACTION.COOPERER, strategie.getAction(actions, 0));

        // L'adversaire trahit au tour suivant
        actions.add(TYPE_ACTION.TRAHIR);

        // Début de la séquence punitive : 5x TRAHIR
        for (int i = 0; i < 5; i++) {
            assertEquals(TYPE_ACTION.TRAHIR, strategie.getAction(actions, 0), "La stratégie doit trahir pour la punition.");
        }

        // Les 2 coups de coopération après les trahisons
        for (int i = 0; i < 2; i++) {
            assertEquals(TYPE_ACTION.COOPERER, strategie.getAction(actions, 0), "La stratégie doit coopérer après la punition.");
        }

        assertEquals(TYPE_ACTION.TRAHIR, strategie.getAction(actions, 0), "La stratégie doit revenir à la coopération.");
    }
}
