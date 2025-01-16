package fr.uga.miage.m1.my_project.core.port.input;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RequestMapping("/api/sse")
public interface SseControllerPort {

    @GetMapping("/subscribe/{clientId}")
    ResponseEntity<SseEmitter> subscribe(@PathVariable String clientId);

    @PostMapping("/send/{clientId}")
    ResponseEntity<String> sendMessage(@PathVariable String clientId, @RequestBody String message);

    @PostMapping("/broadcast")
    ResponseEntity<String> broadcast(@RequestBody String message);
}


