package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AleatoireStrategieTest {

    private AleatoireStrategie strategie;
    private SecureRandom mockRandom;

    @BeforeEach
    void setUp() {
        // Créer un mock de SecureRandom
        mockRandom = Mockito.mock(SecureRandom.class);
        strategie = new AleatoireStrategie(mockRandom);
    }

    @Test
    void testRandomAction() {
        // Configurer le mock pour retourner des valeurs aléatoires spécifiques
        when(mockRandom.nextBoolean()).thenReturn(true); // Simule le cas où l'action retournée est COOPERER

        List<TypeAction> actions = new ArrayList<>();

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que l'action est COOPERER (car nextBoolean() retourne true)
        assertSame(TypeAction.COOPERER, result, "La stratégie doit retourner COOPERER lorsque nextBoolean retourne true.");
    }

    @Test
    void testRandomAction2() {
        // Configurer le mock pour retourner des valeurs aléatoires spécifiques
        when(mockRandom.nextBoolean()).thenReturn(false); // Simule le cas où l'action retournée est TRAHIR

        List<TypeAction> actions = new ArrayList<>();

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que l'action est TRAHIR (car nextBoolean() retourne false)
        assertSame(TypeAction.TRAHIR, result, "La stratégie doit retourner TRAHIR lorsque nextBoolean retourne false.");
    }

    @Test
    void testRandomActionWithPreviousActions() {
        // Configurer le mock pour retourner une action spécifique
        when(mockRandom.nextBoolean()).thenReturn(true); // Simule COOPERER

        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER); // Action précédente, ne sera pas utilisée ici

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que l'action est toujours COOPERER ou TRAHIR selon l'aléatoire
        assertTrue(result == TypeAction.COOPERER || result == TypeAction.TRAHIR,
                "La stratégie doit toujours choisir aléatoirement entre COOPERER et TRAHIR.");
    }
}
