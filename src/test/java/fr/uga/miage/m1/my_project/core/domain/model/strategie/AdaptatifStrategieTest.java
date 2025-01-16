package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
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
        List<TYPE_ACTION> actions = new ArrayList<>();
        int dernierResultat = 0;

        for (int i = 0; i < 11; i++) {
            TYPE_ACTION action = strategie.getAction(actions, dernierResultat);
            actions.add(action);

            if (i < 6) {
                assertEquals(TYPE_ACTION.COOPERER, action, "L'action devrait être COOPERER dans la séquence initiale.");
            } else {
                assertEquals(TYPE_ACTION.TRAHIR, action, "L'action devrait être TRAHIR dans la séquence initiale.");
            }
        }
    }

    @Test
    void testAdaptativeChoiceBasedOnScores() {
        List<TYPE_ACTION> actionsAdversaire = new ArrayList<>();

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TYPE_ACTION action1 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action1, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TYPE_ACTION.COOPERER); // score 3

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TYPE_ACTION action2 =  strategie.getAction(actionsAdversaire, 3);
        assertEquals(TYPE_ACTION.COOPERER, action2, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR); // score 0

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TYPE_ACTION action3 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action3, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR); // score 0

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TYPE_ACTION action4 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action4, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR); // score 0

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TYPE_ACTION action5 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action5, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR); // score 0

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TYPE_ACTION action6 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action6, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR); // score 0

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TYPE_ACTION action7 =  strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.TRAHIR, action7, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR); // score 1

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TYPE_ACTION action8 =  strategie.getAction(actionsAdversaire, 1);
        assertEquals(TYPE_ACTION.TRAHIR, action8, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR); // score 1

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TYPE_ACTION action9 =  strategie.getAction(actionsAdversaire, 1);
        assertEquals(TYPE_ACTION.TRAHIR, action9, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR); // score 1

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TYPE_ACTION action10 =  strategie.getAction(actionsAdversaire, 1);
        assertEquals(TYPE_ACTION.TRAHIR, action10, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR); // score 1

        // Après la séquence initiale, on effectue des choix adaptatifs
        // Ajouter un score favorable à COOPERER
        TYPE_ACTION action11 =  strategie.getAction(actionsAdversaire, 1);
        assertEquals(TYPE_ACTION.TRAHIR, action11, "Le coup initial doit être COOPERER.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR); // score 1


        TYPE_ACTION action12 =  strategie.getAction(actionsAdversaire, 1);
        assertEquals(TYPE_ACTION.TRAHIR, action12, "Le coup initial doit être COOPERER.");

    }

    @Test
    void testEmptyActions() {
        // Test avec une liste vide d'actions
        List<TYPE_ACTION> actions = new ArrayList<>();
        TYPE_ACTION action = strategie.getAction(actions, 0);
        assertEquals(TYPE_ACTION.COOPERER, action, "Le premier coup devrait suivre la séquence initiale.");
    }

}
