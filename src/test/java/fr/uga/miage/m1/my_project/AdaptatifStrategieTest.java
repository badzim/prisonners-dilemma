package fr.uga.miage.m1.my_project;

import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;
import fr.uga.miage.m1.my_project.server.models.strategies.AdaptatifStrategie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdaptatifStrategieTest {

    private AdaptatifStrategie strategie;

    @BeforeEach
    void setUp() {
        strategie = new AdaptatifStrategie();
    }

    @Test
    void testInitialSequence() {
        List<TypeAction> actions = new ArrayList<>();

        // Tester les 6 premiers coups (COOPERER)
        for (int i = 0; i < 6; i++) {
            assertEquals(TypeAction.COOPERER, strategie.getAction(actions, 0), "Le coup initial doit être COOPERER.");
        }

        // Tester les 5 coups suivants (TRAHIR)
        for (int i = 0; i < 5; i++) {
            assertEquals(TypeAction.TRAHIR, strategie.getAction(actions, 0), "Le coup initial doit être TRAHIR.");
        }
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
}
