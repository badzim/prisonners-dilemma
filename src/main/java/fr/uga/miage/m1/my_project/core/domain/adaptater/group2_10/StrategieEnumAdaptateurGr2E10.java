package fr.uga.miage.m1.my_project.core.domain.adaptater.group2_10;

import fr.uga.m1miage.pc.strategy.StrategyType;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;

import java.util.EnumMap;
import java.util.Map;

public class StrategieEnumAdaptateurGr2E10 {
    private static final Map<StrategyType, TYPE_STRATEGIE> correspondance = new EnumMap<>(StrategyType.class);
    private static final Map<TYPE_STRATEGIE, StrategyType> correspondanceInverse = new EnumMap<>(TYPE_STRATEGIE.class);

    static {
        correspondance.put(StrategyType.DONNANT_DONNANT, TYPE_STRATEGIE.DONNANTDONNANT);
        correspondance.put(StrategyType.DONNANT_DONNANT_ALEATOIRE, TYPE_STRATEGIE.DONNANTDONNANTALEATOIRE);
        correspondance.put(StrategyType.DONNANT_DEUX_DONNANT, TYPE_STRATEGIE.DONNANTPOURDEUXDONNANTS);
        correspondance.put(StrategyType.DONNANT_DEUX_DONNANT_ALEATOIRE, TYPE_STRATEGIE.DONNANTPOURDEUXDONNANTSALEATOIRE);
        correspondance.put(StrategyType.SONDEUR_NAIF, TYPE_STRATEGIE.SONDEURNAIF);
        correspondance.put(StrategyType.SONDEUR_REPENTANT, TYPE_STRATEGIE.SONDEURREPENTANT);
        correspondance.put(StrategyType.PACIFICATEUR_NAIF, TYPE_STRATEGIE.PACIFICATEURNAIF);
        correspondance.put(StrategyType.VRAI_PACIFICATEUR, TYPE_STRATEGIE.VRAIPACIFICATEUR);
        correspondance.put(StrategyType.ALEATOIRE, TYPE_STRATEGIE.ALEATOIRE);
        correspondance.put(StrategyType.TOUJOURS_TRAHIR, TYPE_STRATEGIE.TOUJOURSTRAHIR);
        correspondance.put(StrategyType.TOUJOURS_COOPERER, TYPE_STRATEGIE.TOUJOURSCOOPERER);
        correspondance.put(StrategyType.RANCUNIER, TYPE_STRATEGIE.RANCUNIERSTRATEGIE);
        correspondance.put(StrategyType.PAVLOV, TYPE_STRATEGIE.PAVLOVSTRATEGIE);
        correspondance.put(StrategyType.PAVLOV_ALEATOIRE, TYPE_STRATEGIE.PAVLOVALEATOIRE);
        correspondance.put(StrategyType.ADAPTATIF, TYPE_STRATEGIE.ADAPTATIF);
        correspondance.put(StrategyType.GRADUEL, TYPE_STRATEGIE.GRADUEL);
        correspondance.put(StrategyType.DONNANT_DONNANT_SOUPCONNEUX, TYPE_STRATEGIE.DONNANTDONNANTSOUPCONNEUX);
        correspondance.put(StrategyType.RANCUNIER_DOUX, TYPE_STRATEGIE.RANCUNIERDOUX);

        correspondance.forEach((key, value) -> correspondanceInverse.put(value, key));

    }

    private StrategieEnumAdaptateurGr2E10(){}

    public static TYPE_STRATEGIE adapter(StrategyType externeEnum) {
        TYPE_STRATEGIE strategie = correspondance.get(externeEnum);
        if (strategie == null) {
            throw new IllegalArgumentException("Aucune correspondance trouvée pour : " + externeEnum);
        }
        return strategie;
    }

    // Méthode pour adapter une stratégie interne vers externe
    public static StrategyType adapterInverse(TYPE_STRATEGIE interneEnum) {
        StrategyType strategie = correspondanceInverse.get(interneEnum);
        if (strategie == null) {
            throw new IllegalArgumentException("Aucune correspondance inverse trouvée pour : " + interneEnum);
        }
        return strategie;
    }

}
