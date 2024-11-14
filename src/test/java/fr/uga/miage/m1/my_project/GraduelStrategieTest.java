package fr.uga.miage.m1.my_project;

import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;
import fr.uga.miage.m1.my_project.server.models.strategies.GraduelStrategie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraduelStrategieTest {

    private GraduelStrategie strategie;
    private List<TypeAction> actionsAdversaire;

    @BeforeEach
    void setUp() {
        strategie = new GraduelStrategie();
        actionsAdversaire = new ArrayList<>();
    }

    @Test
    void testCooperateInitially() {
        TypeAction action = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action, "La stratégie doit coopérer au premier tour.");
    }

    @Test
    void testCooperateIfOpponentCooperates() {
        actionsAdversaire.add(TypeAction.COOPERER);
        TypeAction action = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action, "La stratégie doit coopérer si l'adversaire a coopéré.");
    }

    @Test
    void testStartVengeanceAfterBetrayal() {
        actionsAdversaire.add(TypeAction.TRAHIR);
        TypeAction action = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.TRAHIR, action, "La stratégie doit commencer la vengeance en trahissant.");
    }

    @Test
    void testVengeanceSequence() {
        // L'adversaire coopère deux fois puis trahit trois fois


        TypeAction action1 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action1, "La stratégie doit cooperer au début.");
        actionsAdversaire.add(TypeAction.COOPERER);


        TypeAction action2 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action2, "La stratégie doit continuer à cooperer.");
        actionsAdversaire.add(TypeAction.COOPERER);

        TypeAction action3 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action3, "La stratégie doit continuer à cooperer.");
        actionsAdversaire.add(TypeAction.TRAHIR);

        TypeAction action4 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.TRAHIR, action4, "La stratégie doit activer la vengeance.");
        actionsAdversaire.add(TypeAction.TRAHIR);

        // Deux coopérations après la vengeance
        TypeAction action5 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action5, "La stratégie doit coopérer après la vengeance.");
        actionsAdversaire.add(TypeAction.TRAHIR);


        // Deux coopérations après la vengeance
        TypeAction action6 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action6, "La stratégie doit coopérer après la vengeance.");
        actionsAdversaire.add(TypeAction.COOPERER);

        TypeAction action7 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.COOPERER, action7, "La stratégie doit coopérer après la vengeance.");
        actionsAdversaire.add(TypeAction.TRAHIR);

        TypeAction action8 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TypeAction.TRAHIR, action8, "La stratégie doit trahir après trahision");
        actionsAdversaire.add(TypeAction.TRAHIR);
    }

}
