package fr.uga.miage.m1.my_project.core.port.output;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;

public interface EventEmitter {
    // Ajouter un SseEmitter pour un client donné
    SseEmitter addSseEmitter(String clientId);

    // Diffuser un message à tous les clients connectés
    void broadcast(String eventName, String data);

    // Vérifie tous les clients déconnectés et retourne leurs IDs
    List<String> handleDisconnectedPlayers();

    // Envoie un événement à un client spécifique
    void sendEvent(String clientId, String eventName, String data);

    // Supprime un émetteur en toute sécurité
    void safelyRemoveEmitter(String clientId);
}