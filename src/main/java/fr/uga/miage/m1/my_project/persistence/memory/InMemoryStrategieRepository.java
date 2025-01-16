package fr.uga.miage.m1.my_project.persistence.memory;

import fr.uga.miage.m1.my_project.core.domain.model.enums.TYPE_STRATEGIE;
import fr.uga.miage.m1.my_project.core.domain.model.strategie.*;
import fr.uga.miage.m1.my_project.core.port.output.StrategieRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@Repository
@Primary
public class InMemoryStrategieRepository implements StrategieRepository {
    // Map des fournisseurs (Supplier) pour une instanciation "à la demande"
    private final Map<TYPE_STRATEGIE, Supplier<Strategie>> strategieSuppliers = new EnumMap<>(TYPE_STRATEGIE.class);

    public InMemoryStrategieRepository() {
        // Enregistrer chaque type de stratégie avec sa logique d'instanciation
        strategieSuppliers.put(TYPE_STRATEGIE.DONNANTDONNANT, DonnantDonnantStrategie::new);
        strategieSuppliers.put(TYPE_STRATEGIE.DONNANTDONNANTALEATOIRE, () -> new DonnantDonnantAleatoireStrategie(new SecureRandom()));
        strategieSuppliers.put(TYPE_STRATEGIE.DONNANTPOURDEUXDONNANTS, DonnantPourDeuxDonnantsStrategie::new);
        strategieSuppliers.put(TYPE_STRATEGIE.DONNANTPOURDEUXDONNANTSALEATOIRE,
                () -> new DonnantPourDeuxDonnantsEtAleatoireStrategie(new SecureRandom(), new SecureRandom()));
        strategieSuppliers.put(TYPE_STRATEGIE.SONDEURNAIF, () -> new SondeurNaifStrategie(new SecureRandom()));
        strategieSuppliers.put(TYPE_STRATEGIE.SONDEURREPENTANT, () -> new SondeurRepentantStrategie(new SecureRandom()));
        strategieSuppliers.put(TYPE_STRATEGIE.PACIFICATEURNAIF, () -> new PacificateurNaifStrategie(new SecureRandom()));
        strategieSuppliers.put(TYPE_STRATEGIE.VRAIPACIFICATEUR, () -> new VraiPacificateurStrategie(new SecureRandom()));
        strategieSuppliers.put(TYPE_STRATEGIE.ALEATOIRE, () -> new AleatoireStrategie(new SecureRandom()));
        strategieSuppliers.put(TYPE_STRATEGIE.TOUJOURSTRAHIR, ToujoursTrahirStrategie::new);
        strategieSuppliers.put(TYPE_STRATEGIE.TOUJOURSCOOPERER, ToujoursCoopererStrategie::new);
        strategieSuppliers.put(TYPE_STRATEGIE.RANCUNIERSTRATEGIE, RancunierStrategie::new);
        strategieSuppliers.put(TYPE_STRATEGIE.PAVLOVSTRATEGIE, PavlovStrategie::new);
        strategieSuppliers.put(TYPE_STRATEGIE.PAVLOVALEATOIRE, () -> new PavlovAleatoireStrategie(new SecureRandom()));
        strategieSuppliers.put(TYPE_STRATEGIE.ADAPTATIF, AdaptatifStrategie::new);
        strategieSuppliers.put(TYPE_STRATEGIE.GRADUEL, GraduelStrategie::new);
        strategieSuppliers.put(TYPE_STRATEGIE.DONNANTDONNANTSOUPCONNEUX, DonnantDonnantSoupconneuxStrategie::new);
        strategieSuppliers.put(TYPE_STRATEGIE.RANCUNIERDOUX, RancunierDouxStrategie::new);
    }

    // Méthode pour récupérer une stratégie à la demande
    public Strategie getStrategie(TYPE_STRATEGIE typeStrategie) {
        Supplier<Strategie> supplier = strategieSuppliers.get(typeStrategie);
        if (supplier == null) {
            throw new IllegalArgumentException("Stratégie inconnue : " + typeStrategie);
        }
        return supplier.get(); // Instancie la stratégie
    }
}
