package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.exception.rest.ClientIdUsedRestException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
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

        SseEmitter emitter = getSseEmitter(clientId);

        sseEmitters.put(clientId, emitter);
        return emitter;
    }

    private SseEmitter getSseEmitter(String clientId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
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
        log.info("Callback onCompletion enregistré pour {}", clientId);
        return emitter;
    }

    public void sendEvent(String clientId, String eventName, String data) {
        SseEmitter emitter = sseEmitters.get(clientId);
        if (emitter == null) {
            log.warn("Impossible d'envoyer un message : client {} introuvable", clientId);
            return;
        }
        try {
            emitter.send(SseEmitter.event().name(eventName).data(data));
            log.debug("Message envoyé au client {}", clientId);
        } catch (IOException | IllegalStateException e) {

            handleSendError(clientId, emitter, e, "Erreur lors de l'envoi du message au client");
        } catch (Exception e) {
            handleSendError(clientId, emitter, e, "Erreur inconnue");
        }
    }

    private void handleSendError(String clientId, SseEmitter emitter, Exception e, String logMessage) {
        log.error("{} '{}' : {}", logMessage, clientId, e.getMessage());
        safelyRemoveEmitter(clientId, emitter);
    }

    private void safelyRemoveEmitter(String clientId, SseEmitter emitter) {
        try {
            sseEmitters.remove(clientId);
            emitter.complete();
        } catch (Exception ex) {
            log.error("Erreur inconnue lors de la suppression de l'émetteur '{}' : {}", clientId, ex.getMessage());
        }
    }

    // Diffuser un message à tous les clients connectés
    public void broadcast(String eventName, String data) {
        sseEmitters.forEach((clientId, emitter) -> {
            sendEvent(clientId, eventName, data);
        });
    }
}
