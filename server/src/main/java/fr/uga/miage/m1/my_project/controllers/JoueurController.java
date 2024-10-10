package fr.uga.miage.m1.my_project.controllers;

import fr.uga.miage.m1.my_project.endpoints.JoueurEndpoints;
import fr.uga.miage.m1.my_project.models.RencontreEntity;
import fr.uga.miage.m1.my_project.repositories.JoueurRepository;
import fr.uga.miage.m1.my_project.responses.JoueurResponse;
import fr.uga.miage.m1.my_project.responses.RencontreResponse;
import fr.uga.miage.m1.my_project.services.HumainService;
import fr.uga.miage.m1.my_project.services.JoueurService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class JoueurController implements JoueurEndpoints {


    private final HumainService humainService;

    private final JoueurService joueurService;

    @Autowired
    private final JoueurRepository joueurRepository;




    @Override
    public ResponseEntity<RencontreEntity> initierRencontre( Long joueurId) {
        RencontreEntity rencontre = humainService.initier(joueurId);
        return ResponseEntity.ok(rencontre);
    }

    @Override
    public ResponseEntity<?> rejoindreRencontre( Long joueurId,  Long rencontreId) {
        humainService.rejoindre(joueurId, rencontreId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> abandonnerRencontre( Long joueurId,  Long rencontreId,  Long strategieId) {
        humainService.abandonner(joueurId, rencontreId);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<JoueurResponse> getJoueurs() {
        return joueurService.getAllJoueurs();
    }
}
