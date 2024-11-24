package fr.uga.miage.m1.my_project.model.strategie;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AdaptatifStrategieTest {

    private AdaptatifStrategie strategie;

    @BeforeEach
    void setUp() {
        strategie = new AdaptatifStrategie();
    }


    @Test
    void testInitialSequence() {
        // Vérifier la séquence initiale (c, c, c, c, c, c, t, t, t, t, t)
        List<TypeAction> actions = new ArrayList<>();
        int dernierResultat = 0;

        for (int i = 0; i < 11; i++) {
            TypeAction action = strategie.getAction(actions, dernierResultat);
            actions.add(action);

            if (i < 6) {
                assertEquals(TypeAction.COOPERER, action, "L'action devrait être COOPERER dans la séquence initiale.");
            } else {
                assertEquals(TypeAction.TRAHIR, action, "L'action devrait être TRAHIR dans la séquence initiale.");
            }
        }
    }

    @Test
    void testEtatInitialStrategie() {
        assertEquals(strategie.getCoupCount(), 0);
        assertEquals(strategie.getScoreC(), 0);
        assertEquals(strategie.getScoreT(), 0);
        assertEquals(strategie.getCountC(), 0);
        assertEquals(strategie.getCountT(), 0);
        double[] expectedResult = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1};
        assertArrayEquals(strategie.getSequenceInitiale(), expectedResult);

    }

    @Test
    void testAdaptativeChoiceBasedOnScores() {
        List<TypeAction> actionsAdversaire = new ArrayList<>();

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TypeAction action1 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action1, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TypeAction.COOPERER); // score 3

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TypeAction action2 =  strategie.getAction(actionsAdversaire, 3);
        assertEquals(TypeAction.COOPERER, action2, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TypeAction.TRAHIR); // score 0

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TypeAction action3 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action3, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TypeAction.TRAHIR); // score 0

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TypeAction action4 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action4, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TypeAction.TRAHIR); // score 0

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TypeAction action5 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action5, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TypeAction.TRAHIR); // score 0

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TypeAction action6 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action6, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TypeAction.TRAHIR); // score 0

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TypeAction action7 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.TRAHIR, action7, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TypeAction.TRAHIR); // score 1

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TypeAction action8 =  strategie.getAction(actionsAdversaire, 1);
        assertEquals(TypeAction.TRAHIR, action8, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TypeAction.TRAHIR); // score 1

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TypeAction action9 =  strategie.getAction(actionsAdversaire, 1);
        assertEquals(TypeAction.TRAHIR, action9, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TypeAction.TRAHIR); // score 1

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TypeAction action10 =  strategie.getAction(actionsAdversaire, 1);
        assertEquals(TypeAction.TRAHIR, action10, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TypeAction.TRAHIR); // score 1

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TypeAction action11 =  strategie.getAction(actionsAdversaire, 1);
        assertEquals(TypeAction.TRAHIR, action11, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TypeAction.TRAHIR); // score 1


        TypeAction action12 =  strategie.getAction(actionsAdversaire, 1);
        assertEquals(TypeAction.TRAHIR, action12, "Le coup initial doit être COOPERER.");

    }

    @Test
    void testUpdateScores() {
        // Simuler quelques actions et résultats pour vérifier la mise à jour des scores
        // verifier au debut
    }

    @Test
    void testEmptyActions() {
        // Test avec une liste vide d'actions
        List<TypeAction> actions = new ArrayList<>();
        TypeAction action = strategie.getAction(actions, 0);
        assertEquals(TypeAction.COOPERER, action, "Le premier coup devrait suivre la séquence initiale.");
    }
}
