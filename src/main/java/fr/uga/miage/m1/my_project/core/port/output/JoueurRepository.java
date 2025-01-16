package fr.uga.miage.m1.my_project.core.port.output;

import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;

import java.util.Map;

public interface JoueurRepository {

    Joueur findById(String id);

    Map<String, Joueur> findAll();

    Joueur save(Joueur joueur);
}
