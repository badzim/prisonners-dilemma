package fr.uga.miage.m1.my_project.model;
import fr.uga.miage.m1.my_project.mapper.RencontreMapper;
import fr.uga.miage.m1.my_project.model.joueur.Humain;
import fr.uga.miage.m1.my_project.model.joueur.Joueur;
import fr.uga.miage.m1.my_project.model.enums.ChoiceCommand;
import fr.uga.miage.m1.my_project.model.enums.EtatJoueur;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;



public class ClientHandler extends Thread {
    private Socket clientSocket;
    private List<Joueur> joueursAttentes;
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class.getName());



    public ClientHandler(Socket socket, List<Joueur> joueursAttente) {
        this.clientSocket = socket;
        this.joueursAttentes = joueursAttente;
    }

    @Override
    public void run() {
        try {
            // Initialiser les flux
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // Créer le joueur
            Joueur joueur = initializePlayer(out, in);

            // Boucle principale
            handlePlayerMenu(joueur, out, in);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    // Méthode pour initialiser le joueur
    Joueur initializePlayer(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
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
    void handlePlayerMenu(Joueur joueur, ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        while (joueur.getEtat() == EtatJoueur.EN_MENU) {
            logger.info(joueur.getEtat().toString());

            // Recevoir choix client
            ChoiceCommand choiceCommand = (ChoiceCommand) in.readObject();
            logger.info("Received :{}", choiceCommand);

            synchronized (joueursAttentes) {
                if (choiceCommand == ChoiceCommand.INITIER_PARTIE) {
                    initiateGame(joueur, out, in);
                } else if (choiceCommand == ChoiceCommand.REJOINDRE_PARTIE) {
                    joinGame(joueur, out, in);
                }
            }
        }
    }

    // Méthode pour initier une partie
    void initiateGame(Joueur joueur, ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        out.writeObject("Saisir nombre de tours");
        out.flush();

        int nombreTourChoisis = (int) in.readObject();
        Rencontre rencontre = new Rencontre(joueur, nombreTourChoisis);
        Rencontre.addToRencontreEnAttente(rencontre);

        joueursAttentes.add(joueur);
        joueur.setEtat(EtatJoueur.EN_ATTENTE);
        joueur.sendMessage("En attente d'un autre joueur pour démarrer la rencontre...");
    }

    // Méthode pour rejoindre une partie
    void joinGame(Joueur joueur, ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        List<Rencontre> rencontres = Rencontre.getRencontresEnAttente();
        out.writeObject(RencontreMapper.rencontreToRencontreDTO(rencontres));
        out.flush();

        if (rencontres.isEmpty()) {
            return;
        }

        int idRencontre = (int) in.readObject(); // ID reçu du client
        logger.info("Received : {}", idRencontre);

        Rencontre rencontreChoisie = findRencontreById(rencontres, idRencontre);

        if (rencontreChoisie != null) {
            logger.info("Rencontre trouvée : {}", rencontreChoisie);
            Rencontre.decrementNombreRencontreEnAttente(rencontreChoisie);
            rencontreChoisie.setAdversaire(joueur);
            joueursAttentes.remove(joueur);
            joueur.setEtat(EtatJoueur.EN_ATTENTE);
            rencontreChoisie.start();
        } else {
            logger.warn("Aucune rencontre avec l'ID : {}", idRencontre);
            out.writeObject("ID de rencontre invalide. Veuillez choisir un ID valide.");
            out.flush();
        }
    }

    // Méthode pour trouver une rencontre par ID
    Rencontre findRencontreById(List<Rencontre> rencontres, int idRencontre) {
        for (Rencontre rencontre : rencontres) {
            if (rencontre.getIdRencontre() == idRencontre) {
                return rencontre;
            }
        }
        return null;
    }



}