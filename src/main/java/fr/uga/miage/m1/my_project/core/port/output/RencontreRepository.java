package fr.uga.miage.m1.my_project.core.port.output;

import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;
import fr.uga.miage.m1.my_project.core.domain.model.enums.ETAT_RENCONTRE;

import java.util.List;

public interface RencontreRepository {

    void addRencontre(String rencontreId, Rencontre rencontre);

    void addRencontreParClient(String clientId, Rencontre rencontre);

    void changerEtatRencontre(String idRencontre, ETAT_RENCONTRE nouvelEtat);

    Rencontre findRencontreById(String idRencontre);

    Rencontre findRencontreByClientIdAndEtatRencontre(String clientId, ETAT_RENCONTRE etatRencontre);

    List<Rencontre> getRencontresEnAttente();
}
