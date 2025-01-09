package fr.uga.miage.m1.my_project.core.exception.rest;

import lombok.Data;

@Data
public class ClientIdUsedRestException extends RuntimeException {

    private final String clientId;

    public ClientIdUsedRestException(String message, String clientId) {
        super(message);
        this.clientId = clientId;
    }
}
