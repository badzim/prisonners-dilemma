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

class SondeurNaifStrategieTest {

    private SondeurNaifStrategie strategie;
    private SecureRandom mockRandom;

    @BeforeEach
    void setUp() {
        // Créer un mock de SecureRandom
        mockRandom = mock(SecureRandom.class);
        strategie = new SondeurNaifStrategie(mockRandom);
    }

   @Test
    void testImitateLastActionWithChanceToBetray() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER); // Action précédente

        // Mock la probabilité de trahir (ici 0.)
        when(mockRandom.nextDouble()).thenReturn(0.9); // 100% de chance de trahir (moins que 0.2, donc on imite l'action)

        TYPE_ACTION result = strategie.getAction(actions, 0);  // On vérifie le comportement pour le tour 0

        // Vérifie que la stratégie imite l'action précédente de l'adversaire (COOPERER ici)
        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit imiter l'action précédente de l'adversaire.");
    }

    @Test
    void testBetrayWithHigherProbability() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER); // Action précédente

        // Mock la probabilité de trahir (ici 0.2)
        when(mockRandom.nextDouble()).thenReturn(0.1);

        TYPE_ACTION result = strategie.getAction(actions, 0);  // On vérifie le comportement pour le tour 0

        // Vérifie que la stratégie trahit avec une probabilité plus élevée
        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit trahir avec une probabilité supérieure à 0.2.");
    }

    @Test
    void testImitateActionWhenOpponentBetrays() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.TRAHIR); // Action précédente

        // Mock la probabilité de trahir (ici 0.2)
        when(mockRandom.nextDouble()).thenReturn(0.9);

        TYPE_ACTION result = strategie.getAction(actions, 0);  // On vérifie le comportement pour le tour 0

        // Vérifie que la stratégie imite l'action précédente de l'adversaire (TRAHIR ici)
        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit imiter l'action précédente de l'adversaire.");
    }

    @Test
    void testCooperateIfNoPreviousAction() {
        List<TYPE_ACTION> actions = new ArrayList<>();

        // Aucune action précédente, donc la stratégie coopère par défaut
        TYPE_ACTION result = strategie.getAction(actions, 0);

        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer si aucune action précédente.");
    }
}
