package fr.uga.miage.m1.my_project.web.restapi.mapper;

import fr.uga.miage.m1.my_project.web.restapi.response.RencontreResponse;
import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;

public class RencontreMapper {
    public static RencontreResponse toDto(Rencontre rencontre) {
        return new RencontreResponse(
                rencontre.getIdRencontre(),
                rencontre.getInitiateur().getNom(),
                rencontre.getNombreTours(),
                rencontre.getAdversaire() == null ? "EN_ATTENTE" : "COMPLÈTE"
        );
    }

    private RencontreMapper() {}
}
