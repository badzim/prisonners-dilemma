package fr.uga.miage.m1.my_project.model;
import fr.uga.miage.m1.my_project.model.joueur.Joueur;
import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Rencontre{
    private String idRencontre;
    private Joueur initiateur;
    private Joueur adversaire;
    private int nombreTours;
    private List<TypeAction> historiqueInitiateur;
    private List<TypeAction> historiqueAdversaire;
    private List<Tour> tours;

    public Rencontre() {
        this.idRencontre = UUID.randomUUID().toString();
        this.initiateur = null;
        this.adversaire = null;
        this.nombreTours = 0;
        this.historiqueInitiateur = new ArrayList<>();
        this.historiqueAdversaire = new ArrayList<>();
        this.tours = new ArrayList<>();
    }
    
    public Rencontre(Joueur initiateur, int nombreTours) {
        this();
        this.initiateur = initiateur;
        this.nombreTours = nombreTours;
    }



}