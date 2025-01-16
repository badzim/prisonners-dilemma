package fr.uga.miage.m1.my_project.web.controller;

import fr.uga.miage.m1.my_project.core.exception.rest.ClientIdUsedRestException;
import fr.uga.miage.m1.my_project.core.port.output.EventEmitter;
import fr.uga.miage.m1.my_project.web.service.SseServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.Disposable;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SseControllerTest {

    private WebClient webClient;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @SpyBean
    @Qualifier("sseServiceImpl")
    private EventEmitter sseServiceImpl;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        webClient = WebClient.create("http://localhost:" + port);
    }

    @AfterEach
    void tearDown() {
        sseServiceImpl.safelyRemoveEmitter("testClient1");
    }

    @Test
    void testSubscribe() throws InterruptedException {
        String clientId = "testClient1";
        CountDownLatch latch = new CountDownLatch(1);


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


        // Se désabonner (fermer la connexion)
        subscription.dispose();

        // Attendre un peu pour ping le sse et declancher le onComplete...
        await().atMost(30, TimeUnit.SECONDS).until(() -> !sseServiceImpl.getSseEmitters().containsKey(clientId));

    }

    private void subscribeToSse(WebClient webClient,String clientId) {
        webClient.get()
                .uri("/api/sse/subscribe/" + clientId)
                .retrieve()
                .bodyToFlux(String.class)
                .blockFirst(); // Bloquer pour déclencher l'exception immédiatement
    }

    @Test
    void testSseEndpointWithDuplicateClientId() throws InterruptedException {
        String clientId = "testClient1";
        CountDownLatch latch = new CountDownLatch(1);


        // Premier client s'abonne
        Disposable subscription1 = webClient.get()
                .uri("/api/sse/subscribe/" + clientId)
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(
                        data -> {
                            System.out.println("Données reçues par le client 1 : " + data);
                            latch.countDown();
                        },
                        error -> System.err.println("Erreur côté client 1 : " + error),
                        () -> System.out.println("Flux terminé pour le client 1")
                );

        // Attendre un peu pour que le premier client soit bien connecté
        await().atMost(15, TimeUnit.SECONDS).until(() -> sseServiceImpl.getSseEmitters().containsKey(clientId));
        // Deuxième client tente de s'abonner avec le même clientId
        assertThrows(WebClientResponseException.class, () -> {
            subscribeToSse(webClient,clientId);
        }, "Une exception ClientIdUsedRestException aurait dû être levée");

        // Nettoyer
        subscription1.dispose();
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
