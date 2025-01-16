package fr.uga.miage.m1.my_project.core.domain.model.strategie;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ToujoursCoopererStrategieTest {

    private ToujoursCoopererStrategie strategie;

    @BeforeEach
    void setUp() {
        strategie = new ToujoursCoopererStrategie();
    }

    @Test
    void testAlwaysCooperate() {
        List<TYPE_ACTION> actions = new ArrayList<>();
        // On peut ajouter des actions à la liste, mais cela ne devrait pas affecter le résultat
        actions.add(TYPE_ACTION.TRAHIR);
        actions.add(TYPE_ACTION.COOPERER);

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie retourne toujours COOPERER
        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit toujours retourner COOPERER, peu importe les actions précédentes.");
    }

    @Test
    void testAlwaysCooperateWithEmptyList() {
        List<TYPE_ACTION> actions = new ArrayList<>();

        TYPE_ACTION result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie retourne COOPERER même avec une liste vide
        assertEquals(TYPE_ACTION.COOPERER, result, "La stratégie doit toujours retourner COOPERER, même avec une liste vide.");
    }
}

