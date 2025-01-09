package fr.uga.miage.m1.my_project.restapi.mapper;

import fr.uga.miage.m1.my_project.restapi.dto.RencontreDto;
import fr.uga.miage.m1.my_project.core.domain.model.Rencontre;

public class RencontreMapper {
    public static RencontreDto toDto(Rencontre rencontre) {
        return new RencontreDto(
                rencontre.getIdRencontre(),
                rencontre.getInitiateur().getNom(),
                rencontre.getNombreTours(),
                rencontre.getAdversaire() == null ? "EN_ATTENTE" : "COMPLÈTE"
        );
    }

    private RencontreMapper() {}
}
