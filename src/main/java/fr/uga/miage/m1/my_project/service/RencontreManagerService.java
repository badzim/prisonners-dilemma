package fr.uga.miage.m1.my_project.service;

import fr.uga.miage.m1.my_project.model.Rencontre;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RencontreManagerService {
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

    // Méthode synchronisée pour ajouter une rencontre en attente
    public synchronized void addToRencontreEnAttente(Rencontre rencontre) {
        rencontresEnAttente.add(rencontre);
    }

    public synchronized List<Rencontre> getRencontresEnAttente() {
        return rencontresEnAttente;
    }
}
