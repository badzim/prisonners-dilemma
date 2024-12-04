package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.exception.rest.ClientIdUsedRestException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@Service
public class SseService {

    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final ScheduledExecutorService pingScheduler = Executors.newSingleThreadScheduledExecutor();


    SseService() {
        pingScheduler.scheduleAtFixedRate(() -> {
            try {
                broadcast("ping", "ping");
            } catch (Exception e) {
                log.error("Exception dans le pingScheduler lors de l'appel à broadcast", e);
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

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
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
                log.debug("Message envoyé au client {}", clientId);
            } catch (IOException | IllegalStateException e) {
                log.error("Erreur lors de l'envoi du message au client '{}' : {}", clientId, e.getMessage());
                try {
                    sseEmitters.remove(clientId);
                    emitter.complete();
                } catch (Exception ex) {
                    log.error("Erreur inconnu 1 '{}' : {}", clientId, e.getMessage());
                }

            } catch (Exception e) {
                log.error("Erreur inconnu '{}' : {}", clientId, e.getMessage());
                try {
                    sseEmitters.remove(clientId);
                    emitter.complete();
                }
                catch (Exception ex) {
                    log.error("Erreur inconnu 2 '{}' : {}", clientId, e.getMessage());
                }
            }
        } else {
            log.warn("Impossible d'envoyer un message : client {} introuvable", clientId);
        }
    }



    // Diffuser un message à tous les clients connectés
    public void broadcast(String eventName, String data) {
        sseEmitters.forEach((clientId, emitter) -> {
            sendEvent(clientId, eventName, data);
        });
    }
}
