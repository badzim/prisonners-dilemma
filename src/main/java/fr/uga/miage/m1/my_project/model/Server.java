package fr.uga.miage.m1.my_project.model;

import fr.uga.miage.m1.my_project.model.joueur.Joueur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {
    private static final int PORT = 7842;
    private static boolean isRunning = true;
    private List<Joueur> joueursAttente;
    private static final Logger logger = LoggerFactory.getLogger(Server.class.getName());


    public Server() {
        joueursAttente = new ArrayList<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Serveur en écoute sur le port {}", PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Connexion de {} : {}", clientSocket.getInetAddress(), clientSocket.getPort());

                ClientHandler handler = new ClientHandler(clientSocket, joueursAttente);
                handler.start();
                if (!isRunning) break;

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

        public static void main(String[] args) {
            new Server().start();
        }
}