package fr.uga.miage.m1.my_project.web.controller;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import fr.uga.miage.m1.my_project.core.port.input.RencontreControllerPort;
import fr.uga.miage.m1.my_project.core.port.input.RencontreServicePort;
import fr.uga.miage.m1.my_project.web.restapi.response.RencontreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// this is an adaptery
@RestController
@RequiredArgsConstructor
public class RencontreControllerImpl implements RencontreControllerPort {

    @Qualifier("rencontreService")
    private final RencontreServicePort rencontreService;


    public ResponseEntity<String> initierRencontre(String clientId, int nombreTours) {
        boolean success = rencontreService.initierRencontre(clientId, nombreTours);

        if (success) {
            return ResponseEntity.ok("Rencontre initiée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client non connecté ou erreur lors de l'initiation.");
        }
    }

    public void rejoindreRencontre(String clientId, String idRencontre) {
        rencontreService.rejoindreRencontre(clientId, idRencontre);
    }


    public ResponseEntity<List<RencontreResponse>> getRencontresDisponibles() {
        // Récupérer les rencontres disponibles et les convertir en DTOs
        return ResponseEntity.ok(rencontreService.getRencontresEnAttente());
    }


    public ResponseEntity<Void> envoyerChoix(
             String clientId,
             TYPE_ACTION action,
             TYPE_STRATEGIE strategie,
             String groupId) {
        rencontreService.enregistrerChoix(clientId, action, strategie, groupId);
        return ResponseEntity.ok().build();
    }
}
