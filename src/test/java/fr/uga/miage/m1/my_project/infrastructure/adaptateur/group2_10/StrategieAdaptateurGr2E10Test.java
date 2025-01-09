package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_10;

import fr.uga.m1miage.pc.strategy.Strategy;
import fr.uga.m1miage.pc.strategy.StrategyFactory;
import fr.uga.miage.m1.my_project.core.domain.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeStrategie;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Robot;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class StrategieAdaptateurGr2E10Test {

    @Test
    void testStrategieAdaptateurGr2E10_getAction() {
        TypeStrategie strategieType = TypeStrategie.DONNANTDONNANT;
        Strategy stategyExtern = StrategyFactory.createStrategy(StrategieEnumAdaptateurGr2E10.adapterInverse(strategieType));

        List<TypeAction> actionsRobot = new ArrayList<>(List.of(TypeAction.TRAHIR, TypeAction.COOPERER));
        StrategieAdaptateurGr2E10 strategie = new StrategieAdaptateurGr2E10(stategyExtern, actionsRobot);

        List<TypeAction> opponentActions = new ArrayList<>(List.of(TypeAction.TRAHIR, TypeAction.COOPERER));
        TypeAction action = strategie.getAction(opponentActions, 0);

        assertEquals(TypeAction.COOPERER, action, "La stratégie doit retourner COOPERER en réponse à l'appel");
        assertEquals(3, actionsRobot.size(), "La liste des actions du robot doit contenir une action après l'appel");
        assertEquals(TypeAction.COOPERER, actionsRobot.get(actionsRobot.size() - 1), "L'action enregistrée dans la liste des actions doit être COOPERER");
    }

    @Test
    void testGetAction_withValidInput() {
        // Préparation des actions adverses
        List<TypeAction> opponentActions = List.of(TypeAction.TRAHIR, TypeAction.COOPERER);

        TypeStrategie strategieType = TypeStrategie.DONNANTDONNANT;
        Strategy stategyExtern = StrategyFactory.createStrategy(StrategieEnumAdaptateurGr2E10.adapterInverse(strategieType));

        List<TypeAction> actionsRobot = new ArrayList<>(List.of(TypeAction.TRAHIR, TypeAction.COOPERER));
        StrategieAdaptateurGr2E10 strategie = new StrategieAdaptateurGr2E10(stategyExtern, actionsRobot);
        Robot robot = new Robot("", "", 0, strategie, EtatJoueur.EN_PARTIE_INITIATEUR);
        TypeAction result = robot.jouer(new ArrayList<>(List.of(TypeAction.TRAHIR, TypeAction.COOPERER)), 1);
        // Vérifications
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals(TypeAction.COOPERER, result);
        assertEquals(3, actionsRobot.size(), "La liste des actions du robot doit contenir une action après l'appel");
        assertEquals(TypeAction.TRAHIR, actionsRobot.get(0), "L'action enregistrée doit correspondre au résultat retourné");
        assertEquals(TypeAction.COOPERER, actionsRobot.get(actionsRobot.size() - 1));
    }

    @Test
    void testGetAction_withEmptyOpponentActions() {
        // Préparation des actions adverses vides
        List<TypeAction> opponentActions = List.of();

        TypeStrategie strategieType = TypeStrategie.DONNANTDONNANT;
        Strategy strategyExtern = StrategyFactory.createStrategy(StrategieEnumAdaptateurGr2E10.adapterInverse(strategieType));

        List<TypeAction> actionsRobot = new ArrayList<>();
        StrategieAdaptateurGr2E10 strategie = new StrategieAdaptateurGr2E10(strategyExtern, actionsRobot);
        Robot robot = new Robot("", "", 0, strategie, EtatJoueur.EN_PARTIE_INITIATEUR);

        // Appel de la méthode
        TypeAction result = robot.jouer(opponentActions, 0);

        // Vérifications
        assertNotNull(result, "Le résultat ne doit pas être null même si les actions adverses sont vides");
        assertEquals(1, actionsRobot.size(), "La liste des actions du robot doit contenir une action après l'appel");
        assertEquals(result, actionsRobot.get(0), "L'action enregistrée doit correspondre au résultat retourné");
    }


    @Test
    void testGetAction_withMultipleCalls() {
        // Préparation des actions adverses
        List<TypeAction> opponentActions1 = List.of(TypeAction.TRAHIR);
        List<TypeAction> opponentActions2 = List.of(TypeAction.COOPERER);

        TypeStrategie strategieType = TypeStrategie.DONNANTDONNANT;
        Strategy strategyExtern = StrategyFactory.createStrategy(StrategieEnumAdaptateurGr2E10.adapterInverse(strategieType));

        List<TypeAction> actionsRobot = new ArrayList<>();
        StrategieAdaptateurGr2E10 strategie = new StrategieAdaptateurGr2E10(strategyExtern, actionsRobot);
        Robot robot = new Robot("", "", 0, strategie, EtatJoueur.EN_PARTIE_INITIATEUR);

        // Appels multiples
        TypeAction result1 = robot.jouer(opponentActions1, 0);
        TypeAction result2 = robot.jouer(opponentActions2, 1);

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
        List<TypeAction> opponentActions = List.of(TypeAction.COOPERER, TypeAction.COOPERER, TypeAction.TRAHIR);

        TypeStrategie strategieType = TypeStrategie.DONNANTDONNANT;
        Strategy strategyExtern = StrategyFactory.createStrategy(StrategieEnumAdaptateurGr2E10.adapterInverse(strategieType));

        List<TypeAction> actionsRobot = new ArrayList<>();
        StrategieAdaptateurGr2E10 strategie = new StrategieAdaptateurGr2E10(strategyExtern, actionsRobot);
        Robot robot = new Robot("", "", 0, strategie, EtatJoueur.EN_PARTIE_INITIATEUR);

        // Appel de la méthode
        TypeAction result = robot.jouer(opponentActions, 0);

        // Vérifications
        assertNotNull(result, "Le résultat ne doit pas être null même dans des cas limites de stratégie adverse");
        assertEquals(1, actionsRobot.size(), "La liste des actions du robot doit contenir une action après l'appel");
        assertEquals(result, actionsRobot.get(0), "L'action enregistrée doit correspondre au résultat retourné");
    }
}
