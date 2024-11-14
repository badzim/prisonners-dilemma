package fr.uga.miage.m1.my_project.server;

import fr.uga.miage.m1.my_project.server.mappers.RencontreMapper;
import fr.uga.miage.m1.my_project.server.models.Humain;
import fr.uga.miage.m1.my_project.server.models.Joueur;
import fr.uga.miage.m1.my_project.server.models.enums.ChoiceCommand;
import fr.uga.miage.m1.my_project.server.models.enums.EtatJoueur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientHandlerTest {

    private ClientHandler clientHandler;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private List<Joueur> joueursAttentes;
    private Joueur joueur;

    @BeforeEach
    public void setUp() throws IOException {
        joueursAttentes = new ArrayList<>();
        clientHandler = new ClientHandler(null, joueursAttentes); // Pas de socket nécessaire

        // Mock des flux
        out = mock(ObjectOutputStream.class);
        in = mock(ObjectInputStream.class);

        joueur = new Humain("127.0.0.1:8080", "TestJoueur", null, out, in);
    }




    @Test
     void testInitiateGame() throws Exception {
        // Préparer les données
        when(in.readObject()).thenReturn(5); // Nombre de tours choisis

        // Appeler la méthode
        clientHandler.initiateGame(joueur, out, in);

        // Vérifier que le joueur est en attente et que la rencontre est ajoutée
        assertEquals(EtatJoueur.EN_ATTENTE, joueur.getEtat());
        assertTrue(joueursAttentes.contains(joueur));
        verify(out).writeObject("Saisir nombre de tours");
    }



    @Test
     void testFindRencontreById_Found() {
        // Ajouter une rencontre avec un ID spécifique
        Rencontre rencontre = new Rencontre(joueur, 5);
        List<Rencontre> rencontres = new ArrayList<>();
        rencontres.add(rencontre);

        // Appeler la méthode
        Rencontre result = clientHandler.findRencontreById(rencontres, rencontre.getIdRencontre());

        // Vérifier que la rencontre a été trouvée
        assertNotNull(result);
        assertEquals(rencontre, result);
    }

    @Test
     void testFindRencontreById_NotFound() {
        // Liste vide
        List<Rencontre> rencontres = new ArrayList<>();

        // Appeler la méthode
        Rencontre result = clientHandler.findRencontreById(rencontres, 1);

        // Vérifier
    }
}