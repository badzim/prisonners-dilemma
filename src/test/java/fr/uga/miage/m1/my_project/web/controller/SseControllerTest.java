package fr.uga.miage.m1.my_project.web.controller;

import fr.uga.miage.m1.my_project.web.service.SseServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SseControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @SpyBean
    private SseServiceImpl sseServiceImpl;

    @LocalServerPort
    private int port;

    @Test
    void testSubscribe() throws InterruptedException {
        String clientId = "testClient1";
        CountDownLatch latch = new CountDownLatch(1);

        // Créer un client WebClient
        WebClient webClient = WebClient.create("http://localhost:" + port);

        // S'abonner à l'endpoint SSE
        Disposable subscription = webClient.get()
                .uri("/api/sse/subscribe/" + clientId)
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(
                        data -> {
                            System.out.println("Données reçues : " + data);
                            latch.countDown();
                        },
                        error -> System.err.println("Erreur : " + error),
                        () -> System.out.println("Flux terminé")
                );
        // Envoyer un événement
        sseServiceImpl.sendEvent(clientId, "testEvent", "testData");

        // Attendre que l'événement soit reçu
        assertTrue(latch.await(5, TimeUnit.SECONDS), "L'événement n'a pas été reçu");

        // Se désabonner (fermer la connexion)
        subscription.dispose();

        // Attendre un peu pour ping le sse et declancher le onComplete...
        await().atMost(30, TimeUnit.SECONDS).until(() -> !sseServiceImpl.getSseEmitters().containsKey(clientId));

    }

    @Test
    void testSendMessageToClient() {
        // Préparer les données
        String clientId = "testClient";
        String message = "Test message";

        // Simuler un comportement dans le service
        doNothing().when(sseServiceImpl).sendEvent(clientId, "message", message);

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
        verify(sseServiceImpl, times(1)).sendEvent(clientId, "message", message);
    }

    @Test
    void testSendMessageToNonExistentClient() {
        String clientId = "nonExistentClient";
        String message = "Test message";

        // Simuler un comportement dans le service
        doNothing().when(sseServiceImpl).sendEvent(clientId, "message", message);

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
        verify(sseServiceImpl, times(1)).sendEvent(clientId, "message", message);
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

        verify(sseServiceImpl, times(1)).broadcast("broadcast",message);
    }
}
