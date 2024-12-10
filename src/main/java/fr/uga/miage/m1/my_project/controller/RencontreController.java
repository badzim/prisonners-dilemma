package fr.uga.miage.m1.my_project.controller;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.model.enums.TypeStrategie;
import fr.uga.miage.m1.my_project.restapi.dto.RencontreDto;
import fr.uga.miage.m1.my_project.service.RencontreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<RencontreDto>> getRencontresDisponibles() {
        // Récupérer les rencontres disponibles et les convertir en DTOs
        return ResponseEntity.ok(rencontreService.getRencontresDisponibles());
    }

    @PostMapping("/play/choix")
    public ResponseEntity<Void> envoyerChoix(
            @RequestParam String clientId,
            @RequestParam TypeAction action,
            @RequestParam(required = false) TypeStrategie strategie,
            @RequestParam (required = false) String groupId) {
        rencontreService.enregistrerChoix(clientId, action, strategie, groupId);
        return ResponseEntity.ok().build();
    }
}
