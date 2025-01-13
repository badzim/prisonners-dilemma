package fr.uga.miage.m1.my_project.core.domain.model;
import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_RENCONTRE;
import fr.uga.miage.m1.my_project.core.domain.model.joueur.Joueur;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
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
    private List<TYPE_ACTION> historiqueInitiateur;
    private List<TYPE_ACTION> historiqueAdversaire;
    private List<Tour> tours;
    private Tour currentTour;
    private ETAT_RENCONTRE etatRencontre;

    public Rencontre() {
        this.idRencontre = UUID.randomUUID().toString();
        this.initiateur = null;
        this.adversaire = null;
        this.nombreTours = 0;
        this.historiqueInitiateur = new ArrayList<>();
        this.historiqueAdversaire = new ArrayList<>();
        this.tours = new ArrayList<>();
        this.currentTour = null;
        this.etatRencontre = ETAT_RENCONTRE.EN_ATTENTE;
    }
}