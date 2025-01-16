package fr.uga.miage.m1.my_project.core.port.input;

import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;

public interface JoueurServicePort {
    Joueur getJoueurById(String id);
}
