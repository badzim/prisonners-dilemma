package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_10;


import fr.uga.miage.m1.my_project.model.enums.TypeAction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrategieTypeActionAdaptateurTest {

    @Test
    void testAdapterStringToTypeAction_validInputs() {
        assertEquals(TypeAction.COOPERER, StrategieTypeActionAdaptateurGr2E10.adapter("c"),
                "La correspondance pour 'c' doit être TypeAction.COOPERER");
        assertEquals(TypeAction.TRAHIR, StrategieTypeActionAdaptateurGr2E10.adapter("t"),
                "La correspondance pour 't' doit être TypeAction.TRAHIR");
    }

    @Test
    void testAdapterStringToTypeAction_invalidInput() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                        StrategieTypeActionAdaptateurGr2E10.adapter("x"),
                "Une exception doit être levée pour une valeur invalide comme 'x'");
        assertEquals("Aucune correspondance trouvée pour : x", exception.getMessage());
    }

    @Test
    void testAdapterTypeActionToString_validInputs() {
        assertEquals("c", StrategieTypeActionAdaptateurGr2E10.adapter(TypeAction.COOPERER),
                "La correspondance pour TypeAction.COOPERER doit être 'c'");
        assertEquals("t", StrategieTypeActionAdaptateurGr2E10.adapter(TypeAction.TRAHIR),
                "La correspondance pour TypeAction.TRAHIR doit être 't'");
    }

    @Test
    void testAdapterListTypeActionToString_validInputs() {
        List<TypeAction> typeActions = List.of(TypeAction.COOPERER, TypeAction.TRAHIR);
        List<String> expected = List.of("c", "t");

        assertEquals(expected, StrategieTypeActionAdaptateurGr2E10.adapter(typeActions),
                "La liste des TypeAction doit être correctement convertie en liste de chaînes");
    }

    @Test
    void testAdapterListTypeActionToString_emptyList() {
        List<TypeAction> typeActions = List.of();
        List<String> expected = List.of();

        assertEquals(expected, StrategieTypeActionAdaptateurGr2E10.adapter(typeActions),
                "Une liste vide doit donner une liste vide");
    }


}