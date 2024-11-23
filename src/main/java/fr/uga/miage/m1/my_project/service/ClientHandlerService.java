package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.component.RencontreComponent;
import fr.uga.miage.m1.my_project.mapper.RencontreMapper;
import fr.uga.miage.m1.my_project.model.ClientHandler;
import fr.uga.miage.m1.my_project.model.Rencontre;
import fr.uga.miage.m1.my_project.model.enums.ChoiceCommand;
import fr.uga.miage.m1.my_project.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.model.joueur.Humain;
import fr.uga.miage.m1.my_project.model.joueur.Joueur;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClientHandlerService {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class.getName());

    private final RencontreManagerService rencontreManagerService;

    // Méthode pour initialiser le joueur
    public Joueur initializePlayer(ObjectOutputStream out, ObjectInputStream in, Socket clientSocket) throws IOException, ClassNotFoundException {
        // Envoyer le message de bienvenue
        out.writeObject("Bienvenue! Veuillez entrer votre nom:");
        out.flush();

        // Recevoir le nom
        String nom = (String) in.readObject();
        logger.info("Received :{}", nom);

        // Créer un joueur humain
        Joueur joueur = new Humain(clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort(), nom, clientSocket, out, in);
        joueur.setEtat(EtatJoueur.EN_MENU);
        return joueur;
    }

    // Méthode pour gérer le menu du joueur
    public void handlePlayerMenu(Joueur joueur, ObjectOutputStream out, ObjectInputStream in, RencontreComponent rencontreComponent) throws IOException, ClassNotFoundException {
        while (joueur.getEtat() == EtatJoueur.EN_MENU) {
            logger.info(joueur.getEtat().toString());

            // Recevoir choix client
            ChoiceCommand choiceCommand = (ChoiceCommand) in.readObject();
            logger.info("Received :{}", choiceCommand);


            if (choiceCommand == ChoiceCommand.INITIER_PARTIE) {
                initiateGame(joueur, out, in);
            } else if (choiceCommand == ChoiceCommand.REJOINDRE_PARTIE) {
                joinGame(joueur, out, in, rencontreComponent);
            }

        }
    }

    // Méthode pour initier une partie
    void initiateGame(Joueur joueur, ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        out.writeObject("Saisir nombre de tours");
        out.flush();

        int nombreTourChoisis = (int) in.readObject();
        Rencontre rencontre = new Rencontre(joueur, nombreTourChoisis);
        rencontreManagerService.incrementNombreRencontreEnAttente();
        rencontreManagerService.addToRencontreEnAttente(rencontre);
        joueur.setEtat(EtatJoueur.EN_ATTENTE);
        joueur.sendMessage("En attente d'un autre joueur pour démarrer la rencontre...");
    }

    // Méthode pour rejoindre une partie
    void joinGame(Joueur joueur, ObjectOutputStream out, ObjectInputStream in, RencontreComponent rencontreComponent) throws IOException, ClassNotFoundException {
        List<Rencontre> rencontres = rencontreManagerService.getRencontresEnAttente();
        out.writeObject(RencontreMapper.rencontreToRencontreDTO(rencontres));
        out.flush();

        if (rencontres.isEmpty()) {
            return;
        }

        String idRencontre = (String) in.readObject(); // ID reçu du client
        logger.info("Received : {}", idRencontre);

        Rencontre rencontreChoisie = findRencontreById(rencontres, idRencontre);

        if (rencontreChoisie != null) {
            rencontreManagerService.decrementNombreRencontreEnAttente(rencontreChoisie);
            rencontreChoisie.setAdversaire(joueur);
            joueur.setEtat(EtatJoueur.EN_ATTENTE);
            rencontreComponent.setRencontre(rencontreChoisie);
            rencontreComponent.run();
        } else {
            logger.warn("Aucune rencontre avec l'ID : {}", idRencontre);
            out.writeObject("ID de rencontre invalide. Veuillez choisir un ID valide.");
            out.flush();
        }
    }

    // Méthode pour trouver une rencontre par ID
    Rencontre findRencontreById(List<Rencontre> rencontres, String idRencontre) {
        for (Rencontre rencontre : rencontres) {
            logger.info("Rencontre cherche : {}", rencontre.getIdRencontre());
            if (Objects.equals(rencontre.getIdRencontre(), idRencontre)) {

                return rencontre;
            }
        }
        return null;
    }
}
