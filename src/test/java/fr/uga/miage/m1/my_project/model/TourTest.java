package fr.uga.miage.m1.my_project.model;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TourTest {

    @Test
    void testBothCooperate() {
        Tour tour = new Tour(TypeAction.COOPERER, TypeAction.COOPERER);

        assertEquals(3, tour.getScoreInitiateur(), "Le score de l'initiateur devrait être 3 lorsque les deux coopèrent.");
        assertEquals(3, tour.getScoreAdversaire(), "Le score de l'adversaire devrait être 3 lorsque les deux coopèrent.");
    }

    @Test
    void testInitiatorCooperatesAdversaryBetrays() {
        Tour tour = new Tour(TypeAction.COOPERER, TypeAction.TRAHIR);

        assertEquals(0, tour.getScoreInitiateur(), "Le score de l'initiateur devrait être 0 lorsqu'il coopère et que l'adversaire trahit.");
        assertEquals(5, tour.getScoreAdversaire(), "Le score de l'adversaire devrait être 5 lorsqu'il trahit et que l'initiateur coopère.");
    }

    @Test
    void testInitiatorBetraysAdversaryCooperates() {
        Tour tour = new Tour(TypeAction.TRAHIR, TypeAction.COOPERER);

        assertEquals(5, tour.getScoreInitiateur(), "Le score de l'initiateur devrait être 5 lorsqu'il trahit et que l'adversaire coopère.");
        assertEquals(0, tour.getScoreAdversaire(), "Le score de l'adversaire devrait être 0 lorsqu'il coopère et que l'initiateur trahit.");
    }

    @Test
    void testBothBetray() {
        Tour tour = new Tour(TypeAction.TRAHIR, TypeAction.TRAHIR);

        assertEquals(1, tour.getScoreInitiateur(), "Le score de l'initiateur devrait être 1 lorsque les deux trahissent.");
        assertEquals(1, tour.getScoreAdversaire(), "Le score de l'adversaire devrait être 1 lorsque les deux trahissent.");
    }
}
