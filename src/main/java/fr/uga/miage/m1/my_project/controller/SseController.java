package fr.uga.miage.m1.my_project.controller;

import fr.uga.miage.m1.my_project.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;


    // Endpoint pour abonner un client
    @GetMapping("/subscribe/{clientId}")
    public ResponseEntity<SseEmitter> subscribe(@PathVariable String clientId) {
        return ResponseEntity.ok(sseService.addSseEmitter(clientId));
    }

    // Endpoint pour envoyer un message à un autre client
    @PostMapping("/send/{clientId}")
    public ResponseEntity<String> sendMessage(@PathVariable String clientId, @RequestBody String message) {
        sseService.sendMessage(clientId, message);
        return ResponseEntity.ok("Si le client était connecté, le message a été envoyé.");
    }

    // Endpoint pour envoyer un message global
    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcast(@RequestBody String message) {
        sseService.broadcast(message);
        return ResponseEntity.ok("Message diffusé à tous les clients.");
    }
}
