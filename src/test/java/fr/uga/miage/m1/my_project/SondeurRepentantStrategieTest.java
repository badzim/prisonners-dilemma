package fr.uga.miage.m1.my_project;

import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;
import fr.uga.miage.m1.my_project.server.models.strategies.SondeurRepentantStrategie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        List<TypeAction> actionsAdversaire = new ArrayList<>();

        TypeAction result = strategie.getAction(actionsAdversaire, 0);

        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer par défaut si aucune action précédente.");
    }

    @Test
    void testCooperateWhenOpponentCooperatedAndNoTest() {
        // Simuler que le coup aléatoire ne se produit pas
        when(mockRandom.nextInt(10)).thenReturn(1);

        List<TypeAction> actionsAdversaire = new ArrayList<>();
        actionsAdversaire.add(TypeAction.COOPERER);

        TypeAction result = strategie.getAction(actionsAdversaire, 0);

        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer si l'adversaire a coopéré et que le test ne se produit pas.");
    }

    @Test
    void testRandomBetrayalOccurs() {
        // Simuler que le coup aléatoire se produit
        when(mockRandom.nextInt(10)).thenReturn(0);

        List<TypeAction> actionsAdversaire = new ArrayList<>();
        actionsAdversaire.add(TypeAction.COOPERER);

        TypeAction result = strategie.getAction(actionsAdversaire, 0);

        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit trahir lors du test aléatoire.");
    }

    @Test
    void testRepentanceAfterOpponentBetrayal() {
        // Simuler que le test de trahison s'est produit précédemment
        when(mockRandom.nextInt(10)).thenReturn(0); // Premier appel : test de trahison
        List<TypeAction> actionsAdversaire = new ArrayList<>();
        actionsAdversaire.add(TypeAction.COOPERER);

        // Premier appel : nous trahissons pour tester
        TypeAction result1 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.TRAHIR, result1, "La stratégie doit trahir pour tester.");

        // Simuler que l'adversaire a trahi en réponse
        actionsAdversaire.add(TypeAction.TRAHIR);

        // Deuxième appel : nous devons coopérer par repentance
        TypeAction result2 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, result2, "La stratégie doit coopérer par repentance après que l'adversaire ait trahi en réponse.");
    }

    @Test
    void testContinueCooperatingAfterOpponentCooperatesPostTest() {
        // Simuler que le test de trahison s'est produit précédemment
        when(mockRandom.nextInt(10)).thenReturn(0); // Premier appel : test de trahison
        List<TypeAction> actionsAdversaire = new ArrayList<>();
        actionsAdversaire.add(TypeAction.COOPERER);

        // Premier appel : nous trahissons pour tester
        TypeAction result1 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.TRAHIR, result1, "La stratégie doit trahir pour tester.");

        // Simuler que l'adversaire a coopéré en réponse
        actionsAdversaire.add(TypeAction.COOPERER);

        // Deuxième appel : nous continuons normalement (coopération)
        when(mockRandom.nextInt(10)).thenReturn(1); // Le test ne se produit pas
        TypeAction result2 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, result2, "La stratégie doit coopérer si l'adversaire a coopéré après notre test.");
    }

    @Test
    void testBetrayIfOpponentBetrayed() {
        List<TypeAction> actionsAdversaire = new ArrayList<>();
        actionsAdversaire.add(TypeAction.TRAHIR);

        TypeAction result = strategie.getAction(actionsAdversaire, 0);

        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit trahir si l'adversaire a trahi au dernier coup.");
    }

    @Test
    void testSequenceOfActions() {
        List<TypeAction> actionsAdversaire = new ArrayList<>();

        // Tour 1 : Aucun historique, doit coopérer
        TypeAction result1 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, result1, "Tour 1 : doit coopérer par défaut.");

        // L'adversaire coopère
        actionsAdversaire.add(TypeAction.COOPERER);

        // Tour 2 : Simuler que le test ne se produit pas
        when(mockRandom.nextInt(10)).thenReturn(1);
        TypeAction result2 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, result2, "Tour 2 : doit coopérer car l'adversaire a coopéré et le test ne se produit pas.");

        // L'adversaire coopère
        actionsAdversaire.add(TypeAction.COOPERER);

        // Tour 3 : Simuler que le test se produit
        when(mockRandom.nextInt(10)).thenReturn(0);
        TypeAction result3 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.TRAHIR, result3, "Tour 3 : doit trahir pour tester.");

        // L'adversaire trahit en réponse
        actionsAdversaire.add(TypeAction.TRAHIR);

        // Tour 4 : Doit coopérer par repentance
        TypeAction result4 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, result4, "Tour 4 : doit coopérer par repentance.");

        // L'adversaire coopère
        actionsAdversaire.add(TypeAction.COOPERER);

        // Tour 5 : Simuler que le test ne se produit pas
        when(mockRandom.nextInt(10)).thenReturn(1);
        TypeAction result5 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, result5, "Tour 5 : doit coopérer car l'adversaire a coopéré et le test ne se produit pas.");
    }
}
