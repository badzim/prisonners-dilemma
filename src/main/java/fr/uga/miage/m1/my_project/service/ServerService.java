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
    private static final boolean ISRUNNING = true;
    private final RencontreService rencontreService;
    private final ClientHandlerService clientHandlerService;

    public void start() {
        Logger logger = LoggerFactory.getLogger(ClientHandler.class.getName());
        int serverPort = Server.PORT;
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            logger.info("Serveur en écoute sur le port {}", serverPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Connexion de {} : {}", clientSocket.getInetAddress(), clientSocket.getPort());
                ClientHandler handler = new ClientHandler(clientSocket, new RencontreComponent(rencontreService), clientHandlerService);
                handler.start();
                if (!ISRUNNING) break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
