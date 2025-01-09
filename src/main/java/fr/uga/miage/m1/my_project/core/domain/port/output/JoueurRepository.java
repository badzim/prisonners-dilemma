package fr.uga.miage.m1.my_project.core.domain.port.output;

import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface JoueurRepository {

    Joueur findById(String id);

    Map<String, Joueur> findAll();

    Joueur save(Joueur joueur);
}
