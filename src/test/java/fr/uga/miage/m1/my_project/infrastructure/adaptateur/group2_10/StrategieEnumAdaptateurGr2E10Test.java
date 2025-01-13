package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_10;

import fr.uga.m1miage.pc.strategy.StrategyType;
import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_10.StrategieEnumAdaptateurGr2E10;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StrategieEnumAdaptateurGr2E10Test {

    @Test
    void testAdapter_withValidStrategyType() {
        assertEquals(TYPE_STRATEGIE.DONNANTDONNANT, StrategieEnumAdaptateurGr2E10.adapter(StrategyType.DONNANT_DONNANT),
                "La correspondance pour StrategyType.DONNANT_DONNANT doit être TypeStrategie.DONNANTDONNANT");

        assertEquals(TYPE_STRATEGIE.RANCUNIERSTRATEGIE, StrategieEnumAdaptateurGr2E10.adapter(StrategyType.RANCUNIER),
                "La correspondance pour StrategyType.RANCUNIER doit être TypeStrategie.RANCUNIERSTRATEGIE");

        assertEquals(TYPE_STRATEGIE.PAVLOVSTRATEGIE, StrategieEnumAdaptateurGr2E10.adapter(StrategyType.PAVLOV),
                "La correspondance pour StrategyType.PAVLOV doit être TypeStrategie.PAVLOVSTRATEGIE");
    }

    @Test
    void testAdapterInverse_withValidTypeStrategie() {
        assertEquals(StrategyType.DONNANT_DONNANT, StrategieEnumAdaptateurGr2E10.adapterInverse(TYPE_STRATEGIE.DONNANTDONNANT),
                "La correspondance inverse pour TypeStrategie.DONNANTDONNANT doit être StrategyType.DONNANT_DONNANT");

        assertEquals(StrategyType.RANCUNIER, StrategieEnumAdaptateurGr2E10.adapterInverse(TYPE_STRATEGIE.RANCUNIERSTRATEGIE),
                "La correspondance inverse pour TypeStrategie.RANCUNIERSTRATEGIE doit être StrategyType.RANCUNIER");

        assertEquals(StrategyType.PAVLOV, StrategieEnumAdaptateurGr2E10.adapterInverse(TYPE_STRATEGIE.PAVLOVSTRATEGIE),
                "La correspondance inverse pour TypeStrategie.PAVLOVSTRATEGIE doit être StrategyType.PAVLOV");
    }

    @Test
    void testAdapter_withAllMappings() {
        for (StrategyType strategyType : StrategyType.values()) {
            TYPE_STRATEGIE TYPESTRATEGIE = StrategieEnumAdaptateurGr2E10.adapter(strategyType);
            assertNotNull(TYPESTRATEGIE, "La correspondance pour " + strategyType + " ne doit pas être null");
        }
    }

    @Test
    void testAdapterInverse_withAllMappings() {
        for (TYPE_STRATEGIE TYPESTRATEGIE : TYPE_STRATEGIE.values()) {
            StrategyType strategyType = StrategieEnumAdaptateurGr2E10.adapterInverse(TYPESTRATEGIE);
            assertNotNull(strategyType, "La correspondance inverse pour " + TYPESTRATEGIE + " ne doit pas être null");
        }
    }
}
