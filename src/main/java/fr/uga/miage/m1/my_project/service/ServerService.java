package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.component.RencontreComponent;
import fr.uga.miage.m1.my_project.model.ClientHandler;
import fr.uga.miage.m1.my_project.model.Server;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ServerSocket;
import java.net.Socket;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final RencontreService rencontreService;
    private final ClientHandlerService clientHandlerService;

    public void start(Server server) {
        Logger logger = LoggerFactory.getLogger(ClientHandler.class.getName());
        int serverPort = server.getPORT();
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            logger.info("Serveur en écoute sur le port {}", serverPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Connexion de {} : {}", clientSocket.getInetAddress(), clientSocket.getPort());
                ClientHandler handler = new ClientHandler(clientSocket, new RencontreComponent(rencontreService), clientHandlerService);
                handler.start();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
