package fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import java.util.EnumMap;
import java.util.Map;

public class StrategieEnumAdaptateurGr2E5 {
    private static final Map<fr.uga.strats.g5_2.enums.TypeStrategie, TYPE_STRATEGIE> correspondance = new EnumMap<>(fr.uga.strats.g5_2.enums.TypeStrategie.class);
    private static final Map<TYPE_STRATEGIE, fr.uga.strats.g5_2.enums.TypeStrategie> correspondanceInverse = new EnumMap<>(TYPE_STRATEGIE.class);

    static {
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.DONNANT_DONNANT, TYPE_STRATEGIE.DONNANTDONNANT);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.DONNANT_DONNANT_ALEATOIRE, TYPE_STRATEGIE.DONNANTDONNANTALEATOIRE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.DONNANT_POUR_DEUX_DONNANTS, TYPE_STRATEGIE.DONNANTPOURDEUXDONNANTS);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.DONNANT_POUR_DEUX_DONNANTS_ALEATOIRE, TYPE_STRATEGIE.DONNANTPOURDEUXDONNANTSALEATOIRE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.SONDEUR_NAIF, TYPE_STRATEGIE.SONDEURNAIF);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.SONDEUR_REPENTANT, TYPE_STRATEGIE.SONDEURREPENTANT);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.PACIFICATEUR_NAIF, TYPE_STRATEGIE.PACIFICATEURNAIF);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.VRAI_PACIFICATEUR, TYPE_STRATEGIE.VRAIPACIFICATEUR);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.ALEATOIRE, TYPE_STRATEGIE.ALEATOIRE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.TOUJOURS_TRAHIR, TYPE_STRATEGIE.TOUJOURSTRAHIR);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.TOUJOURS_COOPERER, TYPE_STRATEGIE.TOUJOURSCOOPERER);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.RANCUNIER, TYPE_STRATEGIE.RANCUNIERSTRATEGIE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.PAVLOV, TYPE_STRATEGIE.PAVLOVSTRATEGIE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.PAVLOV_ALEATOIRE, TYPE_STRATEGIE.PAVLOVALEATOIRE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.ADAPTATIF, TYPE_STRATEGIE.ADAPTATIF);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.GRADUEL, TYPE_STRATEGIE.GRADUEL);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.DONNANT_DONNANT_SOUPCONNEUX, TYPE_STRATEGIE.DONNANTDONNANTSOUPCONNEUX);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.RANCUNIER_DOUX, TYPE_STRATEGIE.RANCUNIERDOUX);

        correspondance.forEach((key, value) -> correspondanceInverse.put(value, key));

    }

    private StrategieEnumAdaptateurGr2E5(){}

    public static TYPE_STRATEGIE adapter(fr.uga.strats.g5_2.enums.TypeStrategie externeEnum) {
        TYPE_STRATEGIE strategie = correspondance.get(externeEnum);
        if (strategie == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + externeEnum);
        }
        return strategie;
    }

    // Méthode pour adapter une stratégie interne vers externe
    public static fr.uga.strats.g5_2.enums.TypeStrategie adapterInverse(TYPE_STRATEGIE interneEnum) {
        fr.uga.strats.g5_2.enums.TypeStrategie strategie = correspondanceInverse.get(interneEnum);
        if (strategie == null) {
            throw new IllegalArgumentException("Aucune correspondance inverse trouvée pour : " + interneEnum);
        }
        return strategie;
    }

}
