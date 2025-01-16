package fr.uga.miage.m1.my_project.web.controller;

import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Humain;
import fr.uga.miage.m1.my_project.core.domain.service.RencontreService;
import fr.uga.miage.m1.my_project.core.port.input.RencontreServicePort;
import fr.uga.miage.m1.my_project.core.port.output.EventEmitter;
import fr.uga.miage.m1.my_project.web.restapi.mapper.RencontreMapper;
import fr.uga.miage.m1.my_project.web.restapi.response.RencontreResponse;
import fr.uga.miage.m1.my_project.web.service.SseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RencontreControllerImplTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @SpyBean
    @Qualifier("rencontreService")
    private RencontreServicePort rencontreService;

    @LocalServerPort
    private int port;

    @SpyBean
    @Qualifier("sseServiceImpl")
    private EventEmitter sseServiceImpl;

    @BeforeEach
    void setUp() {
        // Configuration initiale si nécessaire
    }



    @Test
    void testInitierRencontre_disconnected() {
        // Arrange
        String clientId = "testClient1";
        int nombreTours = 5;

        // Act
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "http://localhost:"+port+"/api/rencontre/initier?clientId=" + clientId + "&nombreTours=" + nombreTours,
                null,
                String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Vérifie que le service a été appelé
        verify(rencontreService, times(1)).initierRencontre(clientId, nombreTours);
    }

    @Test
    void testInitierRencontre_success() {
        // Arrange
        String clientId = "testClient1";
        int nombreTours = 5;

        Map<String, SseEmitter> emitters = new HashMap<>();
        emitters.put("testClient1", new SseEmitter());

        when(sseServiceImpl.getSseEmitters()).thenReturn(emitters);
        // Simuler un appel réussi au service
        when(rencontreService.initierRencontre(clientId, nombreTours)).thenReturn(true);

        // Act
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/api/rencontre/initier?clientId=" + clientId + "&nombreTours=" + nombreTours,
                null,
                String.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rencontre initiée avec succès.", response.getBody());

        // Vérifie que le service a été appelé
        verify(rencontreService, times(1)).initierRencontre(clientId, nombreTours);
    }



    @Test
    void testEnvoyerChoix() {
        // Arrange
        String clientId = "testClient1";
        TYPE_ACTION action = TYPE_ACTION.COOPERER;
        TYPE_STRATEGIE strategie = TYPE_STRATEGIE.DONNANTDONNANT;
        String groupId = "G2_5";

        // Simuler un appel réussi au service
        doNothing().when(rencontreService).enregistrerChoix(clientId, action, strategie, groupId);

        // Act
        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/api/rencontre/play/choix?clientId=" + clientId +
                        "&action=" + action +
                        "&strategie=" + strategie +
                        "&groupId=" + groupId,
                null,
                Void.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Vérifie que le service a été appelé
        verify(rencontreService, times(1)).enregistrerChoix(clientId, action, strategie, groupId);
    }

    @Test
    void testGetRencontresDisponibles() {
        Rencontre rencontre = new Rencontre();
        rencontre.setInitiateur(new Humain("test", "test"));
        rencontre.setAdversaire(new Humain("test1", "test1"));

        // Arrange
        List<RencontreResponse> rencontres = Collections.singletonList(RencontreMapper.toDto(rencontre));

        // Simuler un appel réussi au service
        when(rencontreService.getRencontresEnAttente()).thenReturn(rencontres);

        // Act
        ResponseEntity<List> response = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/api/rencontre/disponibles",
                List.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        // Vérifie que le service a été appelé
        verify(rencontreService, times(1)).getRencontresEnAttente();
    }


    @Test
    void testRejoindreRencontre() {
        // Arrange
        String clientId = "testClient1";
        String idRencontre = "rencontre1";

        // Simuler un appel réussi au service
        doNothing().when(rencontreService).rejoindreRencontre(clientId, idRencontre);

        // Act
        ResponseEntity<Void> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/api/rencontre/rejoindre?clientId=" + clientId + "&idRencontre=" + idRencontre,
                null,
                Void.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Vérifie que le service a été appelé
        verify(rencontreService, times(1)).rejoindreRencontre(clientId, idRencontre);
    }
}
