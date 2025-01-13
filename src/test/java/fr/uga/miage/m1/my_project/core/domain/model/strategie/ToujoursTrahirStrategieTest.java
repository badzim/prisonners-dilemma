package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToujoursTrahirStrategieTest {

    private ToujoursTrahirStrategie strategie;

    @BeforeEach
    void setUp() {
        strategie = new ToujoursTrahirStrategie();
    }

    @Test
    void testAlwaysBetray() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        // Ajouter des actions à la liste, mais cela ne doit pas affecter le résultat
        actions.add(TYPE_ACTION.COOPERER);
        actions.add(TYPE_ACTION.TRAHIR);

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie retourne toujours TRAHIR
        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit toujours retourner TRAHIR, peu importe les actions précédentes.");
    }

    @Test
    void testAlwaysBetrayWithEmptyList() {
        List<TYPE_ACTION> actions = new ArrayList<>();

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie retourne TRAHIR même avec une liste vide
        assertEquals(TYPE_ACTION.TRAHIR, result, "La stratégie doit toujours retourner TRAHIR, même avec une liste vide.");
    }
}
