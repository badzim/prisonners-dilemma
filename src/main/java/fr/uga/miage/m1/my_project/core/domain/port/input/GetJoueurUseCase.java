package fr.uga.miage.m1.my_project.core.domain.port.input;

import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;

public interface GetJoueurUseCase {
    Joueur getJoueurById(String id);
}
