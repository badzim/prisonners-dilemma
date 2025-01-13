package fr.uga.miage.m1.my_project.infrastructure.adaptateur.group2_10;


import fr.uga.miage.m1.my_project.core.domain.adaptater.group2_10.StrategieTypeActionAdaptateurGr2E10;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrategieTYPEACTIONAdaptateurTest {

    @Test
    void testAdapterStringToTypeAction_validInputs() {
        assertEquals(TYPE_ACTION.COOPERER, StrategieTypeActionAdaptateurGr2E10.adapter("c"),
                "La correspondance pour 'c' doit être TypeAction.COOPERER");
        assertEquals(TYPE_ACTION.TRAHIR, StrategieTypeActionAdaptateurGr2E10.adapter("t"),
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
        assertEquals("c", StrategieTypeActionAdaptateurGr2E10.adapter(TYPE_ACTION.COOPERER),
                "La correspondance pour TypeAction.COOPERER doit être 'c'");
        assertEquals("t", StrategieTypeActionAdaptateurGr2E10.adapter(TYPE_ACTION.TRAHIR),
                "La correspondance pour TypeAction.TRAHIR doit être 't'");
    }

    @Test
    void testAdapterListTypeActionToString_validInputs() {
        List<TYPE_ACTION> TYPEACTIONS = List.of(TYPE_ACTION.COOPERER, TYPE_ACTION.TRAHIR);
        List<String> expected = List.of("c", "t");

        assertEquals(expected, StrategieTypeActionAdaptateurGr2E10.adapter(TYPEACTIONS),
                "La liste des TypeAction doit être correctement convertie en liste de chaînes");
    }

    @Test
    void testAdapterListTypeActionToString_emptyList() {
        List<TYPE_ACTION> TYPEACTIONS = List.of();
        List<String> expected = List.of();

        assertEquals(expected, StrategieTypeActionAdaptateurGr2E10.adapter(TYPEACTIONS),
                "Une liste vide doit donner une liste vide");
    }


}