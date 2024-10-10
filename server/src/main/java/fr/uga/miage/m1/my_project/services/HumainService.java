package fr.uga.miage.m1.my_project.services;

import fr.uga.miage.m1.my_project.enums.EtatRencontre;
import fr.uga.miage.m1.my_project.models.*;
import fr.uga.miage.m1.my_project.repositories.JoueurRepository;
import fr.uga.miage.m1.my_project.repositories.RencontreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HumainService {

    @Autowired
    private JoueurRepository joueurRepository;

    @Autowired
    private RencontreRepository rencontreRepository;


    public void choisirStrategieAutomatique(Long joueurId) {
        JoueurEntity joueur = joueurRepository.findById(joueurId)
                .orElseThrow(() -> new EntityNotFoundException("Joueur non trouvé"));

        StrategieEntity strategie = new ToujoursCoopererEntity();
        joueur.setStrategieAutomatiques(List.of(strategie));
        joueurRepository.save(joueur);
    }

    public void abandonner(Long joueurId, Long rencontreId) {
        JoueurEntity joueur = joueurRepository.findById(joueurId)
                .orElseThrow(() -> new EntityNotFoundException("Joueur non trouvé"));

        RencontreEntity rencontre = rencontreRepository.findById(rencontreId)
                .orElseThrow(() -> new EntityNotFoundException("rencontre non trouvé"));
                ;
        // Logique pour abandonner la rencontre
        this.choisirStrategieAutomatique(joueurId);

        RobotEntity robot = new RobotEntity();
        robot.setId(joueur.getId());
        robot.setNom(joueur.getNom() + "_Robot");
        robot.setScore(joueur.getScore());
        robot.setStrategieAutomatiques(joueur.getStrategieAutomatiques());

        // Remplacer le joueur dans la rencontre
        if (rencontre.getJoueur1().equals(joueur)) {
            rencontre.setJoueur1(robot);
        } else if (rencontre.getJoueur2().equals(joueur)) {
            rencontre.setJoueur2(robot);
        } else {
            throw new IllegalArgumentException("Le joueur humain n'est pas dans cette rencontre.");
        }
        // Par exemple, remplacer le joueur humain par un robot avec une stratégie automatique
    }

    public RencontreEntity initier(Long joueurId) {
        JoueurEntity joueur = joueurRepository.findById(joueurId)
                .orElseThrow(() -> new EntityNotFoundException("Joueur non trouvé"));

        RencontreEntity rencontre = new RencontreEntity();
        rencontre.setJoueurInitie(joueur);
        // logique a mettre en place
        rencontre.setNombreTours(10);
        rencontre.setEtat(EtatRencontre.EN_ATTENTE);
        rencontreRepository.save(rencontre);
        return rencontre;
    }

    public void rejoindre(Long joueurId, Long rencontreId) {
        JoueurEntity joueur = joueurRepository.findById(joueurId)
                .orElseThrow(() -> new EntityNotFoundException("Joueur non trouvé"));

        RencontreEntity rencontre = rencontreRepository.findById(rencontreId)
                .orElseThrow(() -> new EntityNotFoundException("Rencontre non trouvée"));

        if (rencontre.getEtat() == EtatRencontre.EN_ATTENTE) {
            // Ajouter le joueur à la rencontre
            rencontre.setJoueurRejoint(joueur);
            rencontre.setEtat(EtatRencontre.EN_COURS);
            rencontreRepository.save(rencontre);
        } else {
            throw new IllegalStateException("La rencontre n'est pas disponible pour être rejointe.");
        }
    }
}

