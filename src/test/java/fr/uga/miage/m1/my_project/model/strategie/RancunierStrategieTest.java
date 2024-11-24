package fr.uga.miage.m1.my_project.model.strategie;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RancunierStrategieTest {

    private RancunierStrategie strategie;

    @BeforeEach
    void setUp() {
        strategie = new RancunierStrategie();
    }

    @Test
    void testCooperateUntilBetrayed() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER); // Premier coup de l'adversaire

        // On s'attend à coopérer car l'adversaire n'a pas trahi
        TypeAction result = strategie.getAction(actions, 0);
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer tant que l'adversaire n'a pas trahi.");

        // Ajouter un coup de trahison de l'adversaire
        actions.add(TypeAction.TRAHIR);

        // Maintenant, la stratégie doit trahir
        result = strategie.getAction(actions, 1);
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit trahir après que l'adversaire ait trahi.");
    }

    @Test
    void testAlwaysCooperateWithEmptyList() {
        List<TypeAction> actions = new ArrayList<>();

        // Avec une liste vide, on s'attend à coopérer
        TypeAction result = strategie.getAction(actions, 0);
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer lorsque la liste est vide.");
    }

    @Test
    void testBetrayAfterPreviousBetrayal() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.TRAHIR); // L'adversaire a trahi précédemment

        // On s'attend à trahir car l'adversaire a déjà trahi
        TypeAction result = strategie.getAction(actions, 0);
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit trahir après que l'adversaire ait trahi.");
    }
}

