package fr.uga.miage.m1.my_project.core.domain.service;

import fr.uga.miage.m1.my_project.core.domain.model.joueur.Humain;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;
import fr.uga.miage.m1.my_project.core.domain.port.input.GetJoueurUseCase;
import fr.uga.miage.m1.my_project.core.domain.port.output.JoueurRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@Data
@RequiredArgsConstructor
public class JoueurService implements GetJoueurUseCase  {

    private final JoueurRepository joueurRepository;

    /**
     * Récupérer un joueur par son ID.
     */
    public Joueur getJoueurById(String clientId) {
        return joueurRepository.findById(clientId);
    }
}
