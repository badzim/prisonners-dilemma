package fr.uga.miage.m1.my_project.core.domain.adapter.group2_5;

import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_5.StrategieEnumAdaptateurGr2E5;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategieEnumAdaptateurGr2E5Test {

    @Test
    void testAdapter_ValidEnum() {
        // Test avec une stratégie externe valide
        fr.uga.strats.g5_2.enums.TypeStrategie externeEnum = fr.uga.strats.g5_2.enums.TypeStrategie.DONNANT_DONNANT;
        TYPE_STRATEGIE interneEnum = StrategieEnumAdaptateurGr2E5.adapter(externeEnum);

        assertEquals(TYPE_STRATEGIE.DONNANTDONNANT, interneEnum);
    }
}