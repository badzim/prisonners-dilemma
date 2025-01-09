package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VraiPacificateurStrategieTest {

    private VraiPacificateurStrategie strategie;
    private SecureRandom mockRandom;

    @BeforeEach
    void setUp() {
        // Créer un mock de SecureRandom
        mockRandom = mock(SecureRandom.class);
        strategie = new VraiPacificateurStrategie(mockRandom);
    }

    @Test
    void testCooperateIfNoPreviousAction() {
        List<TypeAction> actions = new ArrayList<>();

        // Aucune action précédente, donc coopère par défaut
        TypeAction result = strategie.getAction(actions, 0);

        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer si aucune action précédente.");
    }

    @Test
    void testCooperateIfOnePreviousAction() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER); // Action précédente

        // Imite l'action de l'adversaire (coopérer ici)
        TypeAction result = strategie.getAction(actions, 0);

        assertEquals(TypeAction.COOPERER, result, "La stratégie doit imiter l'action précédente de l'adversaire.");
    }

    @Test
    void testBetrayIfOpponentBetraysTwoTimes() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.TRAHIR); // Première trahison
        actions.add(TypeAction.TRAHIR); // Deuxième trahison

        // Mock la probabilité de coopérer après deux trahisons
        when(mockRandom.nextDouble()).thenReturn(0.3); // 30% de chance de coopérer, donc trahit

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie trahit après deux trahisons successives
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit trahir après deux trahisons successives.");
    }

    @Test
    void testCooperateAfterTwoBetraysWithChance() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.TRAHIR); // Première trahison
        actions.add(TypeAction.TRAHIR); // Deuxième trahison

        // Mock la probabilité de coopérer après deux trahisons
        when(mockRandom.nextDouble()).thenReturn(0.1); // 10% de chance de coopérer, donc coopère

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie coopère après deux trahisons successives (avec probabilité)
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer après deux trahisons avec une probabilité.");
    }

    @Test
    void testCooperateIfOnlyOneBetrayal() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.TRAHIR); // Action précédente

        // Imite l'action de l'adversaire (ici trahir)
        TypeAction result = strategie.getAction(actions, 0);

        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit imiter l'action précédente de l'adversaire.");
    }
}
