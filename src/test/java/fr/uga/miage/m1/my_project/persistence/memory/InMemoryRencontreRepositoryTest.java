package fr.uga.miage.m1.my_project.persistence.memory;

import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_RENCONTRE;
import fr.uga.miage.m1.my_project.core.exception.rest.RencontreNotFoundRestException;
import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;
import fr.uga.miage.m1.my_project.core.port.output.RencontreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRencontreRepositoryTest {

    @Qualifier("inMemoryRencontreRepository")
    private RencontreRepository inMemoryRencontreRepository;

    @BeforeEach
    void setUp() {
        inMemoryRencontreRepository = new InMemoryRencontreRepository();
    }


    @Test
    void testChangeEtatRencontre() {
        // Arrange
        Rencontre rencontre = new Rencontre();
        inMemoryRencontreRepository.addRencontre(rencontre.getIdRencontre(), rencontre);

        // Act
        inMemoryRencontreRepository.changerEtatRencontre(rencontre.getIdRencontre(), ETAT_RENCONTRE.EN_COURS);

        // Assert
        assertEquals(ETAT_RENCONTRE.EN_COURS, rencontre.getEtatRencontre());
        assertFalse(inMemoryRencontreRepository.getRencontresEnAttente().contains(rencontre));
    }

    @Test
    void testAddToRencontreMap() {
        // Arrange
        String clientId = "client123";
        Rencontre rencontre = new Rencontre();

        // Act
        inMemoryRencontreRepository.addRencontreParClient(clientId, rencontre);

        // Assert
        assertEquals(rencontre, inMemoryRencontreRepository.findRencontreByClientIdAndEtatRencontre(clientId, ETAT_RENCONTRE.EN_ATTENTE));
    }

    @Test
    void testFindRencontreEnAttenteById_Found() {
        // Arrange
        Rencontre rencontre = new Rencontre();
        String idRencontre = "rencontre123";
        rencontre.setIdRencontre(idRencontre);
        inMemoryRencontreRepository.addRencontre(idRencontre, rencontre);

        // Act
        Rencontre result = inMemoryRencontreRepository.findRencontreById(idRencontre);

        // Assert
        assertEquals(rencontre, result);
    }

    @Test
    void testFindRencontreEnAttenteById_NotFound() {
        // Arrange
        String idRencontre = "rencontreNotFound";

        // Act & Assert
        RencontreNotFoundRestException exception = assertThrows(RencontreNotFoundRestException.class, () -> {
            inMemoryRencontreRepository.findRencontreById(idRencontre);
        });

        assertEquals("Rencontre non trouvée.", exception.getMessage());
        assertEquals(idRencontre, exception.getRencontreId());
    }

    @Test
    void testAddToRencontreEnAttente() {
        // Arrange
        Rencontre rencontre = new Rencontre();

        // Act
        inMemoryRencontreRepository.addRencontre("", rencontre);

        // Assert
        assertTrue(inMemoryRencontreRepository.getRencontresEnAttente().contains(rencontre));
    }

    @Test
    void testGetRencontresEnAttente() {
        // Arrange
        Rencontre rencontre1 = new Rencontre();
        Rencontre rencontre2 = new Rencontre();
        inMemoryRencontreRepository.addRencontre(rencontre1.getIdRencontre(), rencontre1);
        inMemoryRencontreRepository.addRencontre(rencontre2.getIdRencontre(), rencontre2);

        // Act
        List<Rencontre> result = inMemoryRencontreRepository.getRencontresEnAttente();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(rencontre1));
        assertTrue(result.contains(rencontre2));
    }
}
