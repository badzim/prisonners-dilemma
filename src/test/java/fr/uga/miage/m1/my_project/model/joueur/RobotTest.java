package fr.uga.miage.m1.my_project.model.joueur;

import fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5.StrategieAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5.StrategieEnumAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.model.enums.TypeStrategie;
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
        StrategieAdaptateurGr2E5 adaptateur = new StrategieAdaptateurGr2E5(strategieExtern);

        // Création du robot avec l'adaptateur
        Robot robot = new Robot("1", "RobotExterne", 0);
        robot.setStrategieAutomatique(adaptateur);
        robot.setEtat(EtatJoueur.EN_PARTIE_INITIATEUR);

        // Historique
        List<TypeAction> historiqueAdversaire = List.of(TypeAction.TRAHIR, TypeAction.COOPERER);
        robot.setHistoriqueJoueur(new ArrayList<>(List.of(TypeAction.COOPERER, TypeAction.TRAHIR)));

        // Appel à jouer
        TypeAction action = robot.jouer(historiqueAdversaire, 1);

        // Assertions
        assertEquals(TypeAction.TRAHIR, action); // Vérifie l'action retournée
        assertTrue(robot.getHistoriqueJoueur().contains(TypeAction.COOPERER)); // Vérifie l'ajout à l'historique
           }

    @Test
    void testJouerAvecStrategieExterneDonnantDonnant() {
        // Mock de la stratégie externe
        TypeStrategie strategieTest = TypeStrategie.DONNANTDONNANT;
        fr.uga.strats.g5_2.enums.TypeStrategie typeStrategie = StrategieEnumAdaptateurGr2E5.adapterInverse(strategieTest);
        Strategie strategieExtern = StrategieFactory.creeStrategie(typeStrategie);

        // Création de l'adaptateur
        StrategieAdaptateurGr2E5 adaptateur = new StrategieAdaptateurGr2E5(strategieExtern);

        // Création du robot avec l'adaptateur
        Robot robot = new Robot("3", "RobotExterne", 0);
        robot.setStrategieAutomatique(adaptateur);
        robot.setEtat(EtatJoueur.EN_PARTIE_INITIATEUR);

        // Historique
        List<TypeAction> historiqueAdversaire = List.of(TypeAction.COOPERER, TypeAction.TRAHIR);
        robot.setHistoriqueJoueur(new ArrayList<>(List.of(TypeAction.COOPERER, TypeAction.COOPERER)));

        // Appel à jouer
        TypeAction action = robot.jouer(historiqueAdversaire, 1);

        // Assertions
        assertEquals(TypeAction.TRAHIR, action); // Vérifie que le robot imite l'adversaire
        assertTrue(robot.getHistoriqueJoueur().contains(TypeAction.TRAHIR)); // Vérifie l'ajout à l'historique
    }
}
