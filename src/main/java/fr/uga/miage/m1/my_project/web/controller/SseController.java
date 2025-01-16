package fr.uga.miage.m1.my_project.web.controller;

import fr.uga.miage.m1.my_project.core.port.input.SseControllerPort;
import fr.uga.miage.m1.my_project.core.port.output.EventEmitter;
import fr.uga.miage.m1.my_project.web.service.SseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@Slf4j
@RequiredArgsConstructor
public class SseController implements SseControllerPort {

    @Qualifier("sseServiceImpl")
    private final EventEmitter sseServiceImpl;



    // Endpoint pour abonner un client
    public ResponseEntity<SseEmitter> subscribe( String clientId) {
        try {
            SseEmitter emitter = sseServiceImpl.addSseEmitter(clientId);
            log.info("Client {} abonné avec succès", clientId);
            sseServiceImpl.sendEvent(clientId, "ping", "ping");
            return ResponseEntity.ok(emitter);
        } catch (IllegalStateException e) {
            log.error("Opération asynchrone déjà commencée ou terminée pour le client {}: {}", clientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Conflit pour état invalide
        }
    }

    // Endpoint pour envoyer un message à un autre client
    public ResponseEntity<String> sendMessage(String clientId, String message) {
        sseServiceImpl.sendEvent(clientId, "message", message);
        return ResponseEntity.ok("Si le client était connecté, le message a été envoyé.");
    }

    // Endpoint pour envoyer un message global
    public ResponseEntity<String> broadcast(String message) {
        sseServiceImpl.broadcast("broadcast", message);
        return ResponseEntity.ok("Message diffusé à tous les clients.");
    }
}
