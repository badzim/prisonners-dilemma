package fr.uga.miage.m1.my_project.web.service;

import fr.uga.miage.m1.my_project.core.port.output.EventEmitter;
import fr.uga.miage.m1.my_project.core.port.output.TaskScheduler;
import fr.uga.miage.m1.my_project.service.RencontreService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Data
@Primary
public class PingSchedulerServiceImpl implements TaskScheduler {

    @Qualifier("sseServiceImpl")
    private final EventEmitter sseService;
    private ScheduledExecutorService pingScheduler;
    private final RencontreService rencontreService;

    @PostConstruct
    public void startScheduler() {
        pingScheduler = Executors.newSingleThreadScheduledExecutor();
        pingScheduler.scheduleAtFixedRate(() -> {
            try {
                List<String> disconnectedClients = sseService.handleDisconnectedPlayers();
                disconnectedClients.forEach(clientId -> {
                    try {
                        sseService.safelyRemoveEmitter(clientId);
                        rencontreService.handleDesconnectedPlayer(clientId);
                    } catch (Exception e) {
                        log.error("Error while handling disconnected client: {}", clientId, e);
                    }
                });

                if (!disconnectedClients.isEmpty()) {
                    tryToBroadcast(disconnectedClients);
                }
            } catch (Exception e) {
                log.error("Exception in pingScheduler during call to handleDisconnectedPlayers", e);
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    private void tryToBroadcast(List<String> disconnectedClients) {
        try {
            sseService.broadcast("broadcast-player-disconnected", String.join(",", disconnectedClients));
        } catch (Exception e) {
            log.error("Error while broadcasting to disconnected clients: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void stopScheduler() {
        if (pingScheduler != null && !pingScheduler.isShutdown()) {
            pingScheduler.shutdown();
        }
    }
}
