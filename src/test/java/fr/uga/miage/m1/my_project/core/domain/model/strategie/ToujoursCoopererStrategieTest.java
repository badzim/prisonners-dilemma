package fr.uga.miage.m1.my_project.core.domain.model.strategie;
import fr.uga.miage.m1.my_project.core.domain.model.enums.TypeAction;
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
        List<TypeAction> actions = new ArrayList<>();
        // On peut ajouter des actions à la liste, mais cela ne devrait pas affecter le résultat
        actions.add(TypeAction.TRAHIR);
        actions.add(TypeAction.COOPERER);

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie retourne toujours COOPERER
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit toujours retourner COOPERER, peu importe les actions précédentes.");
    }

    @Test
    void testAlwaysCooperateWithEmptyList() {
        List<TypeAction> actions = new ArrayList<>();

        TypeAction result = strategie.getAction(actions, 0);

        // Vérifie que la stratégie retourne COOPERER même avec une liste vide
        assertEquals(TypeAction.COOPERER, result, "La stratégie doit toujours retourner COOPERER, même avec une liste vide.");
    }
}

