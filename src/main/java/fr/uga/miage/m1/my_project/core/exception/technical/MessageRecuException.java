package fr.uga.miage.m1.my_project.core.exception.technical;

public class MessageRecuException extends RuntimeException {
    public MessageRecuException(String message) {
        super(message);
    }

    public MessageRecuException(String message, Throwable cause) {
        super(message, cause);
    }
}
