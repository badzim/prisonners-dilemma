package fr.uga.miage.m1.my_project.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Data
public class PingSchedulerService {
    private final SseService sseService;


    private ScheduledExecutorService pingScheduler;

    @PostConstruct
    public void startScheduler() {
        pingScheduler = Executors.newSingleThreadScheduledExecutor();
        pingScheduler.scheduleAtFixedRate(() -> {
            try {
                sseService.handleDisconnectedPlayers();
            } catch (Exception e) {
                log.error("Exception in pingScheduler during call to broadcast", e);
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void stopScheduler() {
        if (pingScheduler != null && !pingScheduler.isShutdown()) {
            pingScheduler.shutdown();
        }
    }
}
