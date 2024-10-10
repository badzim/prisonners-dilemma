package fr.uga.miage.m1.my_project.endpoints;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rencontres")
public interface RencontreEndpoints {

    @ResponseStatus(HttpStatus.OK)

    @GetMapping("/{rencontreId}")
    ResponseEntity<?> getRencontre(@RequestParam Long rencontreId);

    @PostMapping("/{rencontreId}/demarrer")
    ResponseEntity<?> demarrerRencontre(@RequestParam Long rencontreId);

    @PostMapping("/{rencontreId}/jouer-tour")
    ResponseEntity<?> jouerTour(@RequestParam Long rencontreId);

}
