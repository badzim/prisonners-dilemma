package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5;

import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import fr.uga.strats.g5_2.models.Tour;

import java.util.ArrayList;
import java.util.List;

public class StrategieTourAdapterGr2E5 extends Tour {

    private StrategieTourAdapterGr2E5(){}
    // Constructeur de l'adaptateur
    public StrategieTourAdapterGr2E5(fr.uga.miage.m1.my_project.model.Tour tourIntern) {
        super(
                StrategieDecisionAdaptateurGr2E5.adapter(tourIntern.getActionInitiateur()),
                StrategieDecisionAdaptateurGr2E5.adapter(tourIntern.getActionAdversaire())
        );
    }

    // Méthode statique pour adapter un objet `Tour` interne vers un `Tour` externe
    public static Tour adapter(fr.uga.miage.m1.my_project.model.Tour tourIntern) {
        return new StrategieTourAdapterGr2E5(tourIntern);
    }

    // Méthode statique pour adapter une liste d'objets `Tour` internes vers un tableau de `Tour` externes
    public static Tour[] adapter(List<fr.uga.miage.m1.my_project.model.Tour> toursIntern) {
        // Utilisation de Java Stream API pour convertir la liste
        return toursIntern.stream()
                .map(StrategieTourAdapterGr2E5::adapter) // Appelle la méthode `adapter` pour chaque élément
                .toArray(Tour[]::new); // Convertit le Stream en tableau de `Tour`
    }

    // Méthode pour construire une liste de `Tour`
    public static Tour [] construireTours(List<TypeAction> actionsJoueur, List<TypeAction> actionsAdversaire) {
        List<Tour> tours = new ArrayList<>();
        int taille = Math.min(actionsJoueur.size(), actionsAdversaire.size());

        for (int i = 0; i < taille; i++) {
            tours.add(new Tour(
                    StrategieDecisionAdaptateurGr2E5.adapter(actionsJoueur.get(i)),
                    StrategieDecisionAdaptateurGr2E5.adapter(actionsAdversaire.get(i))
            ));
        }

        return tours.toArray(Tour[]::new);
    }
}
