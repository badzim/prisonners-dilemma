package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_10;

import fr.uga.m1miage.pc.strategy.StrategyType;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeStrategie;

import java.util.EnumMap;
import java.util.Map;

public class StrategieEnumAdaptateurGr2E10 {
    private static final Map<StrategyType, TypeStrategie> correspondance = new EnumMap<>(StrategyType.class);
    private static final Map<TypeStrategie, StrategyType> correspondanceInverse = new EnumMap<>(TypeStrategie.class);

    static {
        correspondance.put(StrategyType.DONNANT_DONNANT, TypeStrategie.DONNANTDONNANT);
        correspondance.put(StrategyType.DONNANT_DONNANT_ALEATOIRE, TypeStrategie.DONNANTDONNANTALEATOIRE);
        correspondance.put(StrategyType.DONNANT_DEUX_DONNANT, TypeStrategie.DONNANTPOURDEUXDONNANTS);
        correspondance.put(StrategyType.DONNANT_DEUX_DONNANT_ALEATOIRE, TypeStrategie.DONNANTPOURDEUXDONNANTSALEATOIRE);
        correspondance.put(StrategyType.SONDEUR_NAIF, TypeStrategie.SONDEURNAIF);
        correspondance.put(StrategyType.SONDEUR_REPENTANT, TypeStrategie.SONDEURREPENTANT);
        correspondance.put(StrategyType.PACIFICATEUR_NAIF, TypeStrategie.PACIFICATEURNAIF);
        correspondance.put(StrategyType.VRAI_PACIFICATEUR, TypeStrategie.VRAIPACIFICATEUR);
        correspondance.put(StrategyType.ALEATOIRE, TypeStrategie.ALEATOIRE);
        correspondance.put(StrategyType.TOUJOURS_TRAHIR, TypeStrategie.TOUJOURSTRAHIR);
        correspondance.put(StrategyType.TOUJOURS_COOPERER, TypeStrategie.TOUJOURSCOOPERER);
        correspondance.put(StrategyType.RANCUNIER, TypeStrategie.RANCUNIERSTRATEGIE);
        correspondance.put(StrategyType.PAVLOV, TypeStrategie.PAVLOVSTRATEGIE);
        correspondance.put(StrategyType.PAVLOV_ALEATOIRE, TypeStrategie.PAVLOVALEATOIRE);
        correspondance.put(StrategyType.ADAPTATIF, TypeStrategie.ADAPTATIF);
        correspondance.put(StrategyType.GRADUEL, TypeStrategie.GRADUEL);
        correspondance.put(StrategyType.DONNANT_DONNANT_SOUPCONNEUX, TypeStrategie.DONNANTDONNANTSOUPCONNEUX);
        correspondance.put(StrategyType.RANCUNIER_DOUX, TypeStrategie.RANCUNIERDOUX);

        correspondance.forEach((key, value) -> correspondanceInverse.put(value, key));

    }

    private StrategieEnumAdaptateurGr2E10(){}

    public static TypeStrategie adapter(StrategyType externeEnum) {
        TypeStrategie strategie = correspondance.get(externeEnum);
        if (strategie == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + externeEnum);
        }
        return strategie;
    }

    // Méthode pour adapter une stratégie interne vers externe
    public static StrategyType adapterInverse(TypeStrategie interneEnum) {
        StrategyType strategie = correspondanceInverse.get(interneEnum);
        if (strategie == null) {
            throw new IllegalArgumentException("Aucune correspondance inverse trouvée pour : " + interneEnum);
        }
        return strategie;
    }

}
