package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.exception.rest.InvalidActionRestException;
import fr.uga.miage.m1.my_project.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.model.joueur.Humain;
import fr.uga.miage.m1.my_project.model.joueur.Joueur;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class JoueurService {

    // Map des joueurs connectés, avec leur ID comme clé
    private final Map<String, Joueur> joueurs = new ConcurrentHashMap<>();

    /**
     * Récupérer un joueur par son ID.
     */
    public Joueur getHumain(String clientId) {
        return joueurs.computeIfAbsent(clientId, k -> {
            Joueur joueur1 = new Humain(clientId, clientId);
            joueur1.setEtat(EtatJoueur.EN_MENU);
            return joueur1;
        });
    }

    /**
     * Vérifier si un joueur peut initier une rencontre.
     */
    public void joueurEstEnMenu(String clientId) {
        Joueur joueur = getHumain(clientId);
        if (joueur.getEtat() != EtatJoueur.EN_MENU) {
            throw new InvalidActionRestException("Le joueur doit être dans le menu");
        }
    }
}
