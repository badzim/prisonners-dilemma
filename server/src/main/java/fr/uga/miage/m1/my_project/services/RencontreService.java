package fr.uga.miage.m1.my_project.services;

import fr.uga.miage.m1.my_project.enums.EtatRencontre;
import fr.uga.miage.m1.my_project.enums.TypeAction;
import fr.uga.miage.m1.my_project.models.*;
import fr.uga.miage.m1.my_project.repositories.RencontreRepository;
import fr.uga.miage.m1.my_project.repositories.TourRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RencontreService {

    @Autowired
    private RencontreRepository rencontreRepository;

    @Autowired
    private TourService tourService;

    @Autowired
    private TourRepository tourRepository;


    public RencontreEntity getById(Long rencontreId) {
        return rencontreRepository.findById(rencontreId)
                .orElseThrow(() -> new EntityNotFoundException("Rencontre non trouvée"));
    }


    public void demarrerRencontre(Long rencontreId) {
        RencontreEntity rencontre = rencontreRepository.findById(rencontreId)
                .orElseThrow(() -> new EntityNotFoundException("Rencontre non trouvée"));
        if (rencontre.getEtat() == EtatRencontre.EN_ATTENTE && rencontre.getJoueurRejoint() != null) {
            rencontre.setEtat(EtatRencontre.EN_COURS);
            rencontreRepository.save(rencontre);
        } else {
            throw new IllegalStateException("La rencontre ne peut pas être démarrée");
        }
    }

    public void jouerTour(Long rencontreId) {
        RencontreEntity rencontre = rencontreRepository.findById(rencontreId)
                .orElseThrow(() -> new EntityNotFoundException("Rencontre non trouvée"));

        if (rencontre.getEtat() == EtatRencontre.EN_COURS) {
            TourEntity tour = tourService.creerTour(rencontre);
            // Collecter les actions des joueurs
            TypeAction actionJoueur1 = obtenirAction(rencontre.getJoueur1());
            TypeAction actionJoueur2 = obtenirAction(rencontre.getJoueur2());

            tour.setActionJoueur1(actionJoueur1);
            tour.setActionJoueur2(actionJoueur2);

            // Calculer les scores
            tourService.calculerScore(tour);

            // Vérifier si la rencontre est terminée
            if (rencontre.getTours().size() >= rencontre.getNombreTours()) {
                rencontre.setEtat(EtatRencontre.TERMINEE);
            }

            rencontreRepository.save(rencontre);
        } else {
            throw new IllegalStateException("La rencontre n'est pas en cours");
        }
    }


    private TypeAction obtenirAction(JoueurEntity joueur) {
        if (joueur instanceof HumainEntity) {
            // Logique pour obtenir l'action du joueur humain
            throw new UnsupportedOperationException("Action du joueur humain non implémentée");
        } else if (joueur instanceof RobotEntity) {
            RobotEntity robot = (RobotEntity) joueur;

            // Appeler la méthode getAction du service de stratégie
            return TypeAction.COOPERER;
        } else {
            // Autres cas si nécessaire
            return TypeAction.COOPERER; // Action par défaut
        }
    }
}
