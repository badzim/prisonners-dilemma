package fr.uga.miage.m1.my_project.core.domain.model.joueur;

import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5.StrategieAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5.StrategieEnumAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_JOUEUR;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import fr.uga.strats.g5_2.factory.StrategieFactory;
import fr.uga.strats.g5_2.models.Strategie;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    @Test
    void testJouerAvecStrategieExterne() {
        // Mock de la stratégie externe
        TYPE_STRATEGIE strategieTest = TYPE_STRATEGIE.TOUJOURSTRAHIR;
        fr.uga.strats.g5_2.enums.TypeStrategie typeStrategie = StrategieEnumAdaptateurGr2E5.adapterInverse(strategieTest);
        Strategie strategieExtern = StrategieFactory.creeStrategie(typeStrategie);
        // Création de l'adaptateur

        // Création du robot avec l'adaptateur
        Robot robot = new Robot("1", "RobotExterne", 0);
        robot.setEtat(ETAT_JOUEUR.EN_PARTIE_INITIATEUR);

        StrategieAdaptateurGr2E5 adaptateur = new StrategieAdaptateurGr2E5(strategieExtern, new ArrayList<>(List.of(TYPE_ACTION.COOPERER, TYPE_ACTION.TRAHIR)), robot.getEtat() == ETAT_JOUEUR.EN_PARTIE_INITIATEUR );

        robot.setStrategieAutomatique(adaptateur);
        // Historique
        List<TYPE_ACTION> historiqueAdversaire = List.of(TYPE_ACTION.TRAHIR, TYPE_ACTION.COOPERER);


        // Appel à jouer
        TYPE_ACTION action = robot.jouer(historiqueAdversaire, 1);

        // Assertions
        assertEquals(TYPE_ACTION.TRAHIR, action); // Vérifie l'action retournée
        assertSame(TYPE_ACTION.TRAHIR, adaptateur.getActionsRobot().get(adaptateur.getActionsRobot().size() - 1)); // Vérifie l'ajout à l'historique
           }

    @Test
    void testJouerAvecStrategieExterneDonnantDonnant() {
        // Mock de la stratégie externe
        TYPE_STRATEGIE strategieTest = TYPE_STRATEGIE.DONNANTDONNANT;
        fr.uga.strats.g5_2.enums.TypeStrategie typeStrategie = StrategieEnumAdaptateurGr2E5.adapterInverse(strategieTest);
        Strategie strategieExtern = StrategieFactory.creeStrategie(typeStrategie);



        // Création du robot avec l'adaptateur
        Robot robot = new Robot("3", "RobotExterne", 0);

        robot.setEtat(ETAT_JOUEUR.EN_PARTIE_INITIATEUR);

        // Création de l'adaptateur
        StrategieAdaptateurGr2E5 adaptateur = new StrategieAdaptateurGr2E5(strategieExtern, new ArrayList<>(List.of(TYPE_ACTION.COOPERER, TYPE_ACTION.TRAHIR)), robot.getEtat() == ETAT_JOUEUR.EN_PARTIE_INITIATEUR );
        robot.setStrategieAutomatique(adaptateur);

        // Historique
        List<TYPE_ACTION> historiqueAdversaire = List.of(TYPE_ACTION.COOPERER, TYPE_ACTION.TRAHIR);


        // Appel à jouer
        TYPE_ACTION action = robot.jouer(historiqueAdversaire, 1);

        // Assertions
        assertEquals(TYPE_ACTION.TRAHIR, action); // Vérifie que le robot imite l'adversaire
        assertTrue(adaptateur.getActionsRobot().contains(TYPE_ACTION.TRAHIR)); // Vérifie l'ajout à l'historique
    }
}
