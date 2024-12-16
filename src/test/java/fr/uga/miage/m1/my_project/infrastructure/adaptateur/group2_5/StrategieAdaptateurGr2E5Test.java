package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5;

import fr.uga.miage.m1.my_project.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.model.enums.TypeStrategie;
import fr.uga.miage.m1.my_project.model.joueur.Robot;
import fr.uga.strats.g5_2.enums.Decision;
import fr.uga.strats.g5_2.factory.StrategieFactory;
import fr.uga.strats.g5_2.models.Strategie;
import fr.uga.strats.g5_2.models.Tour;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class StrategieAdaptateurGr2E5Test {

    @Test
    void testGetAction_ValidTour() {
        // Crée un mock ou une instance de la stratégie externe
        TypeStrategie choixStrategie = TypeStrategie.TOUJOURSTRAHIR;
        Strategie strategieExterne = StrategieFactory.creeStrategie(StrategieEnumAdaptateurGr2E5.adapterInverse(choixStrategie));

        // Crée une instance de l'adaptateur
        StrategieAdaptateurGr2E5 strategie = new StrategieAdaptateurGr2E5(strategieExterne, new ArrayList<>(List.of(TypeAction.COOPERER)), true);

        Robot robot = new Robot("", "", 1, strategie, EtatJoueur.EN_PARTIE_INITIATEUR);


        TypeAction resultat = robot.jouer(new ArrayList<>(List.of(TypeAction.TRAHIR)), 1);



        List<TypeAction> actionsRobot = strategie.getActionsRobot();
        // Vérifie que l'action obtenue est bien la coopération
        assertEquals(TypeAction.TRAHIR, resultat, "La stratégie doit retourner TRAHIR en réponse à l'appel");
        assertEquals(2, actionsRobot.size(), "La liste des actions du robot doit contenir une action après l'appel");
        assertEquals(TypeAction.TRAHIR, actionsRobot.get(actionsRobot.size() - 1), "L'action enregistrée dans la liste des actions doit être TRAHIR");
        assertEquals(TypeAction.COOPERER, actionsRobot.get(actionsRobot.size() - 2), "L'action déjà enregistrée doit pas changer");

    }

    @Test
    void testGetAction_InvalidTour() {
        // Test avec un tour invalide
        StrategieAdaptateurGr2E5 adaptateur = new StrategieAdaptateurGr2E5(null); // Invalid strategy

        List<TypeAction> actionsJoueur = List.of(TypeAction.COOPERER);
        List<TypeAction> actionsAdversaire = List.of(TypeAction.TRAHIR);
        Tour[] tours = StrategieTourAdapterGr2E5.construireTours(actionsJoueur, actionsAdversaire);

        assertThrows(NullPointerException.class, () -> {
            adaptateur.getAction(tours, 0, 1);
        });
    }
}
