package fr.uga.miage.m1.my_project;

import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;
import fr.uga.miage.m1.my_project.server.models.strategies.RancunierDouxStrategie;
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
        List<TypeAction> actions = new ArrayList<>();
        assertEquals(TypeAction.COOPERER, strategie.getAction(actions, 0), "La stratégie doit commencer en coopérant.");
    }

    @Test
    void testPunishmentSequenceAfterBetrayal() {
        List<TypeAction> actions = new ArrayList<>();

        // Initial cooperation
        assertEquals(TypeAction.COOPERER, strategie.getAction(actions, 0));

        // L'adversaire trahit au tour suivant
        actions.add(TypeAction.TRAHIR);

        // Début de la séquence punitive : 5x TRAHIR
        for (int i = 0; i < 5; i++) {
            assertEquals(TypeAction.TRAHIR, strategie.getAction(actions, 0), "La stratégie doit trahir pour la punition.");
        }

        // Les 2 coups de coopération après les trahisons
        for (int i = 0; i < 2; i++) {
            assertEquals(TypeAction.COOPERER, strategie.getAction(actions, 0), "La stratégie doit coopérer après la punition.");
        }

        assertEquals(TypeAction.TRAHIR, strategie.getAction(actions, 0), "La stratégie doit revenir à la coopération.");
    }
}
