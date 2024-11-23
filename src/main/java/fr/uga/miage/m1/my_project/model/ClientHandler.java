package fr.uga.miage.m1.my_project.model;
import fr.uga.miage.m1.my_project.component.RencontreComponent;
import fr.uga.miage.m1.my_project.model.joueur.Joueur;
import fr.uga.miage.m1.my_project.service.ClientHandlerService;
import lombok.Data;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Data
public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final RencontreComponent rencontreComponent;
    private final ClientHandlerService clientHandlerService ;

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class.getName());


    public ClientHandler(Socket socket, RencontreComponent rencontreComponent, ClientHandlerService clientHandlerService) {
        this.clientSocket = socket;
        this.rencontreComponent = rencontreComponent;
        this.clientHandlerService = clientHandlerService;
    }

    @Override
    public void run() {
        try {
            // Initialiser les flux
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // Créer le joueur
            Joueur joueur = clientHandlerService.initializePlayer(out, in, clientSocket);

            // Boucle principale
            clientHandlerService.handlePlayerMenu(joueur, out, in, rencontreComponent);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}