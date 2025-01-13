package fr.uga.miage.m1.my_project.core.domain.service;

import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;
import fr.uga.miage.m1.my_project.core.port.input.JoueurServicePort;
import fr.uga.miage.m1.my_project.core.port.output.JoueurRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service // dépendance faible qui n'affecte pas la logique métier, on se permet donc d'introduire cette dépendance à notre archi.
@Primary
@Data
@RequiredArgsConstructor
public class JoueurService implements JoueurServicePort {

    @Qualifier("inMemoryJoueurRepository")
    private final JoueurRepository joueurRepository;

    /**
     * Récupérer un joueur par son ID.
     */
    public Joueur getJoueurById(String clientId) {
        return joueurRepository.findById(clientId);
    }
}
