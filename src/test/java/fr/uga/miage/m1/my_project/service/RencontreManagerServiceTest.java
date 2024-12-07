package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.exception.rest.RencontreNotFoundRestException;
import fr.uga.miage.m1.my_project.model.Rencontre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RencontreManagerServiceTest {

    private RencontreManagerService rencontreManagerService;

    @BeforeEach
    void setUp() {
        rencontreManagerService = new RencontreManagerService();
    }

    @Test
    void testIncrementNombreRencontreEnAttente() {
        // Act
        rencontreManagerService.incrementNombreRencontreEnAttente();

        // Assert
        assertEquals(1, rencontreManagerService.getNombreRencontreEnAttente().get());
    }

    @Test
    void testDecrementNombreRencontreEnAttente() {
        // Arrange
        Rencontre rencontre = new Rencontre();
        rencontreManagerService.addToRencontreEnAttente(rencontre);
        rencontreManagerService.incrementNombreRencontreEnAttente();

        // Act
        rencontreManagerService.decrementNombreRencontreEnAttente(rencontre);

        // Assert
        assertEquals(0, rencontreManagerService.getNombreRencontreEnAttente().get());
        assertFalse(rencontreManagerService.getRencontresEnAttente().contains(rencontre));
    }

    @Test
    void testAddToRencontreMap() {
        // Arrange
        String clientId = "client123";
        Rencontre rencontre = new Rencontre();

        // Act
        rencontreManagerService.addToRencontreMap(clientId, rencontre);

        // Assert
        assertEquals(rencontre, rencontreManagerService.getRencontreMap().get(clientId));
    }

    @Test
    void testFindRencontreEnAttenteById_Found() {
        // Arrange
        Rencontre rencontre = new Rencontre();
        String idRencontre = "rencontre123";
        rencontre.setIdRencontre(idRencontre);
        rencontreManagerService.addToRencontreEnAttente(rencontre);

        // Act
        Rencontre result = rencontreManagerService.findRencontreEnAttenteById(idRencontre);

        // Assert
        assertEquals(rencontre, result);
    }

    @Test
    void testFindRencontreEnAttenteById_NotFound() {
        // Arrange
        String idRencontre = "rencontreNotFound";

        // Act & Assert
        RencontreNotFoundRestException exception = assertThrows(RencontreNotFoundRestException.class, () -> {
            rencontreManagerService.findRencontreEnAttenteById(idRencontre);
        });

        assertEquals("Rencontre non trouvée.", exception.getMessage());
        assertEquals(idRencontre, exception.getRencontreId());
    }

    @Test
    void testAddToRencontreEnAttente() {
        // Arrange
        Rencontre rencontre = new Rencontre();

        // Act
        rencontreManagerService.addToRencontreEnAttente(rencontre);

        // Assert
        assertTrue(rencontreManagerService.getRencontresEnAttente().contains(rencontre));
    }

    @Test
    void testGetRencontresEnAttente() {
        // Arrange
        Rencontre rencontre1 = new Rencontre();
        Rencontre rencontre2 = new Rencontre();
        rencontreManagerService.addToRencontreEnAttente(rencontre1);
        rencontreManagerService.addToRencontreEnAttente(rencontre2);

        // Act
        List<Rencontre> result = rencontreManagerService.getRencontresEnAttente();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(rencontre1));
        assertTrue(result.contains(rencontre2));
    }
}
