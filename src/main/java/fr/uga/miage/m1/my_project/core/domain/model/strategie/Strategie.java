package fr.uga.miage.m1.my_project.core.domain.model.strategie;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_ACTION;
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

    public abstract TYPE_ACTION getAction(List<TYPE_ACTION> actions, int dernierResultat);

    public TYPE_ACTION getLastAction(List<TYPE_ACTION> actions) {
        return actions.get(actions.size() - 1);
    }
}
