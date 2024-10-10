package fr.uga.miage.m1.my_project.responses;



import lombok.Data;

@Data
public class JoueurResponse {
    private Long id;

    private String nom;

    private int score;
}
