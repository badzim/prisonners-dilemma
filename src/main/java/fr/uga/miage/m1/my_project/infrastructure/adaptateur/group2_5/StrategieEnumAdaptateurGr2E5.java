package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5;

import fr.uga.miage.m1.my_project.model.enums.TypeStrategie;
import java.util.EnumMap;
import java.util.Map;

public class StrategieEnumAdaptateurGr2E5 {
    private static final Map<fr.uga.strats.g5_2.enums.TypeStrategie, TypeStrategie> correspondance = new EnumMap<>(fr.uga.strats.g5_2.enums.TypeStrategie.class);
    private static final Map<TypeStrategie, fr.uga.strats.g5_2.enums.TypeStrategie> correspondanceInverse = new EnumMap<>(TypeStrategie.class);

    static {
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.DONNANT_DONNANT, TypeStrategie.DONNANTDONNANT);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.DONNANT_DONNANT_ALEATOIRE, TypeStrategie.DONNANTDONNANTALEATOIRE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.DONNANT_POUR_DEUX_DONNANTS, TypeStrategie.DONNANTPOURDEUXDONNANTS);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.DONNANT_POUR_DEUX_DONNANTS_ALEATOIRE, TypeStrategie.DONNANTPOURDEUXDONNANTSALEATOIRE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.SONDEUR_NAIF, TypeStrategie.SONDEURNAIF);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.SONDEUR_REPENTANT, TypeStrategie.SONDEURREPENTANT);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.PACIFICATEUR_NAIF, TypeStrategie.PACIFICATEURNAIF);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.VRAI_PACIFICATEUR, TypeStrategie.VRAIPACIFICATEUR);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.ALEATOIRE, TypeStrategie.ALEATOIRE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.TOUJOURS_TRAHIR, TypeStrategie.TOUJOURSTRAHIR);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.TOUJOURS_COOPERER, TypeStrategie.TOUJOURSCOOPERER);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.RANCUNIER, TypeStrategie.RANCUNIERSTRATEGIE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.PAVLOV, TypeStrategie.PAVLOVSTRATEGIE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.PAVLOV_ALEATOIRE, TypeStrategie.PAVLOVALEATOIRE);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.ADAPTATIF, TypeStrategie.ADAPTATIF);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.GRADUEL, TypeStrategie.GRADUEL);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.DONNANT_DONNANT_SOUPCONNEUX, TypeStrategie.DONNANTDONNANTSOUPCONNEUX);
        correspondance.put(fr.uga.strats.g5_2.enums.TypeStrategie.RANCUNIER_DOUX, TypeStrategie.RANCUNIERDOUX);

        correspondance.forEach((key, value) -> correspondanceInverse.put(value, key));

    }

    private StrategieEnumAdaptateurGr2E5(){}

    public static TypeStrategie adapter(fr.uga.strats.g5_2.enums.TypeStrategie externeEnum) {
        TypeStrategie strategie = correspondance.get(externeEnum);
        if (strategie == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + externeEnum);
        }
        return strategie;
    }

    // Méthode pour adapter une stratégie interne vers externe
    public static fr.uga.strats.g5_2.enums.TypeStrategie adapterInverse(TypeStrategie interneEnum) {
        fr.uga.strats.g5_2.enums.TypeStrategie strategie = correspondanceInverse.get(interneEnum);
        if (strategie == null) {
            throw new IllegalArgumentException("Aucune correspondance inverse trouvée pour : " + interneEnum);
        }
        return strategie;
    }

}
