package fr.uga.miage.m1.my_project.core.exception.rest;

import lombok.Data;

@Data
public class RencontreNotFoundRestException extends RuntimeException {

    private final String rencontreId;
    public RencontreNotFoundRestException(String message, String rencontreId) {
      super(message);
      this.rencontreId = rencontreId;
    }
}
