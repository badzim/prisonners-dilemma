package fr.uga.miage.m1.my_project.components;


import fr.uga.miage.m1.my_project.models.JoueurEntity;
import fr.uga.miage.m1.my_project.repositories.JoueurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JoueurComponent {
    private final JoueurRepository joueurRepository;

    public JoueurEntity getJoueurById(Long joueurId) /*throws JoueurNotFoundException*/ {
        return joueurRepository.findById(joueurId).orElseThrow(null);
    }

    public List<JoueurEntity> getAllJoueurs() {
        return joueurRepository.findAll();
    }
}
