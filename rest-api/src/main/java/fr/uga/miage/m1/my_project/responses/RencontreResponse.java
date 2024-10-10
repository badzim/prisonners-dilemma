package fr.uga.miage.m1.my_project.responses;


import fr.uga.miage.m1.my_project.enums.EtatRencontre;
import lombok.Data;

@Data
public class RencontreResponse {
    private Long rencontreId;
    private String joueur1;
    private String joueur2;
    private Integer nombreTours;
    private  EtatRencontre etat;
}
