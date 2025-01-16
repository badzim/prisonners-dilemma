package fr.uga.miage.m1.my_project.core.port.input;

import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;
import fr.uga.miage.m1.my_project.core.domain.model.Tour;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;

public interface TourServicePort {
    void calculerScore(Tour tour);
    void setActionJoueur(Joueur joueur, Rencontre rencontre, TYPE_ACTION actionJoueur);
}
