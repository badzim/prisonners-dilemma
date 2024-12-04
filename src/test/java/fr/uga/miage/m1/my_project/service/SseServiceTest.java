package fr.uga.miage.m1.my_project.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SseServiceTest {


    private SseService sseService;

    @BeforeEach
    public void setUp() {
        sseService = new SseService();
    }

    @Test
    void testAddSseEmitter() {
        // Ajouter un SseEmitter pour un client
        String clientId = "testClient";
        SseEmitter emitter = sseService.addSseEmitter(clientId);
        ResponseEntity<SseEmitter> reponse = ResponseEntity.ok(emitter);
        // Vérifier que l'émetteur a bien été ajouté
        assertNotNull(emitter);
        assertTrue(sseService.getSseEmitters().containsKey(clientId));
        // Simuler une completion
        emitter.complete();
        await().atMost(30, TimeUnit.SECONDS).until(() -> !sseService.getSseEmitters().containsKey(clientId));
        assertFalse(sseService.getSseEmitters().containsKey(clientId));
    }

    @Test
    void testSendEventWithArgumentCaptor() throws IOException {
        // Mock SseEmitter
        SseEmitter emitter = mock(SseEmitter.class);
        String clientId = "testClient";
        String message = "Test message";
        sseService.getSseEmitters().put(clientId, emitter);

        // Appeler la méthode
        sseService.sendEvent(clientId, "message", message);

        // Capturer l'argument passé à send
        ArgumentCaptor<SseEmitter.SseEventBuilder> captor = ArgumentCaptor.forClass(SseEmitter.SseEventBuilder.class);

        // Vérifier que send a été appelé
        verify(emitter).send(captor.capture());

        // Vérifier le contenu de l'argument capturé
        SseEmitter.SseEventBuilder eventBuilder = captor.getValue();
        Set<ResponseBodyEmitter.DataWithMediaType> eventData = eventBuilder.build(); // Extrait la donnée envoyée
        // Vérifier que le message attendu est présent
        assertTrue(eventData.stream().anyMatch(dataWithMediaType -> message.equals(dataWithMediaType.getData())), "message envoyé ne correspond pas");
//        assertEquals(message, eventData); // Vérifie que les données correspondent
    }

    @Test
    void testSendEventToNonExistentClient() {
        // Envoyer un message à un client qui n'existe pas
        String clientId = "nonExistentClient";
        String message = "Test message";

        sseService.sendEvent(clientId, "message", message);

        // Aucun émetteur, vérifier que rien ne plante
        assertFalse(sseService.getSseEmitters().containsKey(clientId));
    }

    @Test
    void testSendEventHandlesIOException() throws IOException {
        // Ajouter un SseEmitter mocké pour simuler une erreur
        String clientId = "testClient";
        SseEmitter emitter = mock(SseEmitter.class);
        doThrow(new IOException("Simulated error")).when(emitter).send(any(SseEmitter.SseEventBuilder.class));

        sseService.getSseEmitters().put(clientId, emitter);

        // Envoyer un message
        String message = "Test message";
        sseService.sendEvent(clientId, "message", message);

        // Vérifier que l'émetteur a été supprimé après l'erreur
        assertFalse(sseService.getSseEmitters().containsKey(clientId));
        verify(emitter, times(1)).complete();
    }

    @Test
    void testBroadcastMessage() throws IOException, InterruptedException {
        // Ajouter plusieurs SseEmitters
        SseEmitter emitter1 = mock(SseEmitter.class);
        SseEmitter emitter2 = mock(SseEmitter.class);
        sseService.getSseEmitters().put("client1", emitter1);
        sseService.getSseEmitters().put("client2", emitter2);

        // Diffuser un message
        String message = "Broadcast message";
        sseService.broadcast("broadcast", message);

        // Capturer les arguments passés aux méthodes send
        ArgumentCaptor<SseEmitter.SseEventBuilder> captor1 = ArgumentCaptor.forClass(SseEmitter.SseEventBuilder.class);
        ArgumentCaptor<SseEmitter.SseEventBuilder> captor2 = ArgumentCaptor.forClass(SseEmitter.SseEventBuilder.class);

        // Vérifier que chaque émetteur a reçu le message
        verify(emitter1, times(1)).send(captor1.capture());
        verify(emitter2, times(1)).send(captor2.capture());

        // Extraire les données des événements capturés pour les deux émetteurs
        Set<ResponseBodyEmitter.DataWithMediaType> eventData1 = captor1.getValue().build();
        Set<ResponseBodyEmitter.DataWithMediaType> eventData2 = captor2.getValue().build();

        // Vérifier que le message est présent dans les données pour emitter1
        assertTrue(eventData1.stream().anyMatch(data -> message.equals(data.getData())), "Message non trouvé pour emitter1");

        // Vérifier que le message est présent dans les données pour emitter2
        assertTrue(eventData2.stream().anyMatch(data -> message.equals(data.getData())), "Message non trouvé pour emitter2");
    }


    @Test
    void testBroadcastHandlesIOException() throws IOException {
        // Ajouter un SseEmitter qui fonctionne et un autre qui échoue
        SseEmitter emitter1 = mock(SseEmitter.class);
        SseEmitter emitter2 = mock(SseEmitter.class);

        // Simuler une IOException pour emitter2
        doThrow(new IOException("Simulated error")).when(emitter2).send(any(SseEmitter.SseEventBuilder.class));

        // Ajouter les emitters dans le service
        sseService.getSseEmitters().put("client1", emitter1);
        sseService.getSseEmitters().put("client2", emitter2);

        // Diffuser un message
        String message = "Broadcast message";
        sseService.broadcast("broadcast", message);

        // Capturer l'argument passé à emitter1
        ArgumentCaptor<SseEmitter.SseEventBuilder> captor1 = ArgumentCaptor.forClass(SseEmitter.SseEventBuilder.class);
        verify(emitter1, times(1)).send(captor1.capture());

        // Vérifier que le premier émetteur a reçu le message correctement
        Set<ResponseBodyEmitter.DataWithMediaType> eventData1 = captor1.getValue().build();
        assertTrue(eventData1.stream().anyMatch(data -> message.equals(data.getData())), "Message non trouvé pour emitter1");
        await().atMost(10, TimeUnit.SECONDS).until(() -> !sseService.getSseEmitters().containsKey("client2"));
        // Vérifier que le deuxième émetteur a échoué et a été supprimé
        assertFalse(sseService.getSseEmitters().containsKey("client2"), "Emitter2 aurait dû être supprimé après l'erreur");
        verify(emitter2, times(1)).send(any(SseEmitter.SseEventBuilder.class));
        verify(emitter2, times(1)).complete();
    }

}
