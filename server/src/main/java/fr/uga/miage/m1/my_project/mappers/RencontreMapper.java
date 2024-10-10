package fr.uga.miage.m1.my_project.mappers;

import fr.uga.miage.m1.my_project.models.RencontreEntity;
import fr.uga.miage.m1.my_project.responses.RencontreResponse;
import org.mapstruct.Mapping;

public interface RencontreMapper {

    @Mapping(source = "id", target = "rencontreId")              // Mapper l'ID
    @Mapping(source = "joueur1.nom", target = "joueur1")         // Mapper le nom du joueur 1
    @Mapping(source = "joueur2.nom", target = "joueur2")         // Mapper le nom du joueur 2
    @Mapping(source = "nombreTours", target = "nombreTours")     // Mapper le nombre de tours
    @Mapping(source = "etat", target = "etat")
        // Mapper l'état de la rencontre
    RencontreResponse toResponse(RencontreEntity rencontreEntity); // Méthode de mapping
}