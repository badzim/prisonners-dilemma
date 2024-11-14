package fr.uga.miage.m1.my_project;

import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;
import fr.uga.miage.m1.my_project.server.models.strategies.SondeurNaifStrategie;
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
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER); // Action précédente

        // Mock la probabilité de trahir (ici 0.)
        when(mockRandom.nextDouble()).thenReturn(0.9); // 100% de chance de trahir (moins que 0.2, donc on imite l'action)

        TypeAction result = strategie.getAction(actions, 0);  // On vérifie le comportement pour le tour 0

        // Vérifie que la stratégie imite l'action précédente de l'adversaire (COOPERER ici)
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit imiter l'action précédente de l'adversaire.");
    }

    @Test
    void testBetrayWithHigherProbability() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER); // Action précédente

        // Mock la probabilité de trahir (ici 0.2)
        when(mockRandom.nextDouble()).thenReturn(0.1);

        TypeAction result = strategie.getAction(actions, 0);  // On vérifie le comportement pour le tour 0

        // Vérifie que la stratégie trahit avec une probabilité plus élevée
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit trahir avec une probabilité supérieure à 0.2.");
    }

    @Test
    void testImitateActionWhenOpponentBetrays() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.TRAHIR); // Action précédente

        // Mock la probabilité de trahir (ici 0.2)
        when(mockRandom.nextDouble()).thenReturn(0.9);

        TypeAction result = strategie.getAction(actions, 0);  // On vérifie le comportement pour le tour 0

        // Vérifie que la stratégie imite l'action précédente de l'adversaire (TRAHIR ici)
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit imiter l'action précédente de l'adversaire.");
    }

    @Test
    void testCooperateIfNoPreviousAction() {
        List<TypeAction> actions = new ArrayList<>();

        // Aucune action précédente, donc la stratégie coopère par défaut
        TypeAction result = strategie.getAction(actions, 0);

        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer si aucune action précédente.");
    }
}
