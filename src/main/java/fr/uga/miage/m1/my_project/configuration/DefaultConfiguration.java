package fr.uga.miage.m1.my_project.configuration;

import fr.uga.miage.m1.my_project.core.domain.port.output.JoueurRepository;
import fr.uga.miage.m1.my_project.persistence.InMemoryJoueurRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultConfiguration {

    @Bean
    public JoueurRepository joueurRepository() {
        return new InMemoryJoueurRepository();
    }

}
