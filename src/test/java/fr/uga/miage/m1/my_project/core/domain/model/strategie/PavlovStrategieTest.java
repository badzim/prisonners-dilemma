package fr.uga.miage.m1.my_project.core.domain.model.strategie;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
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
        List<TypeAction> actions = new ArrayList<>();

        // Vérifie que la stratégie coopère avec une liste vide
        TypeAction result = strategie.getAction(actions, 0);
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer lorsque la liste est vide.");
    }

    @Test
    void testReturnLastActionWhenLastResultIsFive() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER); // Dernière action de l'adversaire

        // Vérifie que la stratégie retourne la dernière action car dernier résultat est 5
        TypeAction result = strategie.getAction(actions, 5);
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit retourner la dernière action (COOPERER) lorsque le dernier résultat est 5.");
    }

    @Test
    void testReturnLastActionWhenLastResultIsThree() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.TRAHIR); // Dernière action de l'adversaire

        // Vérifie que la stratégie retourne la dernière action car dernier résultat est 3
        TypeAction result = strategie.getAction(actions, 3);
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit retourner la dernière action (TRAHIR) lorsque le dernier résultat est 3.");
    }

    @Test
    void testBetrayIfLastActionWasBetrayalAndLastResultIsNotThreeOrFive() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.TRAHIR); // Dernière action de l'adversaire

        // Vérifie que la stratégie trahit car la dernière action de l'adversaire était une trahison et le dernier résultat n'est pas 3 ou 5
        TypeAction result = strategie.getAction(actions, 1);
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit trahir lorsque la dernière action de l'adversaire était TRAHIR et le dernier résultat n'est pas 3 ou 5.");
    }

    @Test
    void testCooperateIfLastActionWasCooperationAndLastResultIsNotThreeOrFive() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER); // Dernière action de l'adversaire

        // Vérifie que la stratégie coopère car la dernière action de l'adversaire était une coopération et le dernier résultat n'est pas 3 ou 5
        TypeAction result = strategie.getAction(actions, 1);
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer lorsque la dernière action de l'adversaire était COOPERER et le dernier résultat n'est pas 3 ou 5.");
    }
}

