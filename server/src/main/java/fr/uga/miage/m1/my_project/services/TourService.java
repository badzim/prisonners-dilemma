package fr.uga.miage.m1.my_project.services;

import fr.uga.miage.m1.my_project.enums.TypeAction;
import fr.uga.miage.m1.my_project.models.JoueurEntity;
import fr.uga.miage.m1.my_project.models.RencontreEntity;
import fr.uga.miage.m1.my_project.models.TourEntity;
import fr.uga.miage.m1.my_project.repositories.JoueurRepository;
import fr.uga.miage.m1.my_project.repositories.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourService {

    // Coefficients selon le dilemme du prisonnier
    private final int T = 5;  // Trahir et l'autre coopère
    private final int D = 0;  // Coopérer et l'autre trahit
    private final int C = 3;  // Les deux coopèrent
    private final int P = 1;  // Les deux trahissent


    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private JoueurRepository joueurRepository;

    public TourEntity creerTour(RencontreEntity rencontre) {
        TourEntity tour = new TourEntity();
        tour.setRencontre(rencontre);
        tourRepository.save(tour);
        rencontre.getTours().add(tour);
        return tour;
    }

    public void calculerScore(TourEntity tour) {
        // Créer une matrice des scores pour toutes les combinaisons possibles
        int[][] scores = {
                {C, C}, // [c, c] : les deux coopèrent
                {T, D}, // [t, c] : Joueur 1 trahit, Joueur 2 coopère
                {D, T}, // [c, t] : Joueur 1 coopère, Joueur 2 trahit
                {P, P}  // [t, t] : les deux trahissent
        };

        int i = tour.getActionJoueur1() == TypeAction.COOPERER ? 0 : 1;
        int j = tour.getActionJoueur2() == TypeAction.COOPERER ? 0 : 1;

        // Met à jour les scores des deux joueurs dans la rencontre
        JoueurEntity joueur1 = tour.getRencontre().getJoueur1();
        JoueurEntity joueur2 = tour.getRencontre().getJoueur2();

        joueur1.setScore(joueur1.getScore() + scores[i * 2 + j][0]);
        joueur2.setScore(joueur2.getScore() + scores[i * 2 + j][1]);

        joueurRepository.save(joueur1);
        joueurRepository.save(joueur2);
    }
}
