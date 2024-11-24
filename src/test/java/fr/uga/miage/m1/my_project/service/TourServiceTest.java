package fr.uga.miage.m1.my_project.service;


import fr.uga.miage.m1.my_project.TestConfig;
import fr.uga.miage.m1.my_project.model.Tour;
import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
