package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DonnantPourDeuxDonnantsEtAleatoireStrategieTest {

    private DonnantPourDeuxDonnantsEtAleatoireStrategie strategie;
    private SecureRandom mockRandom;
    private SecureRandom mockSecondRandom;

    @BeforeEach
    void setUp() {
        // Créer un mock de Random
        mockRandom = Mockito.mock(SecureRandom.class);
        mockSecondRandom = Mockito.mock(SecureRandom.class);
        strategie = new DonnantPourDeuxDonnantsEtAleatoireStrategie(mockRandom, mockSecondRandom);
    }

    @Test
    void testCooperateWhenNoPreviousActions() {
        // Simuler que le coup aléatoire ne se produit pas
        when(mockRandom.nextInt(10)).thenReturn(1);

        List<TYPE_ACTION> actions = new ArrayList<>();
        TYPE_ACTION result = strategie.getAction(actions, 0);

        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer par défaut si aucune action précédente.");
    }

    @Test
    void testRandomActionOccurs() {
        // Simuler que le coup aléatoire se produit
        when(mockRandom.nextInt(10)).thenReturn(0);
        when(mockSecondRandom.nextBoolean()).thenReturn(true); // Simule COOPERER

        List<TYPE_ACTION> actions = new ArrayList<>();

        TYPE_ACTION result = strategie.getAction(actions, 0);

        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer lors du coup aléatoire.");
    }

    @Test
    void testRandomActionOccursBetray() {
        // Simuler que le coup aléatoire se produit
        when(mockRandom.nextInt(10)).thenReturn(0);
        when(mockRandom.nextBoolean()).thenReturn(false); // Simule TRAHIR

        List<TYPE_ACTION> actions = new ArrayList<>();

        TYPE_ACTION result = strategie.getAction(actions, 0);

        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit trahir lors du coup aléatoire.");
    }

    @Test
    void testCooperateAfterTwoSameActions() {
        // Simuler que le coup aléatoire ne se produit pas
        when(mockRandom.nextInt(10)).thenReturn(1);

        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER);
        actions.add(TYPE_ACTION.COOPERER);

        TYPE_ACTION result = strategie.getAction(actions, 0);

        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit coopérer si l'adversaire a coopéré deux fois de suite.");
    }

    @Test
    void testBetrayAfterNoTwoSameActions() {
        // Simuler que le coup aléatoire ne se produit pas
        when(mockRandom.nextInt(10)).thenReturn(1);

        List<TYPE_ACTION> actions = new ArrayList<>();
        actions.add(TYPE_ACTION.COOPERER);
        actions.add(TYPE_ACTION.TRAHIR);

        TYPE_ACTION result = strategie.getAction(actions, 0);

        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit trahir si l'adversaire n'a pas fait la même action deux fois de suite.");
    }
}
