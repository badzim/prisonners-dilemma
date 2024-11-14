package fr.uga.miage.m1.my_project;

import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;
import fr.uga.miage.m1.my_project.server.models.strategies.PavlovAleatoireStrategie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PavlovAleatoireStrategieTest {

    private PavlovAleatoireStrategie strategie;
    private SecureRandom mockRandom;

    @BeforeEach
    void setUp() {
        // Créer un mock de SecureRandom
        mockRandom = mock(SecureRandom.class);
        strategie = new PavlovAleatoireStrategie(mockRandom);
    }

    @Test
    void testRepeatLastActionOnGoodScore() {
        // Si le dernier résultat est 5 ou 3, répéter le dernier choix
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER);

        when(mockRandom.nextDouble()).thenReturn(0.3);  // Probabilité pour éviter le choix aléatoire

        TypeAction result = strategie.getAction(actions, 5);  // Résultat précédent de 5 points
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit répéter COOPERER après un score de 5.");

        result = strategie.getAction(actions, 3);  // Résultat précédent de 3 points
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit répéter COOPERER après un score de 3.");
    }

    @Test
    void testAlternateOnBadScore() {
        // Si le dernier résultat est autre que 5 ou 3, alterner l'action
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER);

        when(mockRandom.nextDouble()).thenReturn(0.3);  // Probabilité pour éviter le choix aléatoire

        TypeAction result = strategie.getAction(actions, 1);  // Résultat précédent de 1 point
        assertEquals(TypeAction.TRAHIR, result, "La stratégie doit alterner après un mauvais score.");
    }

    @Test
    void testRandomChoice() {
        // Tester si un choix aléatoire est fait quand la probabilité est déclenchée
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER);

        when(mockRandom.nextDouble()).thenReturn(0.1);  // Probabilité pour déclencher le choix aléatoire
        when(mockRandom.nextBoolean()).thenReturn(true);  // Choisit COOPERER en cas de choix aléatoire

        TypeAction result = strategie.getAction(actions, 0);
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit choisir COOPERER de manière aléatoire.");
    }
}
