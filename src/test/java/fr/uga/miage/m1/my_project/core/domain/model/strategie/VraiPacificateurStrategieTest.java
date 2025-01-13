package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
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
        List<TYPE_ACTION> actions = new ArrayList<>();

        // Aucune action précédente, donc coopère par défaut
        TYPE_ACTION result = strategie.getAction(actions, 0);

        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer si aucune action précédente.");
    }

    @Test
    void testCooperateIfOnePreviousAction() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER); // Action précédente

        // Imite l'action de l'adversaire (coopérer ici)
        TYPE_ACTION result = strategie.getAction(actions, 0);

        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit imiter l'action précédente de l'adversaire.");
    }

    @Test
    void testBetrayIfOpponentBetraysTwoTimes() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.TRAHIR); // Première trahison
        actions.add(TYPE_ACTION.TRAHIR); // Deuxième trahison

        // Mock la probabilité de coopérer après deux trahisons
        when(mockRandom.nextDouble()).thenReturn(0.3); // 30% de chance de coopérer, donc trahit

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie trahit après deux trahisons successives
        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit trahir après deux trahisons successives.");
    }

    @Test
    void testCooperateAfterTwoBetraysWithChance() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.TRAHIR); // Première trahison
        actions.add(TYPE_ACTION.TRAHIR); // Deuxième trahison

        // Mock la probabilité de coopérer après deux trahisons
        when(mockRandom.nextDouble()).thenReturn(0.1); // 10% de chance de coopérer, donc coopère

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie coopère après deux trahisons successives (avec probabilité)
        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer après deux trahisons avec une probabilité.");
    }

    @Test
    void testCooperateIfOnlyOneBetrayal() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.TRAHIR); // Action précédente

        // Imite l'action de l'adversaire (ici trahir)
        TYPE_ACTION result = strategie.getAction(actions, 0);

        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit imiter l'action précédente de l'adversaire.");
    }
}
