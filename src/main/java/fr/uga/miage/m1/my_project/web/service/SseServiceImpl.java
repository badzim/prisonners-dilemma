package fr.uga.miage.m1.my_project.web.service;

import fr.uga.miage.m1.my_project.core.exception.rest.ClientIdUsedRestException;
import fr.uga.miage.m1.my_project.core.port.output.EventEmitter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@Primary
public class SseServiceImpl implements EventEmitter {

    // il me faut sseRepository ici hmm

    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    // Ajouter un SseEmitter pour un client donné
    public SseEmitter addSseEmitter(String clientId) {
        log.info("Ajout d'un SseEmitter pour le client {}", clientId);

        // Vérifier si un émetteur existe déjà
        if (sseEmitters.containsKey(clientId)) {
            log.error("Un SseEmitter existe déjà pour le client {}. Connexion refusée.", clientId);
            throw new ClientIdUsedRestException("Un client avec cet ID est déjà connecté : ",  clientId);
        }

        SseEmitter emitter = createSseEmitter(clientId);

            sseEmitters.put(clientId, emitter);
        return emitter;
    }

    @Override
    public Map<String, SseEmitter> getSseEmitters() {
        return sseEmitters;
    }

    private SseEmitter createSseEmitter(String clientId) {
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


    // Diffuser un message à tous les clients connectés
    public void broadcast(String eventName, String data) {
        sseEmitters.forEach((clientId, emitter) -> sendEvent(clientId, eventName, data));
    }

    /**
     * Vérifie tous les clients déconnectés et effectue un broadcast avec les IDs déconnectés.
     */
    public List<String> handleDisconnectedPlayers() {
        List<String> disconnectedClients = new ArrayList<>();

        // Identifier les clients déconnectés

        sseEmitters.forEach((clientId, emitter) -> {
            try {
                if (!isEmitterActive(emitter)) {
                    disconnectedClients.add(clientId);
                }
            } catch (IOException e) {
                log.warn("IOException occurred while sending to disconnected ppl : {}", e.getMessage());
            }
        });



        return disconnectedClients;
    }

    /**
     * Vérifie si un SseEmitter est actif.
     */
    private boolean isEmitterActive(SseEmitter emitter) throws IOException{

            try {
                // Attempt a lightweight "ping" to check if the connection is still open
                emitter.send(SseEmitter.event().name("ping").data("test"));
            } catch (IOException | IllegalStateException e) {
                // This occurs if the emitter is in an invalid state (e.g., already completed)
                log.warn("Emitter is in an invalid state: {}", e.getMessage());
                return false;
            } catch (Exception e) {
                // IOException indicates a broken connection or client disconnect
                log.warn("IOException occurred while sending to emitter: {}", e.getMessage());
                return false;
            }

        return true;
    }

    /**
     * Envoie un événement à un client spécifique.
     */
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
            handleSendError(clientId, e, "Erreur lors de l'envoi du message au client");
        } catch (Exception e) {
            handleSendError(clientId, e, "Erreur inconnue");
        }
    }

    /**
     * Gère une erreur d'envoi et retire l'émetteur du client en toute sécurité.
     */
    private void handleSendError(String clientId, Exception e, String logMessage) {
        log.error("{} '{}' : {}", logMessage, clientId, e.getMessage());
        safelyRemoveEmitter(clientId);
    }

    /**
     * Supprime un émetteur en toute sécurité.
     */
    public void safelyRemoveEmitter(String clientId) {
        try {
            SseEmitter emitter = sseEmitters.get(clientId);
            sseEmitters.remove(clientId);
            if (emitter != null) {
                emitter.complete();
            }
        } catch (Exception ex) {
            log.error("Erreur inconnue lors de la suppression de l'émetteur '{}' : {}", clientId, ex.getMessage());
        }
    }

}
