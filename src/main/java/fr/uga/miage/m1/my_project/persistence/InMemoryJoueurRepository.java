package fr.uga.miage.m1.my_project.persistence;

import fr.uga.miage.m1.my_project.core.domain.model.joueur.Humain;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;
import fr.uga.miage.m1.my_project.core.domain.port.output.JoueurRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryJoueurRepository implements JoueurRepository {
    private final Map<String, Joueur> joueurs = new ConcurrentHashMap<>();


    @Override
    public Joueur findById(String clientId) {
        return joueurs.computeIfAbsent(clientId, k ->
                new Humain(clientId, clientId)
        );
    }

    @Override
    public Map<String ,Joueur> findAll() {
        return joueurs;
    }

    @Override
    public Joueur save(Joueur joueur) {
        joueurs.put(joueur.getId(), joueur);
        return joueur;
    }
}
