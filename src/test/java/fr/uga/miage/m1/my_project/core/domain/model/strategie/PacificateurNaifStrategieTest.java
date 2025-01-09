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

class PacificateurNaifStrategieTest {

    private PacificateurNaifStrategie strategie;
    private SecureRandom mockRandom;

    @BeforeEach
    void setUp() {
        // Créer un mock de SecureRandom
        mockRandom = mock(SecureRandom.class);
        strategie = new PacificateurNaifStrategie(mockRandom);
    }

    @Test
    void testImitateLastActionWithChanceToCooperate() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER); // Action précédente

        // Mock la probabilité de coopérer (ici 0.2)
        when(mockRandom.nextDouble()).thenReturn(0.1); // 10% de chance de coopérer (moins que 0.2, donc on imite l'action)

        TypeAction result = strategie.getAction(actions, 0);  // On vérifie le comportement pour le tour 0

        // Vérifie que la stratégie imite l'action précédente de l'adversaire (COOPERER ici)
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit imiter l'action précédente de l'adversaire.");
    }

    @Test
    void testCooperateEvenWhenOpponentBetrays() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.TRAHIR); // Action précédente

        // Mock la probabilité de coopérer (ici 0.2)
        when(mockRandom.nextDouble()).thenReturn(0.1); // 10% de chance de coopérer (moins que 0.2, donc on coopère)

        TypeAction result = strategie.getAction(actions, 0);  // On vérifie le comportement pour le tour 0

        // Vérifie que la stratégie coopère malgré la trahison de l'adversaire (avec la probabilité)
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer même si l'adversaire a trahi.");
    }

    @Test
    void testBetrayWithHigherProbability() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.TRAHIR); // Action précédente

        // Mock la probabilité de coopérer (ici 0.2)
        when(mockRandom.nextDouble()).thenReturn(0.3); // 30% de chance de coopérer (plus que 0.2, donc on trahit)

        TypeAction result = strategie.getAction(actions, 0);  // On vérifie le comportement pour le tour 0

        // Vérifie que la stratégie trahit en raison de la probabilité
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit trahir si la probabilité de coopérer est inférieure à 0.2.");
    }

    @Test
    void testCooperateIfNoPreviousAction() {
        List<TypeAction> actions = new ArrayList<>();

        // Aucune action précédente, donc la stratégie coopère par défaut
        TypeAction result = strategie.getAction(actions, 0);

        assertEquals(TypeAction.COOPERER, result, "La stratégie doit coopérer si aucune action précédente.");
    }
}
