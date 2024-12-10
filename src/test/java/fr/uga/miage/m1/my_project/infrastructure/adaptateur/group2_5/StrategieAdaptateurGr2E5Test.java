package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import fr.uga.strats.g5_2.enums.Decision;
import fr.uga.strats.g5_2.models.Tour;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class StrategieAdaptateurGr2E5Test {

    @Test
    void testGetAction_ValidTour() {
        // Crée un mock ou une instance de la stratégie externe
        fr.uga.strats.g5_2.models.Strategie strategieExterne = new fr.uga.strats.g5_2.models.Strategie() {
            @Override
            public Decision deciderTour(Tour[] tours, int idJoueur, int idJoueurAdversaire) {
                return Decision.COOPERER; // Simule une décision de coopération
            }
        };

        // Crée une instance de l'adaptateur
        StrategieAdaptateurGr2E5 adaptateur = new StrategieAdaptateurGr2E5(strategieExterne);

        // Crée un tour valide pour tester l'action
        List<TypeAction> actionsJoueur = List.of(TypeAction.COOPERER);
        List<TypeAction> actionsAdversaire = List.of(TypeAction.TRAHIR);
        Tour [] tours = StrategieTourAdapterGr2E5.construireTours(actionsJoueur, actionsAdversaire);

        TypeAction action = adaptateur.getAction(tours, 0, 1);

        // Vérifie que l'action obtenue est bien la coopération
        assertEquals(TypeAction.COOPERER, action);
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
