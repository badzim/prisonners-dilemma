package fr.uga.miage.m1.my_project.persistence.memory;

import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_RENCONTRE;
import fr.uga.miage.m1.my_project.core.exception.rest.RencontreNotFoundRestException;
import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;
import fr.uga.miage.m1.my_project.core.port.output.RencontreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Primary
public class InMemoryRencontreRepository implements RencontreRepository {
    // Map synchronisée pour stocker les rencontres par ID
    private Map<String, Rencontre> rencontres = Collections.synchronizedMap(new HashMap<>());

    // Map synchronisée pour stocker les IDs de rencontres par client
    private Map<String, List<String>> rencontresParClient = Collections.synchronizedMap(new HashMap<>());

    // Méthode pour ajouter une rencontre en attente
    public void addRencontre(String rencontreId, Rencontre rencontre) {
        rencontre.setEtatRencontre(ETAT_RENCONTRE.EN_ATTENTE);

        // Synchronisation pour éviter les problèmes de concurrence
        synchronized (rencontres) {
            // Ajoute la rencontre à la Map des rencontres
            rencontres.put(rencontre.getIdRencontre(), rencontre);

            // Ajoute l'ID de la rencontre à la liste des rencontres du client
            rencontresParClient.computeIfAbsent(rencontreId, k -> new ArrayList<>()).add(rencontre.getIdRencontre());
        }
    }

    // Méthode pour ajouter une rencontre en attente
    public void addRencontreParClient(String clientId, Rencontre rencontre) {

        // Synchronisation pour éviter les problèmes de concurrence
        synchronized (rencontresParClient) {
            // Ajoute la rencontre à la Map des rencontres
            rencontres.put(rencontre.getIdRencontre(), rencontre);

            // Ajoute l'ID de la rencontre à la liste des rencontres du client
            rencontresParClient.computeIfAbsent(clientId, k -> new ArrayList<>()).add(rencontre.getIdRencontre());
        }
    }



    // Méthode pour changer l'état d'une rencontre
    public void changerEtatRencontre(String idRencontre, ETAT_RENCONTRE nouvelEtat) {
        // Synchronisation pour éviter les problèmes de concurrence
        synchronized (rencontres) {
            // Récupère la rencontre par son ID
            Rencontre rencontre = rencontres.get(idRencontre);

            // Vérifie si la rencontre existe
            if (rencontre == null) {
                throw new RencontreNotFoundRestException("Rencontre non trouvée.", idRencontre);
            }

            // Met à jour l'état de la rencontre
            rencontre.setEtatRencontre(nouvelEtat);
            log.info("Rencontre {}passée en {}.", idRencontre, nouvelEtat);
        }
    }

    // Méthode pour trouver une rencontre par son ID
    public Rencontre findRencontreById(String idRencontre) {
        // Synchronisation pour éviter les problèmes de concurrence
        synchronized (rencontres) {
            Rencontre rencontre = rencontres.get(idRencontre);
            if (rencontre == null) {
                throw new RencontreNotFoundRestException("Rencontre non trouvée.", idRencontre);
            }
            return rencontre;
        }
    }



    // Méthode pour trouver les rencontres d'un client par son ID
    public Rencontre findRencontreByClientIdAndEtatRencontre(String clientId, ETAT_RENCONTRE etatRencontre) {
        // Synchronisation pour éviter les problèmes de concurrence
        synchronized (rencontresParClient) {
            // Récupère la liste des IDs de rencontres pour ce client
            List<String> idsRencontres = rencontresParClient.get(clientId);

            // Vérifie si la liste existe
            if (idsRencontres == null) {
                throw new RencontreNotFoundRestException("Aucune rencontre trouvée pour le client.", clientId);
            }

            // Récupère les rencontres correspondantes
            return idsRencontres
                    .stream()
                    .map(rencontres::get) // Convertit les IDs en objets Rencontre
                    .filter(Objects::nonNull) // Filtre les rencontres non trouvées
                    .filter(r -> r.getEtatRencontre() == etatRencontre)
                    .toList()
                    .get(0);
        }
    }

    // Méthode pour obtenir toutes les rencontres en attente
    public List<Rencontre> getRencontresEnAttente() {
        // Synchronisation pour éviter les problèmes de concurrence
        synchronized (rencontres) {
            return rencontres.values()
                    .stream()
                    .filter(r -> r.getEtatRencontre() == ETAT_RENCONTRE.EN_ATTENTE)
                    .toList();
        }
    }
}