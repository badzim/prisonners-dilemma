package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_10;

import fr.uga.m1miage.pc.strategy.Strategy;
import fr.uga.m1miage.pc.strategy.StrategyFactory;
import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_10.StrategieAdaptateurGr2E10;
import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_10.StrategieEnumAdaptateurGr2E10;
import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_JOUEUR;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Robot;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class StrategieAdaptateurGr2E10Test {

    @Test
    void testStrategieAdaptateurGr2E10_getAction() {
        TYPE_STRATEGIE strategieType = TYPE_STRATEGIE.DONNANTDONNANT;
        Strategy stategyExtern = StrategyFactory.createStrategy(StrategieEnumAdaptateurGr2E10.adapterInverse(strategieType));

        List<TYPE_ACTION> actionsRobot = new ArrayList<>(List.of(TYPE_ACTION.TRAHIR, TYPE_ACTION.COOPERER));
        StrategieAdaptateurGr2E10 strategie = new StrategieAdaptateurGr2E10(stategyExtern, actionsRobot);

        List<TYPE_ACTION> opponentActions = new ArrayList<>(List.of(TYPE_ACTION.TRAHIR, TYPE_ACTION.COOPERER));
        TYPE_ACTION action = strategie.getAction(opponentActions, 0);

        assertEquals(TYPE_ACTION.COOPERER, action, "La stratégie doit retourner COOPERER en réponse à l'appel");
        assertEquals(3, actionsRobot.size(), "La liste des actions du robot doit contenir une action après l'appel");
        assertEquals(TYPE_ACTION.COOPERER, actionsRobot.get(actionsRobot.size() - 1), "L'action enregistrée dans la liste des actions doit être COOPERER");
    }

    @Test
    void testGetAction_withValidInput() {
        // Préparation des actions adverses
        List<TYPE_ACTION> opponentActions = List.of(TYPE_ACTION.TRAHIR, TYPE_ACTION.COOPERER);

        TYPE_STRATEGIE strategieType = TYPE_STRATEGIE.DONNANTDONNANT;
        Strategy stategyExtern = StrategyFactory.createStrategy(StrategieEnumAdaptateurGr2E10.adapterInverse(strategieType));

        List<TYPE_ACTION> actionsRobot = new ArrayList<>(List.of(TYPE_ACTION.TRAHIR, TYPE_ACTION.COOPERER));
        StrategieAdaptateurGr2E10 strategie = new StrategieAdaptateurGr2E10(stategyExtern, actionsRobot);
        Robot robot = new Robot("", "", 0, strategie, ETAT_JOUEUR.EN_PARTIE_INITIATEUR);
        TYPE_ACTION result = robot.jouer(new ArrayList<>(List.of(TYPE_ACTION.TRAHIR, TYPE_ACTION.COOPERER)), 1);
        // Vérifications
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals(TYPE_ACTION.COOPERER, result);
        assertEquals(3, actionsRobot.size(), "La liste des actions du robot doit contenir une action après l'appel");
        assertEquals(TYPE_ACTION.TRAHIR, actionsRobot.get(0), "L'action enregistrée doit correspondre au résultat retourné");
        assertEquals(TYPE_ACTION.COOPERER, actionsRobot.get(actionsRobot.size() - 1));
    }

    @Test
    void testGetAction_withEmptyOpponentActions() {
        // Préparation des actions adverses vides
        List<TYPE_ACTION> opponentActions = List.of();

        TYPE_STRATEGIE strategieType = TYPE_STRATEGIE.DONNANTDONNANT;
        Strategy strategyExtern = StrategyFactory.createStrategy(StrategieEnumAdaptateurGr2E10.adapterInverse(strategieType));

        List<TYPE_ACTION> actionsRobot = new ArrayList<>();
        StrategieAdaptateurGr2E10 strategie = new StrategieAdaptateurGr2E10(strategyExtern, actionsRobot);
        Robot robot = new Robot("", "", 0, strategie, ETAT_JOUEUR.EN_PARTIE_INITIATEUR);

        // Appel de la méthode
        TYPE_ACTION result = robot.jouer(opponentActions, 0);

        // Vérifications
        assertNotNull(result, "Le résultat ne doit pas être null même si les actions adverses sont vides");
        assertEquals(1, actionsRobot.size(), "La liste des actions du robot doit contenir une action après l'appel");
        assertEquals(result, actionsRobot.get(0), "L'action enregistrée doit correspondre au résultat retourné");
    }


    @Test
    void testGetAction_withMultipleCalls() {
        // Préparation des actions adverses
        List<TYPE_ACTION> opponentActions1 = List.of(TYPE_ACTION.TRAHIR);
        List<TYPE_ACTION> opponentActions2 = List.of(TYPE_ACTION.COOPERER);

        TYPE_STRATEGIE strategieType = TYPE_STRATEGIE.DONNANTDONNANT;
        Strategy strategyExtern = StrategyFactory.createStrategy(StrategieEnumAdaptateurGr2E10.adapterInverse(strategieType));

        List<TYPE_ACTION> actionsRobot = new ArrayList<>();
        StrategieAdaptateurGr2E10 strategie = new StrategieAdaptateurGr2E10(strategyExtern, actionsRobot);
        Robot robot = new Robot("", "", 0, strategie, ETAT_JOUEUR.EN_PARTIE_INITIATEUR);

        // Appels multiples
        TYPE_ACTION result1 = robot.jouer(opponentActions1, 0);
        TYPE_ACTION result2 = robot.jouer(opponentActions2, 1);

        // Vérifications
        assertNotNull(result1, "Premier appel : le résultat ne doit pas être null");
        assertNotNull(result2, "Deuxième appel : le résultat ne doit pas être null");

        assertEquals(2, actionsRobot.size(), "La liste des actions du robot doit contenir deux actions après deux appels");
        assertEquals(result1, actionsRobot.get(0), "La première action enregistrée doit correspondre au premier résultat");
        assertEquals(result2, actionsRobot.get(1), "La deuxième action enregistrée doit correspondre au deuxième résultat");
    }


    @Test
    void testGetAction_withEdgeCaseOpponentMoves() {
        // Préparation des actions adverses avec des répétitions ou aucune cohérence
        List<TYPE_ACTION> opponentActions = List.of(TYPE_ACTION.COOPERER, TYPE_ACTION.COOPERER, TYPE_ACTION.TRAHIR);

        TYPE_STRATEGIE strategieType = TYPE_STRATEGIE.DONNANTDONNANT;
        Strategy strategyExtern = StrategyFactory.createStrategy(StrategieEnumAdaptateurGr2E10.adapterInverse(strategieType));

        List<TYPE_ACTION> actionsRobot = new ArrayList<>();
        StrategieAdaptateurGr2E10 strategie = new StrategieAdaptateurGr2E10(strategyExtern, actionsRobot);
        Robot robot = new Robot("", "", 0, strategie, ETAT_JOUEUR.EN_PARTIE_INITIATEUR);

        // Appel de la méthode
        TYPE_ACTION result = robot.jouer(opponentActions, 0);

        // Vérifications
        assertNotNull(result, "Le résultat ne doit pas être null même dans des cas limites de stratégie adverse");
        assertEquals(1, actionsRobot.size(), "La liste des actions du robot doit contenir une action après l'appel");
        assertEquals(result, actionsRobot.get(0), "L'action enregistrée doit correspondre au résultat retourné");
    }
}
