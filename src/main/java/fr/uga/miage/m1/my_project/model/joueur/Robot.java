package fr.uga.miage.m1.my_project.model.joueur;

import fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5.StrategieAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5.StrategieTourAdapterGr2E5;
import fr.uga.miage.m1.my_project.model.enums.EtatJoueur;
import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import fr.uga.miage.m1.my_project.model.strategie.Strategie;
import fr.uga.strats.g5_2.models.Tour;

import java.util.List;

public class Robot extends Joueur {

    public Robot(String id, String nom, int score) {
        super(id, nom);
        this.score = score;
    }

    public Robot(String id, String nom, int score, Strategie strategie, EtatJoueur etat) {
        this(id, nom, score);
        this.strategieAutomatique = strategie;
        this.etat = etat;
    }

    @Override
    public TypeAction jouer(List<TypeAction> historiqueAdversaire, int dernierResultat) {
        if (strategieAutomatique instanceof StrategieAdaptateurGr2E5 adaptateur) {
            // Appel à la stratégie externe

            // Construction de la liste des `Tour` (historique complet des actions)
            TypeAction result = null;
            Tour[] tours = StrategieTourAdapterGr2E5.construireTours(getHistoriqueJoueur(), historiqueAdversaire);
            if (this.getEtat() == EtatJoueur.EN_PARTIE_INITIATEUR) result = adaptateur.getAction(tours, 1, 2);
            else result = adaptateur.getAction(tours, 2, 1);
            getHistoriqueJoueur().add(result);
            return result;
        }

        // Si la stratégie est interne
        if (strategieAutomatique != null) {
            return strategieAutomatique.getAction(historiqueAdversaire, dernierResultat);
        }

        return TypeAction.COOPERER; // Valeur par défaut
    }
}