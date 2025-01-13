package fr.uga.miage.m1.my_project.web.controller;

import fr.uga.miage.m1.my_project.web.service.SseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
@Slf4j
public class SseController {

    private final SseServiceImpl sseServiceImpl;



    // Endpoint pour abonner un client
    @GetMapping("/subscribe/{clientId}")
    public ResponseEntity<SseEmitter> subscribe(@PathVariable String clientId) {
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
    @PostMapping("/send/{clientId}")
    public ResponseEntity<String> sendMessage(@PathVariable String clientId, @RequestBody String message) {
        sseServiceImpl.sendEvent(clientId, "message", message);
        return ResponseEntity.ok("Si le client était connecté, le message a été envoyé.");
    }

    // Endpoint pour envoyer un message global
    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcast(@RequestBody String message) {
        sseServiceImpl.broadcast("broadcast", message);
        return ResponseEntity.ok("Message diffusé à tous les clients.");
    }
}
