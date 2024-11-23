package fr.uga.miage.m1.my_project.model.joueur;

import fr.uga.miage.m1.my_project.exception.MessageEnvoiException;
import fr.uga.miage.m1.my_project.exception.MessageRecuException;
import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Humain extends Joueur {

    protected Socket socket;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;
    private static final Logger logger = LoggerFactory.getLogger(Humain.class);

    public Humain(String id, String nom, Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        super(id, nom);
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    @Override
    public TypeAction jouer(List<TypeAction> historiqueAdversaire, int dernierResultat)  {
        // Attendre que le client envoie l'action
        Object obj = receiveMessage();

        if (obj instanceof TypeAction typeAction) {
            logger.info("{}", typeAction);
            return typeAction;
        }
        return TypeAction.COOPERER; // Valeur par défaut
    }

    @Override
    public void sendMessage(Object obj)  {
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            throw new MessageEnvoiException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Object receiveMessage() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new MessageRecuException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            // Ignorer
        }
    }
}