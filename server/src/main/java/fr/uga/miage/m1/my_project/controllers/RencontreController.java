package fr.uga.miage.m1.my_project.controllers;


import fr.uga.miage.m1.my_project.endpoints.RencontreEndpoints;
import fr.uga.miage.m1.my_project.models.RencontreEntity;
import fr.uga.miage.m1.my_project.services.RencontreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RencontreController implements RencontreEndpoints {


    private final RencontreService rencontreService;


    // To fix
    @Override
    public ResponseEntity<RencontreEntity> getRencontre(Long rencontreId) {
        RencontreEntity rencontre = rencontreService.getById(rencontreId);
        return ResponseEntity.ok(rencontre);
    }

    @Override
    public ResponseEntity<?> demarrerRencontre( Long rencontreId) {
        rencontreService.demarrerRencontre(rencontreId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> jouerTour(Long rencontreId) {
        rencontreService.jouerTour(rencontreId);
        return ResponseEntity.ok().build();
    }
}
