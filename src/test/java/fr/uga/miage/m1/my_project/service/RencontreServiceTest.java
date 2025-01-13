package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_RENCONTRE;
import fr.uga.miage.m1.my_project.core.domain.service.JoueurService;
import fr.uga.miage.m1.my_project.core.domain.service.TourService;
import fr.uga.miage.m1.my_project.core.exception.rest.RencontreNotFoundRestException;
import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;
import fr.uga.miage.m1.my_project.core.domain.model.Tour;
import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_JOUEUR;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Humain;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Robot;
import fr.uga.miage.m1.my_project.core.port.output.StrategieRepository;
import fr.uga.miage.m1.my_project.persistence.memory.InMemoryRencontreRepository;
import fr.uga.miage.m1.my_project.web.restapi.response.RencontreResponse;
import fr.uga.miage.m1.my_project.web.service.PingSchedulerServiceImpl;
import fr.uga.miage.m1.my_project.web.service.SseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class RencontreServiceTest {

    @SpyBean
    private RencontreService rencontreService;

    @MockBean
    private PingSchedulerServiceImpl pingSchedulerServiceImpl;

    @MockBean
    private SseServiceImpl sseServiceImpl;

    @MockBean
    private InMemoryRencontreRepository inMemoryRencontreRepository;

    @MockBean
    private JoueurService joueurService;

    @MockBean
    private TourService tourService;
    @Autowired
    private StrategieRepository strategieFactoryService;

    @BeforeEach
    public void setUp() {
        // Ré-initialiser tous vos objets, remettre vos mocks à zéro, etc.
        MockitoAnnotations.openMocks(this);
        // Recréer vos données de test si nécessaire
    }


    @Test
    void testGetRencontresDisponibles() {
        // Arrange
        Rencontre rencontre1 = new Rencontre();
        rencontre1.setInitiateur(new Humain("id_test_01", "nom_test_01"));
        Rencontre rencontre2 = new Rencontre();
        rencontre2.setInitiateur(new Humain("id_test_02", "nom_test_02"));

        when(inMemoryRencontreRepository.getRencontresEnAttente()).thenReturn(List.of(rencontre1, rencontre2));

        // Act
        List<RencontreResponse> result = rencontreService.getRencontresEnAttente();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size(), "La liste doit contenir 2 rencontres.");
        // Vérifie que les rencontres ont bien été transformées en DTO
        // Le mappeur est statique, donc on se contente d'assurer la taille et la non-nullité.

        // Vérifie que les méthodes internes sont appelées
        verify(inMemoryRencontreRepository, times(2)).getRencontresEnAttente();
        // On ne peut pas vérifier directement removeDisconnectedRencontres() car elle est privée,
        // mais on voit qu'au moins on appelle getRencontresEnAttente() deux fois (une dans remove et une après).
        // On peut également vérifier qu'on a appelé handleDisconnectedPlayers sur sseService.
        verify(sseServiceImpl, times(1)).handleDisconnectedPlayers();
    }

    @Test
    void testInitierRencontre() {
        // Arrange
        String clientId = "client123";
        int nombreTours = 3;

        Joueur mockJoueur = new Humain(clientId, clientId);
        mockJoueur.setId(clientId);

        // Mock comportement
        when(sseServiceImpl.getSseEmitters()).thenReturn(java.util.Collections.singletonMap(clientId, new SseEmitter())); // Pour éviter l'exception dans verifyClientConnected
        when(joueurService.getJoueurById(clientId)).thenReturn(mockJoueur);

        // Act
        boolean result = rencontreService.initierRencontre(clientId, nombreTours);

        // Assert
        assertTrue(result, "initierRencontre devrait retourner true.");

        // Vérifications des appels
        verify(sseServiceImpl, times(1)).getSseEmitters(); // Vérifie que le client est connecté
        verify(joueurService, times(1)).getJoueurById(clientId); // Vérifie que le joueur est récupéré
        assertSame(ETAT_JOUEUR.EN_ATTENTE, joueurService.getJoueurById(clientId).getEtat());
        // Vérifie que la rencontre a été ajoutée et que le compteur d'attente a été incrémenté
        verify(inMemoryRencontreRepository, times(1)).addRencontre(any(), any(Rencontre.class));

        // Vérifie les appels SSE
        verify(sseServiceImpl, times(1)).sendEvent(anyString(), anyString(), anyString());
        verify(sseServiceImpl, times(1)).broadcast(anyString(), contains(clientId));

        // Pas besoin de vérifier les logs directement, sauf si le logger est mocké.
    }

    @Test
    void testRejoindreRencontre() {
        // Arrange
        String initiateurId = "initiateur123";
        String adversaireId = "adversaire123";
        String idRencontre = "rencontre456";

        // Créer un joueur Humain pour représenter l'adversaire
        Humain adversaire = new Humain(adversaireId, "Adversaire Test");

        // Créer un initiateur pour la rencontre
        Humain initiateur = new Humain(initiateurId, "Initiateur Test");

        // Créer une rencontre factice
        Rencontre rencontre = new Rencontre();
        rencontre.setInitiateur(initiateur);
        rencontre.setIdRencontre(idRencontre);
        initiateur.setEtat(ETAT_JOUEUR.EN_MENU);

        // Simuler les comportements
        // Mock des comportements
        Map<String, SseEmitter> emitters = new HashMap<>();
        emitters.put(initiateurId, new SseEmitter());
        emitters.put(adversaireId, new SseEmitter());

        when(sseServiceImpl.getSseEmitters()).thenReturn(emitters);
        when(joueurService.getJoueurById(adversaireId)).thenReturn(adversaire);
        when(inMemoryRencontreRepository.findRencontreById(idRencontre)).thenReturn(rencontre);

        // Act
        rencontreService.rejoindreRencontre(adversaireId, idRencontre);

        // Assert
        // Vérifie que l'adversaire est ajouté à la rencontre
        assertEquals(adversaire, rencontre.getAdversaire());
        assertEquals(ETAT_JOUEUR.EN_PARTIE_ADVERSAIRE, adversaire.getEtat());
        assertEquals(ETAT_JOUEUR.EN_PARTIE_INITIATEUR, initiateur.getEtat());


        // Vérifie que les appels aux dépendances sont faits correctement
        verify(sseServiceImpl, times(3)).getSseEmitters(); // Vérifie la connexion du client
        verify(joueurService, times(1)).getJoueurById(adversaireId); // Vérifie que le joueur est récupéré
        verify(inMemoryRencontreRepository, times(1)).findRencontreById(idRencontre); // Vérifie que la rencontre est récupérée

        // Vérifie que les états des joueurs sont correctement mis à jour
        verify(inMemoryRencontreRepository, times(1)).changerEtatRencontre(idRencontre, ETAT_RENCONTRE.EN_COURS);
        verify(inMemoryRencontreRepository, times(1)).addRencontreParClient(adversaireId, rencontre);

        // Vérifie les notifications SSE
        verify(sseServiceImpl, times(2)).sendEvent(anyString(), anyString(), anyString());
        verify(sseServiceImpl, times(1)).broadcast(anyString(), contains(adversaireId));
    }

    @Test
    void testRejoindreRencontre_initiateur_deconnecte() {
        // Arrange
        String initiateurId = "initiateur123";
        String adversaireId = "adversaire123";
        String idRencontre = "rencontre456";

        // Créer un joueur Humain pour représenter l'adversaire
        Humain adversaire = new Humain(adversaireId, "Adversaire Test");

        // Créer un initiateur pour la rencontre
        Humain initiateur = new Humain(initiateurId, "Initiateur Test");

        // Créer une rencontre factice
        Rencontre rencontre = new Rencontre();
        rencontre.setInitiateur(initiateur);
        rencontre.setIdRencontre(idRencontre);
        initiateur.setEtat(ETAT_JOUEUR.EN_MENU);

        // Simuler les comportements
        // Mock des comportements
        Map<String, SseEmitter> emitters = new HashMap<>();
        emitters.put(adversaireId, new SseEmitter());

        when(sseServiceImpl.getSseEmitters()).thenReturn(emitters);
        when(joueurService.getJoueurById(adversaireId)).thenReturn(adversaire);
        when(inMemoryRencontreRepository.findRencontreById(idRencontre)).thenReturn(rencontre);

        // Act
        rencontreService.rejoindreRencontre(adversaireId, idRencontre);

        // Assert
        // Vérifie que l'adversaire est ajouté à la rencontre
        assertEquals(adversaire, rencontre.getAdversaire());
        assertEquals(ETAT_JOUEUR.EN_PARTIE_ADVERSAIRE, adversaire.getEtat());
        assertEquals(ETAT_JOUEUR.EN_MENU, initiateur.getEtat());


        // Vérifie que les appels aux dépendances sont faits correctement
        verify(sseServiceImpl, times(3)).getSseEmitters(); // Vérifie la connexion du client
        verify(joueurService, times(1)).getJoueurById(adversaireId); // Vérifie que le joueur est récupéré
        verify(inMemoryRencontreRepository, times(1)).findRencontreById(idRencontre); // Vérifie que la rencontre est récupérée

        // Vérifie que les états des joueurs sont correctement mis à
        verify(inMemoryRencontreRepository, times(1)).changerEtatRencontre(idRencontre, ETAT_RENCONTRE.EN_COURS);
        verify(inMemoryRencontreRepository, times(1)).addRencontreParClient(adversaireId, rencontre);

        // Vérifie les notifications SSE
        verify(sseServiceImpl, times(2)).sendEvent(anyString(), anyString(), anyString());
        verify(sseServiceImpl, times(1)).broadcast(anyString(), contains(adversaireId));
    }


    @Test
    void testEnregistrerChoix_NormalFlow() {
        // Arrange
        String clientId = "client123";
        TYPE_ACTION action = TYPE_ACTION.COOPERER;
        TYPE_STRATEGIE strategie = TYPE_STRATEGIE.DONNANTDONNANT;

        Humain joueur = new Humain(clientId, "Joueur Test");
        Humain adversaire = new Humain("opponent456", "Adversaire Test");

        Rencontre rencontre = new Rencontre();
        rencontre.setInitiateur(joueur);
        rencontre.setAdversaire(adversaire);

        Tour currentTour = new Tour(1);
        rencontre.setCurrentTour(currentTour);

        // Mock des comportements
        when(sseServiceImpl.getSseEmitters()).thenReturn(java.util.Collections.singletonMap(clientId, new SseEmitter()));
        when(inMemoryRencontreRepository.findRencontreByClientIdAndEtatRencontre(clientId, ETAT_RENCONTRE.EN_COURS)).thenReturn(rencontre);

        // Mock pour vérifier si le tour est prêt
        when(rencontreService.estTourPret(rencontre)).thenReturn(false);

        // Act
        rencontreService.enregistrerChoix(clientId, action, strategie, "");

        // Assert
        // Vérifie que le joueur est bien trouvé et que l'action est enregistrée
        verify(tourService, times(1)).setActionJoueur(joueur, rencontre, action);

        // Vérifie que le tour n'est pas encore traité car il n'est pas prêt
        assertEquals(currentTour, rencontre.getCurrentTour());

        // Vérifie que le client est bien connecté
        verify(sseServiceImpl, times(1)).getSseEmitters();
    }

    @Test
    void testEnregistrerChoix_PlayerAbandons() {
        // Arrange
        String clientId = "client123";
        TYPE_ACTION action = TYPE_ACTION.ABONDONNER;
        TYPE_STRATEGIE strategie = TYPE_STRATEGIE.DONNANTDONNANT;

        Humain joueur = new Humain(clientId, "Joueur Test");
        Humain adversaire = new Humain("opponent456", "Adversaire Test");

        Rencontre rencontre = new Rencontre();
        rencontre.setInitiateur(joueur);
        rencontre.setAdversaire(adversaire);

        Tour currentTour = new Tour(1);
        rencontre.setCurrentTour(currentTour);

        // Mock des comportements
        when(sseServiceImpl.getSseEmitters()).thenReturn(java.util.Collections.singletonMap(clientId, new SseEmitter()));
        when(inMemoryRencontreRepository.findRencontreByClientIdAndEtatRencontre(clientId, ETAT_RENCONTRE.EN_COURS)).thenReturn(rencontre);
        when(rencontreService.estTourPret(rencontre)).thenReturn(false);

        // Mock handlePlayerAbandon pour retourner une action par défaut
        when(rencontreService.handlePlayerAbandon(rencontre, joueur, strategie, adversaire,"" ))
                .thenReturn(TYPE_ACTION.COOPERER);

        // Mock pour sendEvent
        doNothing().when(sseServiceImpl).sendEvent(anyString(), anyString(), anyString());

        // Act
        rencontreService.enregistrerChoix(clientId, action, strategie,"" );

        // Assert
        // Vérifie que l'abandon a été géré
        verify(rencontreService, times(1)).handlePlayerAbandon(rencontre, joueur, strategie, adversaire,"");

        // Vérifie que l'action enregistrée après l'abandon est COOPERER
        verify(tourService, times(1)).setActionJoueur(joueur, rencontre, TYPE_ACTION.COOPERER);

        // Vérifie l'envoi de l'événement d'abandon
        verify(sseServiceImpl, times(2)).sendEvent(anyString(), anyString(), anyString());
    }


    @Test
    void testEnregistrerChoix_TourIsReady() {
        // Arrange
        String clientId = "client123";
        TYPE_ACTION action = TYPE_ACTION.COOPERER;
        TYPE_STRATEGIE strategie = null; // Pas nécessaire pour un tour normal

        Humain joueur = new Humain(clientId, "Joueur Test");
        Humain adversaire = new Humain("opponent456", "Adversaire Test");

        Rencontre rencontre = new Rencontre();
        rencontre.setNombreTours(3);
        rencontre.setInitiateur(joueur);
        rencontre.setAdversaire(adversaire);

        Tour currentTour = new Tour(1);
        rencontre.setCurrentTour(currentTour);

        // Mock des comportements
        when(sseServiceImpl.getSseEmitters()).thenReturn(java.util.Collections.singletonMap(clientId, new SseEmitter()));
        when(inMemoryRencontreRepository.findRencontreByClientIdAndEtatRencontre(clientId, ETAT_RENCONTRE.EN_COURS)).thenReturn(rencontre);

        // Mock pour vérifier si le tour est prêt
        when(rencontreService.estTourPret(rencontre)).thenReturn(true);

        // Act
        rencontreService.enregistrerChoix(clientId, action, strategie, "");

        // Assert
        // Vérifie que l'action est enregistrée
        verify(tourService, times(1)).setActionJoueur(joueur, rencontre, action);

        // Vérifie que le tour est traité car il est prêt
        assertNotEquals(currentTour , rencontre.getCurrentTour());
    }

    @Test
    void testHandleRobotActionAfterBothPlayersAbandon_WithInitialConnection() {
        // Arrange
        String initiateurId = "humanInitiator";
        String adversaireId = "humanAdversary";

        Humain initiateur = new Humain(initiateurId, "Initiateur Humain");
        initiateur.setEtat(ETAT_JOUEUR.EN_PARTIE_INITIATEUR);

        Humain adversaire = new Humain(adversaireId, "Adversaire Humain");
        adversaire.setEtat(ETAT_JOUEUR.EN_PARTIE_ADVERSAIRE);

        Rencontre rencontre = new Rencontre();
        rencontre.setNombreTours(3);
        rencontre.setInitiateur(initiateur);
        rencontre.setAdversaire(adversaire);

        Tour currentTour = new Tour(1);
        rencontre.setCurrentTour(currentTour);

        // Mock des SseEmitters pour simuler les connexions des deux joueurs
        Map<String, SseEmitter> sseEmitters = new HashMap<>();
        sseEmitters.put(initiateurId, new SseEmitter());
        sseEmitters.put(adversaireId, new SseEmitter());
        when(sseServiceImpl.getSseEmitters()).thenReturn(sseEmitters);

        // Mock des rencontres dans RencontreManagerService
        when(inMemoryRencontreRepository.findRencontreByClientIdAndEtatRencontre(initiateurId, ETAT_RENCONTRE.EN_COURS)).thenReturn(rencontre);
        when(inMemoryRencontreRepository.findRencontreByClientIdAndEtatRencontre(adversaireId, ETAT_RENCONTRE.EN_COURS)).thenReturn(rencontre);

        // Act - Le premier joueur abandonne
        rencontreService.enregistrerChoix(initiateurId, TYPE_ACTION.ABONDONNER, TYPE_STRATEGIE.DONNANTDONNANT, "");

        // Vérifie que l'initiateur est remplacé par un robot
        assertInstanceOf(Robot.class, rencontre.getInitiateur(), "L'initiateur doit être remplacé par un robot.");
        Robot initiateurRobot = (Robot) rencontre.getInitiateur();

        // Act - Le deuxième joueur abandonne
        rencontreService.enregistrerChoix(adversaireId, TYPE_ACTION.ABONDONNER, TYPE_STRATEGIE.DONNANTDONNANT, "");

        // Vérifie que l'adversaire est remplacé par un robot
        assertTrue(rencontre.getAdversaire() instanceof Robot, "L'adversaire doit être remplacé par un robot.");
        Robot adversaireRobot = (Robot) rencontre.getAdversaire();




    }



    @Test
    void testVerifyClientConnected_ClientConnected() {
        // Arrange
        String clientId = "client123";
        int nombreTours = 3;

        Joueur mockJoueur = new Humain(clientId, clientId);
        mockJoueur.setId(clientId);

        // Mock comportement
        when(sseServiceImpl.getSseEmitters()).thenReturn(java.util.Collections.singletonMap(clientId, new SseEmitter())); // Pour éviter l'exception dans verifyClientConnected
        when(joueurService.getJoueurById(clientId)).thenReturn(mockJoueur);

        // Act
        boolean result = rencontreService.initierRencontre(clientId, nombreTours);

        // Assert
        assertTrue(result, "initierRencontre devrait retourner true.");

        // Vérifications des appels
        verify(sseServiceImpl, times(1)).getSseEmitters(); // Vérifie que le client est connecté
        verify(joueurService, times(1)).getJoueurById(clientId); // Vérifie que le joueur est récupéré
        assertSame(ETAT_JOUEUR.EN_ATTENTE, joueurService.getJoueurById(clientId).getEtat());
    }

    @Test
    void testVerifyClientConnected_ClientNotConnected() {
        // Arrange
        String clientId = "client123";
        when(sseServiceImpl.getSseEmitters()).thenReturn(new HashMap<>());

        // Act & Assert
        RencontreNotFoundRestException exception = assertThrows(RencontreNotFoundRestException.class, () -> {
            rencontreService.initierRencontre(clientId, 3);
        });

        assertEquals("Le client n'est pas connecté.", exception.getMessage());
        assertEquals(clientId, exception.getRencontreId());
        verify(sseServiceImpl, times(1)).getSseEmitters();
    }





    @Test
    void testEnregistrerChoix_PlayerAbandons_WithG2_5() {
        // Arrange
        String clientId = "client123";
        TYPE_ACTION action = TYPE_ACTION.ABONDONNER;
        TYPE_STRATEGIE strategie = TYPE_STRATEGIE.TOUJOURSTRAHIR;

        Humain joueur = new Humain(clientId, "Joueur Test");
        Humain adversaire = new Humain("opponent456", "Adversaire Test");

        Rencontre rencontre = new Rencontre();
        rencontre.setInitiateur(joueur);
        rencontre.setAdversaire(adversaire);

        Tour currentTour = new Tour(1);
        rencontre.setCurrentTour(currentTour);

        // Mock des services
        when(sseServiceImpl.getSseEmitters()).thenReturn(Collections.singletonMap(clientId, new SseEmitter()));
        when(inMemoryRencontreRepository.findRencontreByClientIdAndEtatRencontre(clientId, ETAT_RENCONTRE.EN_COURS)).thenReturn(rencontre);
        when(rencontreService.estTourPret(rencontre)).thenReturn(false);

        // Configuration pour le groupe G2_5
        String groupId = "G2_5";

        // Act
        rencontreService.enregistrerChoix(clientId, action, strategie, groupId);

        // Assert
        // Vérifie que l'abandon a été géré
        verify(rencontreService, times(1)).handlePlayerAbandon(rencontre, joueur, strategie, adversaire, groupId);


        // Vérifie l'envoi des événements d'abandon
        verify(sseServiceImpl, times(1)).sendEvent(eq(clientId), eq("player-abondonne"), anyString());
        verify(sseServiceImpl, times(1)).sendEvent(eq(adversaire.getId()), eq("opposite-player-abondonne"), anyString());
    }



}
