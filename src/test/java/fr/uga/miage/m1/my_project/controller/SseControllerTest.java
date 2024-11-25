package fr.uga.miage.m1.my_project.controller;

import fr.uga.miage.m1.my_project.service.SseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SseControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private SseService sseService;

    @Test
    void testSubscribe() {
        // Appeler l'endpoint pour s'abonner
        String clientId = "testClient1";

        ResponseEntity<SseEmitter> response = testRestTemplate.exchange(
                "/api/sse/subscribe/" + clientId,
                HttpMethod.GET,
                new HttpEntity<>(null),
                SseEmitter.class
        );
        // Vérifier la réponse
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Vérifier que le service a bien été appelé
        verify(sseService, times(1)).addSseEmitter(clientId);
    }

    @Test
    void testSendMessageToClient() {
        // Préparer les données
        String clientId = "testClient";
        String message = "Test message";

        // Simuler un comportement dans le service
        doNothing().when(sseService).sendMessage(clientId, message);

        // Effectuer la requête POST
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/api/sse/send/" + clientId,
                message,
                String.class
        );

        // Vérifier la réponse
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Si le client était connecté, le message a été envoyé.", response.getBody());

        // Vérifier que le service a bien été appelé
        verify(sseService, times(1)).sendMessage(clientId, message);
    }

    @Test
    void testSendMessageToNonExistentClient() {
        String clientId = "nonExistentClient";
        String message = "Test message";

        // Simuler un comportement dans le service
        doNothing().when(sseService).sendMessage(clientId, message);

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/api/sse/send/" + clientId,
                message,
                String.class
        );

        // Vérifier la réponse
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Si le client était connecté, le message a été envoyé.", response.getBody());

        // Vérifier que le service a bien été appelé
        verify(sseService, times(1)).sendMessage(clientId, message);
    }

    @Test
    void testBroadcast() {
        // Diffuser un message
        String message = "Hello, clients!";
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/sse/broadcast",
                HttpMethod.POST,
                new HttpEntity<>(message),
                String.class
        );

        // Vérifier la réponse
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Message diffusé à tous les clients.", response.getBody());

        verify(sseService, times(1)).broadcast(message);
    }
}
