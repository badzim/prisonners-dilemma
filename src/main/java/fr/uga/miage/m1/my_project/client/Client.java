package fr.uga.miage.m1.my_project.client;

import fr.uga.miage.m1.my_project.server.Rencontre;
import fr.uga.miage.m1.my_project.server.dtos.RencontreDTO;
import fr.uga.miage.m1.my_project.server.models.enums.ChoiceCommand;
import fr.uga.miage.m1.my_project.server.models.enums.TypeStrategie;
import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;



public class Client {
    public static final String SERVEUR_MESSAGE = "Serveur: {}";
    private final String host;
    private final int port;
    private final Logger logger = LoggerFactory.getLogger(Client.class.getName());

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(host, port)) {
            // Créer les flux une seule fois, dans cet ordre
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);
            recevoirMessageBienvenue(in);
            envoyerNomJoueur(scanner, out);
            choixDeMethode(scanner, out, in);
            // Commencer à écouter les messages du serveur
            ecouteClient(out, in, scanner);
        } catch (Exception e) {
            logger.info("Connexion fermée.");
        }
    }

    private void recevoirMessageBienvenue(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Recevoir le message de bienvenue
        String welcome = (String) in.readObject();
        logger.info(SERVEUR_MESSAGE, welcome);
    }

    private void envoyerNomJoueur(Scanner scanner, ObjectOutputStream out) throws IOException {
        // Entrer le nom
        logger.info("Entrez votre nom: ");
        String nom = scanner.nextLine();
        out.writeObject(nom);
        out.flush();
    }

    private void choixDeMethode(Scanner scanner, ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Choisir une option
        ChoiceCommand choix = null;
        while (choix == null) {
            logger.info("Entrez votre choix (INITIER_PARTIE / REJOINDRE_PARTIE): ");
            String input = scanner.nextLine().toUpperCase();
            try {
                choix = ChoiceCommand.valueOf(input);
            } catch (IllegalArgumentException e) {
                logger.info("Choix invalide. Veuillez entrer INITIER_PARTIE, REJOINDRE_PARTIE ou QUITTER_JEUX.");
                continue;
            }
            // Envoyer la commande au serveur
            out.writeObject(choix);
            out.flush();



            if (choix == ChoiceCommand.INITIER_PARTIE) {
                // Ici on doit mentionner le nombre de tours qu'on veut...
                // Recevoir la confirmation ou attendre
                int nbTour = demanderNombre(scanner);
                out.writeObject(nbTour);
                out.flush();

            } else if (choix == ChoiceCommand.REJOINDRE_PARTIE) {
                // Ici le client doit choisir une partie parmi les parties existantes
                // Si aucune partie n'existe, informer le client, le mettre en attente,
                // et éventuellement proposer d'initier une partie s'il le souhaite...
                // Cette logique dépend de la mise en œuvre côté serveur

                List<RencontreDTO> rencontres = getRencontreFromInputStream(in);

                if (rencontres.isEmpty()) {
                    logger.info("Aucune partie n'est initiée.");
                    choix = null;
                } else {
                    choisirEtEnvoyerIdPartie(scanner, out, rencontres);
                }



            }
        }
    }

    // Méthodes auxiliaires

    private void choisirEtEnvoyerIdPartie(Scanner scanner, ObjectOutputStream out, List<RencontreDTO> rencontres) throws IOException {
        boolean choixValide = false;
        while (!choixValide) {
            afficherRencontresDisponibles(rencontres);
            int idChoisi = demanderIdPartie(scanner);

            if (idValide(idChoisi, rencontres.size())) {
                choixValide = true;
                envoyerIdAuServeur(out, idChoisi);
                logger.info("Vous avez choisi la partie ID {}.", idChoisi);
            } else {
                logger.info("ID invalide. Veuillez choisir un ID parmi les parties disponibles.");
            }
        }
    }

    private void afficherRencontresDisponibles(List<RencontreDTO> rencontres) {
        logger.info("Voici les parties disponibles :");
        for (int i = 0; i < rencontres.size(); i++) {
            RencontreDTO rencontre = rencontres.get(i);
            logger.info("ID {} avec {} tours.", (i + 1), rencontre.getNombreTour());
        }
    }

    private int demanderIdPartie(Scanner scanner) {
        logger.info("Entrez l'ID de la partie que vous souhaitez rejoindre : ");
        return demanderNombre(scanner);
    }

    private boolean idValide(int idChoisi, int maxId) {
        return idChoisi > 0 && idChoisi <= maxId;
    }

    private void envoyerIdAuServeur(ObjectOutputStream out, int idChoisi) throws IOException {
        out.writeObject(idChoisi);
        out.flush();
    }


    private static List<RencontreDTO> getRencontreFromInputStream(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object obj = in.readObject();
        if (obj instanceof List<?> list) {
            if (list.isEmpty()) {
                return List.of();
            }
            else if (list.get(0) instanceof RencontreDTO) {
                @SuppressWarnings("unchecked")
                List<RencontreDTO> rencontres = (List<RencontreDTO>) list;
                return rencontres;
                // Traitez rencontres en toute sécurité ici
            } else {
                throw new ClassCastException("L'objet désérialisé n'est pas une List<Rencontre>.");
            }
        } else {
            throw new ClassCastException("L'objet désérialisé n'est pas une List.");
        }
    }


    private void ecouteClient(ObjectOutputStream out, ObjectInputStream in, Scanner scanner) throws IOException, ClassNotFoundException {
        while (true) {
            Object obj = in.readObject();

            if (obj instanceof String serveurMessage) {
                logger.info(SERVEUR_MESSAGE, serveurMessage);

                if (serveurMessage.startsWith("Tour")) {
                    traiterTour(out, scanner);
                } else if (serveurMessage.startsWith("Bye")) {
                    break;
                } else if (serveurMessage.startsWith("choisir une strategie automatique parmi")) {
                    traiterChoixStrategie(out, scanner);
                }
            }
        }
    }

    private void traiterTour(ObjectOutputStream out, Scanner scanner) throws IOException {
        TypeAction action = null;
        while (action == null) {
            logger.info("Choisissez votre action (COOPERER/TRAHIR/ABONDONNER): ");
            String actionInput = scanner.nextLine().toUpperCase();
            action = getTypeAction(null, actionInput);
        }
        envoyerObjet(out, action);
    }

    private void traiterChoixStrategie(ObjectOutputStream out, Scanner scanner) throws IOException {
        TypeStrategie typeStrategie = null;
        while (typeStrategie == null) {
            logger.info("[DonnantDonnant, DonnantDonnantAleatoire, DonnantPourDeuxDonnants, DonnantPourDeuxDonnantsAleatoire, " +
                    "SondeurNaif, SondeurRepentant, PacificateurNaif, VraiPacificateur, Aleatoire, ToujoursTrahir, ToujoursCooperer, " +
                    "RancunierStrategie, PavlovStrategie, PavlovAleatoire, Adaptatif, Graduel, DonnantDonnantSoupconneux, RancunierDoux]");
            logger.info("Choisissez votre stratégie automatique : ");
            String actionInput = scanner.nextLine().toUpperCase();
            typeStrategie = getTypeStrategie(actionInput); // Validation de l’entrée
        }
        envoyerObjet(out, typeStrategie); // Envoi de la stratégie valide
    }

    private TypeStrategie getTypeStrategie(String actionInput) {
        try {
            return TypeStrategie.valueOf(actionInput);
        } catch (IllegalArgumentException e) {
            logger.info("Stratégie invalide. Veuillez choisir une stratégie valide.");
            return null;
        }
    }

    private void envoyerObjet(ObjectOutputStream out, Object obj) throws IOException {
        out.writeObject(obj);
        out.flush();
    }



    private TypeAction getTypeAction(TypeAction action, String actionInput) {
        try {
            action = TypeAction.valueOf(actionInput);
        } catch (IllegalArgumentException e) {
            logger.info("Action invalide. Veuillez choisir COOPERER ou TRAHIR.");
        }
        return action;
    }

    public static void main(String[] args) {
        String host = "127.0.0.1"; // Adresse du serveur
        int port = 7842;

        Client client = new Client(host, port);
        client.start();
    }

    private int demanderNombre(Scanner scanner) {
        int nbTour;

        while (true) {
            logger.info("saisissez un nombre");
            String input = scanner.nextLine();
            if (isPositiveInteger(input)) {
                nbTour = Integer.parseInt(input);
                break;
            }
            else {
                logger.info("Le nombre choisis doit être un entier positif.");
            }

        }
        return nbTour;
    }

    private boolean isPositiveInteger(String input) {
        try {
            int number = Integer.parseInt(input);
            return number > 0;
        }
        catch (NumberFormatException e) {
            logger.info("Entrée invalide. Veuillez entrer un entier.");
            return false;
        }
    }
}
