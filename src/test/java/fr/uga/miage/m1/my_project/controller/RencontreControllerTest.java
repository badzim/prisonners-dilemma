package fr.uga.miage.m1.my_project.controller;

import fr.uga.miage.m1.my_project.core.domain.service.RencontreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RencontreControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @SpyBean
    private RencontreService rencontreService;

    @LocalServerPort
    private int port;

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
}
