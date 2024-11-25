package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.exception.rest.ClientIdUsedRestException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@Data
public class SseService {

    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    // Ajouter un SseEmitter pour un client donné
    public SseEmitter addSseEmitter(String clientId) {
        log.info("Ajout d'un SseEmitter pour le client {}", clientId);

        // Vérifier si un émetteur existe déjà
        if (sseEmitters.containsKey(clientId)) {
            log.error("Un SseEmitter existe déjà pour le client {}. Connexion refusée.", clientId);
            throw new ClientIdUsedRestException("Un client avec cet ID est déjà connecté : ",  clientId);
        }

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitters.put(clientId, emitter);

        // Nettoyage en cas de déconnexion
        emitter.onCompletion(() -> {
            log.info("SseEmitter pour le client {} complété.", clientId);
            sseEmitters.remove(clientId);
        });
        emitter.onTimeout(() -> {
            log.warn("SseEmitter pour le client {} expiré.", clientId);
            sseEmitters.remove(clientId);
        });
        emitter.onError(e -> {
            log.error("Erreur sur le SseEmitter du client {} : {}", clientId, e.getMessage());
            sseEmitters.remove(clientId);
        });

        return emitter;
    }

    public void sendMessage(String clientId, String message) {
        SseEmitter emitter = sseEmitters.get(clientId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("message").data(message));
                log.debug("Message envoyé au client {}", clientId);
            } catch (IOException e) {
                log.error("Erreur lors de l'envoi du message au client {} : {}", clientId, e.getMessage());
                emitter.completeWithError(e);
                sseEmitters.remove(clientId);
            }
        } else {
            log.warn("Impossible d'envoyer un message : client {} introuvable", clientId);
        }
    }

    // Diffuser un message à tous les clients connectés
    public void broadcast(String message) {
        sseEmitters.forEach((clientId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("broadcast").data(message));
            } catch (IOException e) {
                emitter.completeWithError(e);
                sseEmitters.remove(clientId);
            }
        });
    }
}
