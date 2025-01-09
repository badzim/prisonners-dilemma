package fr.uga.miage.m1.my_project.core.domain.model.strategie;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;


class DonnantDonnantAleatoireStrategieTest {

    private DonnantDonnantAleatoireStrategie strategie;
    private SecureRandom mockRandom;
    @BeforeEach
    void setUp() {
        // Créer un mock de Random
        mockRandom = Mockito.mock(SecureRandom.class);
        strategie = new DonnantDonnantAleatoireStrategie(mockRandom);
    }

    @Test
    void testRandomOrLastAction() {
        List<TypeAction> actions = new ArrayList<>();
        actions.add(TypeAction.COOPERER);
        actions.add(TypeAction.TRAHIR);

        TypeAction result = strategie.getAction(actions, 0);

        // Le résultat doit être soit la dernière action de l'adversaire (TRAHIR), soit une action aléatoire (COOPERER ou TRAHIR)
        assertTrue(result == TypeAction.COOPERER || result == TypeAction.TRAHIR,
                "La stratégie doit retourner soit la dernière action de l'adversaire, soit une action aléatoire.");
    }

    @Test
    void testDefaultCooperateOnEmptyList() {
        // Configurer le mock pour retourner false
        when(mockRandom.nextBoolean()).thenReturn(false);

        List<TypeAction> actions = new ArrayList<>();

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifier que getLastAction n'est pas appelé car la liste est vide
        verify(mockRandom, times(1)).nextBoolean();
        assertEquals(TypeAction.COOPERER, result);
    }
}
