package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_5;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
import fr.uga.strats.g5_2.enums.Decision;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategieDecisionAdaptateurGr2E5Test {

    @Test
     void testAdapter_ValidDecision() {
        // Test avec une décision valide
        Decision decision = Decision.COOPERER;
        TypeAction action = StrategieDecisionAdaptateurGr2E5.adapter(decision);

        assertEquals(TypeAction.COOPERER, action);
    }
}