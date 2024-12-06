package fr.uga.miage.m1.my_project.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PingSchedulerService {
    private final SseService sseService;


    @PostConstruct
    public void startScheduler() {
        ScheduledExecutorService pingScheduler = Executors.newSingleThreadScheduledExecutor();
        pingScheduler.scheduleAtFixedRate(() -> {
            try {
                sseService.handleDisconnectedPlayers();
            } catch (Exception e) {
                log.error("Exception dans le pingScheduler lors de l'appel à broadcast", e);
            }
        }, 1, 5, TimeUnit.SECONDS);
    }
}
