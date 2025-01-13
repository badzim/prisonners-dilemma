package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraduelStrategieTest {

    private GraduelStrategie strategie;
    private List<TYPE_ACTION> actionsAdversaire;

    @BeforeEach
    void setUp() {
        strategie = new GraduelStrategie();
        actionsAdversaire = new ArrayList<>();
    }

    @Test
    void testCooperateInitially() {
        TYPE_ACTION action = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action, "La stratégie doit coopérer au premier tour.");
    }

    @Test
    void testCooperateIfOpponentCooperates() {
        actionsAdversaire.add(TYPE_ACTION.COOPERER);
        TYPE_ACTION action = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action, "La stratégie doit coopérer si l'adversaire a coopéré.");
    }

    @Test
    void testStartVengeanceAfterBetrayal() {
        actionsAdversaire.add(TYPE_ACTION.TRAHIR);
        TYPE_ACTION action = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.TRAHIR, action, "La stratégie doit commencer la vengeance en trahissant.");
    }

    @Test
    void testVengeanceSequence() {
        // L'adversaire coopère deux fois puis trahit trois fois


        TYPE_ACTION action1 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action1, "La stratégie doit cooperer au début.");
        actionsAdversaire.add(TYPE_ACTION.COOPERER);


        TYPE_ACTION action2 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action2, "La stratégie doit continuer à cooperer.");
        actionsAdversaire.add(TYPE_ACTION.COOPERER);

        TYPE_ACTION action3 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action3, "La stratégie doit continuer à cooperer.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR);

        TYPE_ACTION action4 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.TRAHIR, action4, "La stratégie doit activer la vengeance.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR);

        // Deux coopérations après la vengeance
        TYPE_ACTION action5 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action5, "La stratégie doit coopérer après la vengeance.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR);


        // Deux coopérations après la vengeance
        TYPE_ACTION action6 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action6, "La stratégie doit coopérer après la vengeance.");
        actionsAdversaire.add(TYPE_ACTION.COOPERER);

        TYPE_ACTION action7 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.COOPERER, action7, "La stratégie doit coopérer après la vengeance.");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR);

        TYPE_ACTION action8 = strategie.getAction(actionsAdversaire, 0);
        assertEquals(TYPE_ACTION.TRAHIR, action8, "La stratégie doit trahir après trahision");
        actionsAdversaire.add(TYPE_ACTION.TRAHIR);
    }

}
