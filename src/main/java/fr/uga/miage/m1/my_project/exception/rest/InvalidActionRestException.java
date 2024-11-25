package fr.uga.miage.m1.my_project.exception.rest;

import lombok.Data;

@Data
public class InvalidActionRestException extends RuntimeException {

    public InvalidActionRestException(String message) {
        super(message);
    }
}
