package fr.uga.miage.m1.my_project.model.strategie;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
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
        List<TypeAction> actions = new ArrayList<>();
        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie coopère par défaut
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer par défaut si aucune action précédente.");
    }

    @Test
    void testFollowLastAction() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER);
        actions.add(TypeAction.TRAHIR);

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie imite la dernière action (trahir ici)
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit suivre la dernière action de l'adversaire.");
    }

    @Test
    void testFollowLastActionAfterMultipleRounds() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER);
        actions.add(TypeAction.COOPERER);
        actions.add(TypeAction.TRAHIR);
        actions.add(TypeAction.COOPERER);

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie imite la dernière action (coopérer ici)
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit suivre la dernière action de l'adversaire.");
    }
}
