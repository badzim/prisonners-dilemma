package fr.uga.miage.m1.my_project.service;


import fr.uga.miage.m1.my_project.TestConfig;
import fr.uga.miage.m1.my_project.exception.rest.InvalidActionRestException;
import fr.uga.miage.m1.my_project.model.Rencontre;
import fr.uga.miage.m1.my_project.model.Tour;
import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.model.joueur.Humain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TourServiceTest implements TestConfig {

    @Autowired
    private  TourService tourService;

    @Test
    void testBothCooperate() {
        Tour tour = Tour.builder()
                .actionInitiateur(TypeAction.COOPERER)
                .actionAdversaire(TypeAction.COOPERER)
                .build();
        tourService.calculerScore(tour);
        assertEquals(3, tour.getScoreInitiateur(), "Le score de l'initiateur devrait être 3 lorsque les deux coopèrent.");
        assertEquals(3, tour.getScoreAdversaire(), "Le score de l'adversaire devrait être 3 lorsque les deux coopèrent.");
    }

    @Test
    void testInitiatorCooperatesAdversaryBetrays() {
        Tour tour = Tour.builder()
                .actionInitiateur(TypeAction.COOPERER)
                .actionAdversaire(TypeAction.TRAHIR)
                .build();
        tourService.calculerScore(tour);

        assertEquals(0, tour.getScoreInitiateur(), "Le score de l'initiateur devrait être 0 lorsqu'il coopère et que l'adversaire trahit.");
        assertEquals(5, tour.getScoreAdversaire(), "Le score de l'adversaire devrait être 5 lorsqu'il trahit et que l'initiateur coopère.");
    }

    @Test
    void testInitiatorBetraysAdversaryCooperates() {
        Tour tour = Tour.builder()
                .actionInitiateur(TypeAction.TRAHIR)
                .actionAdversaire(TypeAction.COOPERER)
                .build();
        tourService.calculerScore(tour);

        assertEquals(5, tour.getScoreInitiateur(), "Le score de l'initiateur devrait être 5 lorsqu'il trahit et que l'adversaire coopère.");
        assertEquals(0, tour.getScoreAdversaire(), "Le score de l'adversaire devrait être 0 lorsqu'il coopère et que l'initiateur trahit.");
    }

    @Test
    void testBothBetray() {
        Tour tour = Tour.builder()
                .actionInitiateur(TypeAction.TRAHIR)
                .actionAdversaire(TypeAction.TRAHIR)
                .build();
        tourService.calculerScore(tour);

        assertEquals(1, tour.getScoreInitiateur(), "Le score de l'initiateur devrait être 1 lorsque les deux trahissent.");
        assertEquals(1, tour.getScoreAdversaire(), "Le score de l'adversaire devrait être 1 lorsque les deux trahissent.");
    }

    @Test
    void testSetActionJoueur_InitiateurSetsAction() {
        // Arrange
        Humain initiateur = new Humain("initiateur123", "Initiateur Test");
        Humain adversaire = new Humain("adversaire456", "Adversaire Test");

        Rencontre rencontre = new Rencontre();
        rencontre.setInitiateur(initiateur);
        rencontre.setAdversaire(adversaire);

        Tour tour = new Tour(1);
        rencontre.setCurrentTour(tour);

        TypeAction action = TypeAction.COOPERER;

        // Act
        assertDoesNotThrow(() -> tourService.setActionJoueur(initiateur, rencontre, action));

        // Assert
        assertEquals(action, tour.getActionInitiateur(), "L'action de l'initiateur doit être enregistrée.");
    }

    @Test
    void testSetActionJoueur_AdversaireSetsAction() {
        // Arrange
        Humain initiateur = new Humain("initiateur123", "Initiateur Test");
        Humain adversaire = new Humain("adversaire456", "Adversaire Test");

        Rencontre rencontre = new Rencontre();
        rencontre.setInitiateur(initiateur);
        rencontre.setAdversaire(adversaire);

        Tour tour = new Tour(1);
        rencontre.setCurrentTour(tour);

        TypeAction action = TypeAction.TRAHIR;

        // Act
        assertDoesNotThrow(() -> tourService.setActionJoueur(adversaire, rencontre, action));

        // Assert
        assertEquals(action, tour.getActionAdversaire(), "L'action de l'adversaire doit être enregistrée.");
    }

    @Test
    void testSetActionJoueur_JoueurNotInRencontreThrowsException() {
        // Arrange
        Humain initiateur = new Humain("initiateur123", "Initiateur Test");
        Humain adversaire = new Humain("adversaire456", "Adversaire Test");
        Humain joueurInconnu = new Humain("unknown789", "Joueur Inconnu");

        Rencontre rencontre = new Rencontre();
        rencontre.setInitiateur(initiateur);
        rencontre.setAdversaire(adversaire);

        Tour tour = new Tour(1);
        rencontre.setCurrentTour(tour);

        TypeAction action = TypeAction.COOPERER;

        // Act & Assert
        InvalidActionRestException exception = assertThrows(InvalidActionRestException.class, () -> {
            tourService.setActionJoueur(joueurInconnu, rencontre, action);
        });

        assertEquals("Le joueur ne fait pas partie de cette rencontre.", exception.getMessage());
    }

    @Test
    void testSetActionJoueur_InitiateurAlreadySetActionThrowsException() {
        // Arrange
        Humain initiateur = new Humain("initiateur123", "Initiateur Test");
        Humain adversaire = new Humain("adversaire456", "Adversaire Test");

        Rencontre rencontre = new Rencontre();
        rencontre.setInitiateur(initiateur);
        rencontre.setAdversaire(adversaire);

        Tour tour = new Tour(1);
        tour.setActionInitiateur(TypeAction.COOPERER);
        rencontre.setCurrentTour(tour);

        TypeAction action = TypeAction.TRAHIR;

        // Act & Assert
        InvalidActionRestException exception = assertThrows(InvalidActionRestException.class, () -> {
            tourService.setActionJoueur(initiateur, rencontre, action);
        });

        assertEquals("Vous avez déjà fait votre choix pour ce tour.", exception.getMessage());
    }

    @Test
    void testSetActionJoueur_AdversaireAlreadySetActionThrowsException() {
        // Arrange
        Humain initiateur = new Humain("initiateur123", "Initiateur Test");
        Humain adversaire = new Humain("adversaire456", "Adversaire Test");

        Rencontre rencontre = new Rencontre();
        rencontre.setInitiateur(initiateur);
        rencontre.setAdversaire(adversaire);

        Tour tour = new Tour(1);
        tour.setActionAdversaire(TypeAction.TRAHIR);
        rencontre.setCurrentTour(tour);

        TypeAction action = TypeAction.COOPERER;

        // Act & Assert
        InvalidActionRestException exception = assertThrows(InvalidActionRestException.class, () -> {
            tourService.setActionJoueur(adversaire, rencontre, action);
        });

        assertEquals("Vous avez déjà fait votre choix pour ce tour.", exception.getMessage());
    }
}
