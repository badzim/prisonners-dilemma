package fr.uga.miage.m1.my_project.services;

import fr.uga.miage.m1.my_project.components.JoueurComponent;
import fr.uga.miage.m1.my_project.mappers.JoueurMapper;
import fr.uga.miage.m1.my_project.responses.JoueurResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JoueurService {

    private final JoueurComponent joueurComponent;
    private final JoueurMapper joueurMapper;

    public void jouer(Long joueurId) {
        // Implémentation pour le joueur humain
        // Ici, vous pouvez intégrer la logique pour obtenir l'action du joueur humain, par exemple via une interface utilisateur
        // Pour l'instant, nous allons lever une exception pour indiquer que c'est à implémenter
        throw new UnsupportedOperationException("La méthode jouer pour Humain doit être implémentée.");
    }

    public List<JoueurResponse> getAllJoueurs() {
        return joueurMapper.toResponses(joueurComponent.getAllJoueurs());
    }

}