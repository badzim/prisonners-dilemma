package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.exception.rest.InvalidActionRestException;
import fr.uga.miage.m1.my_project.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.model.joueur.Humain;
import fr.uga.miage.m1.my_project.model.joueur.Joueur;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JoueurServiceTest {

    @SpyBean
    private JoueurService joueurService;


    @Test
    void testGetHumain_NewJoueurCreated() {
        // Arrange
        String clientId = "client123";

        joueurService.getJoueurs().clear();

        // Act
        Joueur result = joueurService.getHumain(clientId);

        // Assert
        assertNotNull(result, "Un joueur doit être créé si non existant.");
        assertTrue(result instanceof Humain, "Le joueur créé doit être de type Humain.");
        assertEquals(clientId, result.getId(), "L'ID du joueur doit correspondre au clientId.");
        assertEquals(EtatJoueur.EN_MENU, result.getEtat(), "Le joueur doit être initialement dans l'état EN_MENU.");
    }


    @Test
    void testGetHumain_ExistingJoueurRetrieved() {
        // Arrange
        String clientId = "client123";
        Joueur existingJoueur = new Humain(clientId, "Nom Test");
        existingJoueur.setEtat(EtatJoueur.EN_ATTENTE);

        // Ajouter un joueur existant dans la map
        joueurService.getJoueurs().put(clientId, existingJoueur);

        // Act
        Joueur result = joueurService.getHumain(clientId);

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

        joueurService.getJoueurs().put(clientId, joueur);

        // Act & Assert
        assertDoesNotThrow(() -> joueurService.joueurEstEnMenu(clientId), "Le joueur étant dans le menu ne doit pas lancer d'exception.");
    }

    @Test
    void testJoueurEstEnMenu_ThrowsException() {
        // Arrange
        String clientId = "client123";
        Joueur joueur = new Humain(clientId, "Nom Test");
        joueur.setEtat(EtatJoueur.EN_ATTENTE); // Pas dans EN_MENU

        joueurService.getJoueurs().put(clientId, joueur);

        // Act & Assert
        InvalidActionRestException exception = assertThrows(InvalidActionRestException.class, () -> {
            joueurService.joueurEstEnMenu(clientId);
        });

        assertEquals("Le joueur doit être dans le menu", exception.getMessage(), "Le message d'exception doit être correct.");
    }
}