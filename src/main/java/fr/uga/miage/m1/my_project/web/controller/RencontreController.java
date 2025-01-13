package fr.uga.miage.m1.my_project.web.controller;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import fr.uga.miage.m1.my_project.web.restapi.response.RencontreResponse;
import fr.uga.miage.m1.my_project.core.domain.service.RencontreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// this is an adapter
@RestController
@RequestMapping("/api/rencontre")
@RequiredArgsConstructor
public class RencontreController {

    private final RencontreService rencontreService;


    @PostMapping("/initier")
    public ResponseEntity<String> initierRencontre(@RequestParam String clientId, @RequestParam int nombreTours) {
        boolean success = rencontreService.initierRencontre(clientId, nombreTours);

        if (success) {
            return ResponseEntity.ok("Rencontre initiée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client non connecté ou erreur lors de l'initiation.");
        }
    }

    @PostMapping("/rejoindre")
    public void rejoindreRencontre(@RequestParam String clientId, @RequestParam String idRencontre) {
        rencontreService.rejoindreRencontre(clientId, idRencontre);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<RencontreResponse>> getRencontresDisponibles() {
        // Récupérer les rencontres disponibles et les convertir en DTOs
        return ResponseEntity.ok(rencontreService.getRencontresEnAttente());
    }

    @PostMapping("/play/choix")
    public ResponseEntity<Void> envoyerChoix(
            @RequestParam String clientId,
            @RequestParam TYPE_ACTION action,
            @RequestParam(required = false) TYPE_STRATEGIE strategie,
            @RequestParam (required = false) String groupId) {
        rencontreService.enregistrerChoix(clientId, action, strategie, groupId);
        return ResponseEntity.ok().build();
    }
}
