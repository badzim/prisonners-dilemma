package fr.uga.miage.m1.my_project.endpoints;


import fr.uga.miage.m1.my_project.responses.JoueurResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/joueurs")
public interface JoueurEndpoints {



    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{joueurId}/rejoindre/{rencontreId}")
    ResponseEntity<?> rejoindreRencontre(@RequestParam Long joueurId, @RequestParam Long rencontreId);

    @PostMapping("/{joueurId}/initier")
    ResponseEntity<?> initierRencontre(@RequestParam Long joueurId);

    @PostMapping("/{joueurId}/abandonner/{rencontreId}")
    ResponseEntity<?> abandonnerRencontre(@PathVariable Long joueurId, @PathVariable Long rencontreId, @RequestParam Long strategieId);

   @GetMapping("/affichage/des/joueurs")
   List<JoueurResponse> getJoueurs();
}