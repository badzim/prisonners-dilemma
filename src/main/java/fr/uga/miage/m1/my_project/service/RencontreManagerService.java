package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.core.exception.rest.RencontreNotFoundRestException;
import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Data
public class RencontreManagerService {
    private Map<String, Rencontre> rencontreMap = Collections.synchronizedMap(new HashMap<>());
    private List<Rencontre> rencontresEnAttente = Collections.synchronizedList(new ArrayList<>());
    private AtomicInteger nombreRencontreEnAttente = new AtomicInteger(0);

    public synchronized void incrementNombreRencontreEnAttente() {
        nombreRencontreEnAttente.incrementAndGet();
    }

    public void decrementNombreRencontreEnAttente(Rencontre rencontre) {
        rencontresEnAttente.remove(rencontre);
        if (nombreRencontreEnAttente.get() > 0) {
            nombreRencontreEnAttente.decrementAndGet();
        }
    }

    public void addToRencontreMap(String clientId, Rencontre rencontre) {
        rencontreMap.put(clientId, rencontre);
    }

    public Rencontre findRencontreEnAttenteById(String idRencontre) {
        return rencontresEnAttente
                .stream()
                .filter(r -> r.getIdRencontre().equals(idRencontre))
                .findFirst()
                .orElseThrow(() -> new RencontreNotFoundRestException( "Rencontre non trouvée.", idRencontre));
    }

    // Méthode synchronisée pour ajouter une rencontre en attente
    public synchronized void addToRencontreEnAttente(Rencontre rencontre) {
        rencontresEnAttente.add(rencontre);
    }

    public synchronized List<Rencontre> getRencontresEnAttente() {
        return rencontresEnAttente;
    }
}
