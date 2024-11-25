package fr.uga.miage.m1.my_project.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RencontreDto {
    private String idRencontre;
    private String initiateur;
    private int nombreTours;
    private String etat; // Par exemple : "EN_ATTENTE", "COMPLÈTE"
}
