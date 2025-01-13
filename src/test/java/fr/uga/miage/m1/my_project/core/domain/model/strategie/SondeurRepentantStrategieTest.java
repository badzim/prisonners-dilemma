package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SondeurRepentantStrategieTest {

    private SondeurRepentantStrategie strategie;
    private SecureRandom mockRandom;

    @BeforeEach
    void setUp() {
        // Créer un mock de SecureRandom
        mockRandom = mock(SecureRandom.class);
        strategie = new SondeurRepentantStrategie(mockRandom);
    }

    @Test
    void testCooperateWhenNoPreviousActions() {
        List<TYPE_ACTION> actionsAdversaire = new ArrayList<>();

        TYPE_ACTION result = strategie.getAction(actionsAdversaire, 0);

        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer par défaut si aucune action précédente.");
    }

    @Test
    void testCooperateWhenOpponentCooperatedAndNoTest() {
        // Simuler que le coup aléatoire ne se produit pas
        when(mockRandom.nextInt(10)).thenReturn(1);

        List<TYPE_ACTION> actionsAdversaire = new ArrayList<>();
        actionsAdversaire.add(TYPE_ACTION.COOPERER);

        TYPE_ACTION result = strategie.getAction(actionsAdversaire, 0);

        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer si l'adversaire a coopéré et que le test ne se produit pas.");
    }

    @Test
    void testRandomBetrayalOccurs() {
        // Simuler que le coup aléatoire se produit
        when(mockRandom.nextInt(10)).thenReturn(0);

        List<TYPE_ACTION> actionsAdversaire = new ArrayList<>();
        actionsAdversaire.add(TYPE_ACTION.COOPERER);

        TYPE_ACTION result = strategie.getAction(actionsAdversaire, 0);

        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit trahir lors du test aléatoire.");
    }

    @Test
    void testRepentanceAfterOpponentBetrayal() {
        // Simuler que le test de trahison s'est produit précédemment
        when(mockRandom.nextInt(10)).thenReturn(0); // Premier appel : test de trahison
        List<TYPE_ACTION> actionsAdversaire = new ArrayList<>();
        actionsAdversaire.add(TYPE_ACTION.COOPERER);

        // Premier appel : nous trahissons pour tester
        TYPE_ACTION result1 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.TRAHIR, result1, "La stratégie doit trahir pour tester.");

        // Simuler que l'adversaire a trahi en réponse
        actionsAdversaire.add(TYPE_ACTION.TRAHIR);

        // Deuxième appel : nous devons coopérer par repentance
        TYPE_ACTION result2 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, result2, "La stratégie doit coopérer par repentance après que l'adversaire ait trahi en réponse.");
    }

    @Test
    void testContinueCooperatingAfterOpponentCooperatesPostTest() {
        // Simuler que le test de trahison s'est produit précédemment
        when(mockRandom.nextInt(10)).thenReturn(0); // Premier appel : test de trahison
        List<TYPE_ACTION> actionsAdversaire = new ArrayList<>();
        actionsAdversaire.add(TYPE_ACTION.COOPERER);

        // Premier appel : nous trahissons pour tester
        TYPE_ACTION result1 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.TRAHIR, result1, "La stratégie doit trahir pour tester.");

        // Simuler que l'adversaire a coopéré en réponse
        actionsAdversaire.add(TYPE_ACTION.COOPERER);

        // Deuxième appel : nous continuons normalement (coopération)
        when(mockRandom.nextInt(10)).thenReturn(1); // Le test ne se produit pas
        TYPE_ACTION result2 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, result2, "La stratégie doit coopérer si l'adversaire a coopéré après notre test.");
    }

    @Test
    void testBetrayIfOpponentBetrayed() {
        List<TYPE_ACTION> actionsAdversaire = new ArrayList<>();
        actionsAdversaire.add(TYPE_ACTION.TRAHIR);

        TYPE_ACTION result = strategie.getAction(actionsAdversaire, 0);

        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit trahir si l'adversaire a trahi au dernier coup.");
    }

    @Test
    void testSequenceOfActions() {
        List<TYPE_ACTION> actionsAdversaire = new ArrayList<>();

        // Tour 1 : Aucun historique, doit coopérer
        TYPE_ACTION result1 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, result1, "Tour 1 : doit coopérer par défaut.");

        // L'adversaire coopère
        actionsAdversaire.add(TYPE_ACTION.COOPERER);

        // Tour 2 : Simuler que le test ne se produit pas
        when(mockRandom.nextInt(10)).thenReturn(1);
        TYPE_ACTION result2 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, result2, "Tour 2 : doit coopérer car l'adversaire a coopéré et le test ne se produit pas.");

        // L'adversaire coopère
        actionsAdversaire.add(TYPE_ACTION.COOPERER);

        // Tour 3 : Simuler que le test se produit
        when(mockRandom.nextInt(10)).thenReturn(0);
        TYPE_ACTION result3 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.TRAHIR, result3, "Tour 3 : doit trahir pour tester.");

        // L'adversaire trahit en réponse
        actionsAdversaire.add(TYPE_ACTION.TRAHIR);

        // Tour 4 : Doit coopérer par repentance
        TYPE_ACTION result4 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, result4, "Tour 4 : doit coopérer par repentance.");

        // L'adversaire coopère
        actionsAdversaire.add(TYPE_ACTION.COOPERER);

        // Tour 5 : Simuler que le test ne se produit pas
        when(mockRandom.nextInt(10)).thenReturn(1);
        TYPE_ACTION result5 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, result5, "Tour 5 : doit coopérer car l'adversaire a coopéré et le test ne se produit pas.");
    }
}
