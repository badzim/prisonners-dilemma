package fr.uga.miage.m1.my_project.core.domain.model.joueur;

import fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5.StrategieAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5.StrategieEnumAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.core.domain.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeStrategie;
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
        TypeStrategie strategieTest = TypeStrategie.TOUJOURSTRAHIR;
        fr.uga.strats.g5_2.enums.TypeStrategie typeStrategie = StrategieEnumAdaptateurGr2E5.adapterInverse(strategieTest);
        Strategie strategieExtern = StrategieFactory.creeStrategie(typeStrategie);
        // Création de l'adaptateur

        // Création du robot avec l'adaptateur
        Robot robot = new Robot("1", "RobotExterne", 0);
        robot.setEtat(EtatJoueur.EN_PARTIE_INITIATEUR);

        StrategieAdaptateurGr2E5 adaptateur = new StrategieAdaptateurGr2E5(strategieExtern, new ArrayList<>(List.of(TypeAction.COOPERER, TypeAction.TRAHIR)), robot.getEtat() == EtatJoueur.EN_PARTIE_INITIATEUR );

        robot.setStrategieAutomatique(adaptateur);
        // Historique
        List<TypeAction> historiqueAdversaire = List.of(TypeAction.TRAHIR, TypeAction.COOPERER);


        // Appel à jouer
        TypeAction action = robot.jouer(historiqueAdversaire, 1);

        // Assertions
        assertEquals(TypeAction.TRAHIR, action); // Vérifie l'action retournée
        assertSame(TypeAction.TRAHIR, adaptateur.getActionsRobot().get(adaptateur.getActionsRobot().size() - 1)); // Vérifie l'ajout à l'historique
           }

    @Test
    void testJouerAvecStrategieExterneDonnantDonnant() {
        // Mock de la stratégie externe
        TypeStrategie strategieTest = TypeStrategie.DONNANTDONNANT;
        fr.uga.strats.g5_2.enums.TypeStrategie typeStrategie = StrategieEnumAdaptateurGr2E5.adapterInverse(strategieTest);
        Strategie strategieExtern = StrategieFactory.creeStrategie(typeStrategie);



        // Création du robot avec l'adaptateur
        Robot robot = new Robot("3", "RobotExterne", 0);

        robot.setEtat(EtatJoueur.EN_PARTIE_INITIATEUR);

        // Création de l'adaptateur
        StrategieAdaptateurGr2E5 adaptateur = new StrategieAdaptateurGr2E5(strategieExtern, new ArrayList<>(List.of(TypeAction.COOPERER, TypeAction.TRAHIR)), robot.getEtat() == EtatJoueur.EN_PARTIE_INITIATEUR );
        robot.setStrategieAutomatique(adaptateur);

        // Historique
        List<TypeAction> historiqueAdversaire = List.of(TypeAction.COOPERER, TypeAction.TRAHIR);


        // Appel à jouer
        TypeAction action = robot.jouer(historiqueAdversaire, 1);

        // Assertions
        assertEquals(TypeAction.TRAHIR, action); // Vérifie que le robot imite l'adversaire
        assertTrue(adaptateur.getActionsRobot().contains(TypeAction.TRAHIR)); // Vérifie l'ajout à l'historique
    }
}
