package fr.uga.miage.m1.my_project.service;


import fr.uga.miage.m1.my_project.model.enums.TypeStrategie;
import fr.uga.miage.m1.my_project.model.strategie.*;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class StrategieFactoryService {

    // Map des fournisseurs (Supplier) pour une instanciation "à la demande"
    private final Map<TypeStrategie, Supplier<Strategie>> strategieSuppliers = new HashMap<>();

    public StrategieFactoryService() {
        // Enregistrer chaque type de stratégie avec sa logique d'instanciation
        strategieSuppliers.put(TypeStrategie.DONNANTDONNANT, DonnantDonnantStrategie::new);
        strategieSuppliers.put(TypeStrategie.DONNANTDONNANTALEATOIRE, () -> new DonnantDonnantAleatoireStrategie(new SecureRandom()));
        strategieSuppliers.put(TypeStrategie.DONNANTPOURDEUXDONNANTS, DonnantPourDeuxDonnantsStrategie::new);
        strategieSuppliers.put(TypeStrategie.DONNANTPOURDEUXDONNANTSALEATOIRE,
                () -> new DonnantPourDeuxDonnantsEtAleatoireStrategie(new SecureRandom(), new SecureRandom()));
        strategieSuppliers.put(TypeStrategie.SONDEURNAIF, () -> new SondeurNaifStrategie(new SecureRandom()));
        strategieSuppliers.put(TypeStrategie.SONDEURREPENTANT, () -> new SondeurRepentantStrategie(new SecureRandom()));
        strategieSuppliers.put(TypeStrategie.PACIFICATEURNAIF, () -> new PacificateurNaifStrategie(new SecureRandom()));
        strategieSuppliers.put(TypeStrategie.VRAIPACIFICATEUR, () -> new VraiPacificateurStrategie(new SecureRandom()));
        strategieSuppliers.put(TypeStrategie.ALEATOIRE, () -> new AleatoireStrategie(new SecureRandom()));
        strategieSuppliers.put(TypeStrategie.TOUJOURSTRAHIR, ToujoursTrahirStrategie::new);
        strategieSuppliers.put(TypeStrategie.TOUJOURSCOOPERER, ToujoursCoopererStrategie::new);
        strategieSuppliers.put(TypeStrategie.RANCUNIERSTRATEGIE, RancunierStrategie::new);
        strategieSuppliers.put(TypeStrategie.PAVLOVSTRATEGIE, PavlovStrategie::new);
        strategieSuppliers.put(TypeStrategie.PAVLOVALEATOIRE, () -> new PavlovAleatoireStrategie(new SecureRandom()));
        strategieSuppliers.put(TypeStrategie.ADAPTATIF, AdaptatifStrategie::new);
        strategieSuppliers.put(TypeStrategie.GRADUEL, GraduelStrategie::new);
        strategieSuppliers.put(TypeStrategie.DONNANTDONNANTSOUPCONNEUX, DonnantDonnantSoupconneuxStrategie::new);
        strategieSuppliers.put(TypeStrategie.RANCUNIERDOUX, RancunierDouxStrategie::new);
    }

    // Méthode pour récupérer une stratégie à la demande
    public Strategie getStrategie(TypeStrategie typeStrategie) {
        Supplier<Strategie> supplier = strategieSuppliers.get(typeStrategie);
        if (supplier == null) {
            throw new IllegalArgumentException("Stratégie inconnue : " + typeStrategie);
        }
        return supplier.get(); // Instancie la stratégie
    }
}
