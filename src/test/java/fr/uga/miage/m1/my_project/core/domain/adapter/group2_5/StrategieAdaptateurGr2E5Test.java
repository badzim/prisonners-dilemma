package fr.uga.miage.m1.my_project.core.domain.adapter.group2_5;

import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5.StrategieAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5.StrategieEnumAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5.StrategieTourAdapterGr2E5;
import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_JOUEUR;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Robot;
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
        TYPE_STRATEGIE choixStrategie = TYPE_STRATEGIE.TOUJOURSTRAHIR;
        Strategie strategieExterne = StrategieFactory.creeStrategie(StrategieEnumAdaptateurGr2E5.adapterInverse(choixStrategie));

        // Crée une instance de l'adaptateur
        StrategieAdaptateurGr2E5 strategie = new StrategieAdaptateurGr2E5(strategieExterne, new ArrayList<>(List.of(TYPE_ACTION.COOPERER)), true);

        Robot robot = new Robot("", "", 1, strategie, ETAT_JOUEUR.EN_PARTIE_INITIATEUR);


        TYPE_ACTION resultat = robot.jouer(new ArrayList<>(List.of(TYPE_ACTION.TRAHIR)), 1);



        List<TYPE_ACTION> actionsRobot = strategie.getActionsRobot();
        // Vérifie que l'action obtenue est bien la coopération
        assertEquals(TYPE_ACTION.TRAHIR, resultat, "La stratégie doit retourner TRAHIR en réponse à l'appel");
        assertEquals(2, actionsRobot.size(), "La liste des actions du robot doit contenir une action après l'appel");
        assertEquals(TYPE_ACTION.TRAHIR, actionsRobot.get(actionsRobot.size() - 1), "L'action enregistrée dans la liste des actions doit être TRAHIR");
        assertEquals(TYPE_ACTION.COOPERER, actionsRobot.get(actionsRobot.size() - 2), "L'action déjà enregistrée doit pas changer");

    }

    @Test
    void testGetAction_InvalidTour() {
        // Test avec un tour invalide
        StrategieAdaptateurGr2E5 adaptateur = new StrategieAdaptateurGr2E5(null); // Invalid strategy

        List<TYPE_ACTION> actionsJoueur = List.of(TYPE_ACTION.COOPERER);
        List<TYPE_ACTION> actionsAdversaire = List.of(TYPE_ACTION.TRAHIR);
        Tour[] tours = StrategieTourAdapterGr2E5.construireTours(actionsJoueur, actionsAdversaire);

        assertThrows(NullPointerException.class, () -> {
            adaptateur.getAction(tours, 0, 1);
        });
    }
}
