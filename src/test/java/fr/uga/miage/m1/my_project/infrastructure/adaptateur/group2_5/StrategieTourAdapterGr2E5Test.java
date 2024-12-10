package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import fr.uga.strats.g5_2.enums.Decision;
import fr.uga.strats.g5_2.models.Tour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategieTourAdapterGr2E5Test {

    @Test
    void testAdapter_ValidTour() {
        // Crée un tour interne pour tester l'adaptation
        fr.uga.miage.m1.my_project.model.Tour tourIntern = new fr.uga.miage.m1.my_project.model.Tour(

        );
        tourIntern.setActionInitiateur(TypeAction.COOPERER);
        tourIntern.setActionAdversaire(TypeAction.TRAHIR);

        Tour tourExterne = StrategieTourAdapterGr2E5.adapter(tourIntern);

        // Vérifie que la conversion a bien fonctionné
        assertNotNull(tourExterne);
        assertEquals(Decision.COOPERER, tourExterne.getDecisionJoueur(1));
        assertEquals(Decision.TRAHIR, tourExterne.getDecisionJoueur(2));
    }

    @Test
    void testAdapter_InvalidTour() {
        // Test avec un tour invalide (s'il y avait un cas de tour avec des actions invalides)
        fr.uga.miage.m1.my_project.model.Tour tourIntern = new fr.uga.miage.m1.my_project.model.Tour(

        );

        tourIntern.setActionInitiateur(TypeAction.TRAHIR);

        assertThrows(IllegalArgumentException.class, () -> {
            StrategieTourAdapterGr2E5.adapter(tourIntern);
        });
    }
}