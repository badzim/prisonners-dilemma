package fr.uga.miage.m1.my_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RencontreDTO implements Serializable {
    private int idRencontre;
    private int nombreTour;
}
