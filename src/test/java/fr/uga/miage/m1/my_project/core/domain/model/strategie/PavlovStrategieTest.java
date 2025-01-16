package fr.uga.miage.m1.my_project.core.domain.model.strategie;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PavlovStrategieTest {

    private PavlovStrategie strategie;

    @BeforeEach
    void setUp() {
        strategie = new PavlovStrategie();
    }

    @Test
    void testDefaultCooperateWithEmptyList() {
        List<TYPE_ACTION> actions = new ArrayList<>();

        // Vérifie que la stratégie coopère avec une liste vide
        TYPE_ACTION result = strategie.getAction(actions, 0);
        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer lorsque la liste est vide.");
    }

    @Test
    void testReturnLastActionWhenLastResultIsFive() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER); // Dernière action de l'adversaire

        // Vérifie que la stratégie retourne la dernière action car dernier résultat est 5
        TYPE_ACTION result = strategie.getAction(actions, 5);
        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit retourner la dernière action (COOPERER) lorsque le dernier résultat est 5.");
    }

    @Test
    void testReturnLastActionWhenLastResultIsThree() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.TRAHIR); // Dernière action de l'adversaire

        // Vérifie que la stratégie retourne la dernière action car dernier résultat est 3
        TYPE_ACTION result = strategie.getAction(actions, 3);
        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit retourner la dernière action (TRAHIR) lorsque le dernier résultat est 3.");
    }

    @Test
    void testBetrayIfLastActionWasBetrayalAndLastResultIsNotThreeOrFive() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.TRAHIR); // Dernière action de l'adversaire

        // Vérifie que la stratégie trahit car la dernière action de l'adversaire était une trahison et le dernier résultat n'est pas 3 ou 5
        TYPE_ACTION result = strategie.getAction(actions, 1);
        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit trahir lorsque la dernière action de l'adversaire était TRAHIR et le dernier résultat n'est pas 3 ou 5.");
    }

    @Test
    void testCooperateIfLastActionWasCooperationAndLastResultIsNotThreeOrFive() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER); // Dernière action de l'adversaire

        // Vérifie que la stratégie coopère car la dernière action de l'adversaire était une coopération et le dernier résultat n'est pas 3 ou 5
        TYPE_ACTION result = strategie.getAction(actions, 1);
        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer lorsque la dernière action de l'adversaire était COOPERER et le dernier résultat n'est pas 3 ou 5.");
    }
}

