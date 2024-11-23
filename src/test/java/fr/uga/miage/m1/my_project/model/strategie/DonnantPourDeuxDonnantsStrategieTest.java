package fr.uga.miage.m1.my_project.model.strategie;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
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
        List<TypeAction> actions = new ArrayList<>();
        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie coopère par défaut
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer par défaut si aucune action précédente.");
    }

    @Test
    void testCooperateAfterTwoCooperations() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER);
        actions.add(TypeAction.COOPERER);

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie coopère si l'adversaire a coopéré deux fois de suite
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer si l'adversaire a coopéré deux fois.");
    }

    @Test
    void testBetrayAfterBetrayal() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.TRAHIR);
        actions.add(TypeAction.COOPERER);

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie trahit si l'adversaire a trahi
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit trahir si l'adversaire a trahi.");
    }

    @Test
    void testFollowActionAfterMixedActions() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER);
        actions.add(TypeAction.TRAHIR);
        actions.add(TypeAction.COOPERER);
        actions.add(TypeAction.TRAHIR);

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie suit l'adversaire correctement en fonction des dernières actions
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit trahir si l'adversaire a trahi.");
    }
}
