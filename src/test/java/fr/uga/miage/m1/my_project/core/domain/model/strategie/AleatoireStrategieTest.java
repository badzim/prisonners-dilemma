package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
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

        List<TYPE_ACTION> actions = new ArrayList<>();

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que l'action est COOPERER (car nextBoolean() retourne true)
        assertSame(TYPE_ACTION.COOPERER, result, "La stratégie doit retourner COOPERER lorsque nextBoolean retourne true.");
    }

    @Test
    void testRandomAction2() {
        // Configurer le mock pour retourner des valeurs aléatoires spécifiques
        when(mockRandom.nextBoolean()).thenReturn(false); // Simule le cas où l'action retournée est TRAHIR

        List<TYPE_ACTION> actions = new ArrayList<>();

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que l'action est TRAHIR (car nextBoolean() retourne false)
        assertSame(TYPE_ACTION.TRAHIR, result, "La stratégie doit retourner TRAHIR lorsque nextBoolean retourne false.");
    }

    @Test
    void testRandomActionWithPreviousActions() {
        // Configurer le mock pour retourner une action spécifique
        when(mockRandom.nextBoolean()).thenReturn(true); // Simule COOPERER

        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER); // Action précédente, ne sera pas utilisée ici

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que l'action est toujours COOPERER ou TRAHIR selon l'aléatoire
        assertTrue(result == TYPE_ACTION.COOPERER || result == TYPE_ACTION.TRAHIR,
                "La stratégie doit toujours choisir aléatoirement entre COOPERER et TRAHIR.");
    }
}
