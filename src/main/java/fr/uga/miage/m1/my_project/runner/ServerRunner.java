package fr.uga.miage.m1.my_project.runner;

import fr.uga.miage.m1.my_project.controller.SseController;
import fr.uga.miage.m1.my_project.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("server")
public class ServerRunner implements CommandLineRunner {

    private final ServerService serverService;
    private final SseController sseController;
    @Override
    public void run(String... args) throws Exception {
        serverService.start();
    }
}
