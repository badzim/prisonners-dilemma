package fr.uga.miage.m1.my_project.core.port.input;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import fr.uga.miage.m1.my_project.web.restapi.response.RencontreResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/rencontre")
public interface RencontreControllerPort {


    @PostMapping("/initier")
    ResponseEntity<String> initierRencontre(@RequestParam String clientId, @RequestParam int nombreTours);

    @PostMapping("/rejoindre")
    void rejoindreRencontre(@RequestParam String clientId, @RequestParam String idRencontre);

    @GetMapping("/disponibles")
    ResponseEntity<List<RencontreResponse>> getRencontresDisponibles();

    @PostMapping("/play/choix")
    ResponseEntity<Void> envoyerChoix(
            @RequestParam String clientId,
            @RequestParam TYPE_ACTION action,
            @RequestParam(required = false) TYPE_STRATEGIE strategie,
            @RequestParam (required = false) String groupId);


    }