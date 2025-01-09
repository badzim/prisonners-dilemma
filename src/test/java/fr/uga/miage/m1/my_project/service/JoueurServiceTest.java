package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.core.domain.port.output.JoueurRepository;
import fr.uga.miage.m1.my_project.core.domain.service.JoueurService;
import fr.uga.miage.m1.my_project.core.domain.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Humain;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JoueurServiceTest {

    @SpyBean
    private JoueurService joueurService;

    @SpyBean
    private JoueurRepository joueurRepository;


    @Test
    void testGetJoueur_NewJoueurCreated() {
        // Arrange
        String clientId = "client123";

        joueurRepository.findAll().clear();

        // Act
        Joueur result = joueurService.getJoueurById(clientId);

        // Assert
        assertNotNull(result, "Un joueur doit être créé si non existant.");
        assertTrue(result instanceof Humain, "Le joueur créé doit être de type Humain.");
        assertEquals(clientId, result.getId(), "L'ID du joueur doit correspondre au clientId.");
        assertEquals(EtatJoueur.EN_MENU, result.getEtat(), "Le joueur doit être initialement dans l'état EN_MENU.");
    }


    @Test
    void testGetJoueur_ExistingJoueurRetrieved() {
        // Arrange
        String clientId = "client123";
        Joueur existingJoueur = new Humain(clientId, "Nom Test");
        existingJoueur.setEtat(EtatJoueur.EN_ATTENTE);

        // Ajouter un joueur existant dans la map
        joueurRepository.save(existingJoueur);

        // Act
        Joueur result = joueurService.getJoueurById(clientId);

        // Assert
        assertNotNull(result, "Le joueur existant doit être retourné.");
        assertEquals(existingJoueur, result, "Le joueur retourné doit être le même que l'existant.");
    }

    @Test
    void testJoueurEstEnMenu_Success() {
        // Arrange
        String clientId = "client123";
        Joueur joueur = new Humain(clientId, "Nom Test");
        joueur.setEtat(EtatJoueur.EN_MENU);

        joueurRepository.save(joueur);

        // Act & Assert
        assertSame(EtatJoueur.EN_MENU, joueurService.getJoueurById(clientId).getEtat());
    }
}