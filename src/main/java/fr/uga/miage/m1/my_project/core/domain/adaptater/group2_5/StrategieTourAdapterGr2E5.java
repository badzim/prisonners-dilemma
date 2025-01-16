package fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import fr.uga.strats.g5_2.models.Tour;
import java.util.ArrayList;
import java.util.List;

public class StrategieTourAdapterGr2E5 extends Tour {

    private StrategieTourAdapterGr2E5(){}
    // Constructeur de l'adaptateur
    public StrategieTourAdapterGr2E5(fr.uga.miage.m1.my_project.core.domain.model.Tour tourIntern) {
        super(
                StrategieDecisionAdaptateurGr2E5.adapter(tourIntern.getActionInitiateur()),
                StrategieDecisionAdaptateurGr2E5.adapter(tourIntern.getActionAdversaire())
        );
    }

    // Méthode statique pour adapter un objet `Tour` interne vers un `Tour` externe
    public static Tour adapter(fr.uga.miage.m1.my_project.core.domain.model.Tour tourIntern) {
        return new StrategieTourAdapterGr2E5(tourIntern);
    }

    // Méthode pour construire une liste de `Tour`
    public static Tour [] construireTours(List<TYPE_ACTION> actionsJoueur, List<TYPE_ACTION> actionsAdversaire) {
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
