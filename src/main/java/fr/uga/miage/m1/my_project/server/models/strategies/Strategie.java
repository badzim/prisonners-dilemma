package fr.uga.miage.m1.my_project.server.models.strategies;

import fr.uga.miage.m1.my_project.server.models.enums.TypeAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.security.SecureRandom; // more secured random...

@Data
@AllArgsConstructor
public abstract class Strategie  {

    private String name;
    private SecureRandom random;
    private SecureRandom secondRandom;

    protected Strategie() {
        this(new SecureRandom());
    }

    protected Strategie(SecureRandom random) {
        this.name = this.getClass().getSimpleName();
        this.random = random;
    }

    protected Strategie(SecureRandom random, SecureRandom secondRandom) {
        this(random);
        this.secondRandom = secondRandom;

    }

    public abstract TypeAction getAction(List<TypeAction> actions, int dernierResultat);

    public TypeAction getLastAction(List<TypeAction> actions) {
        return actions.get(actions.size() - 1);
    }
}
