package fr.uga.miage.m1.my_project.mapper;


import fr.uga.miage.m1.my_project.model.Rencontre;
import fr.uga.miage.m1.my_project.dto.RencontreDTO;
import java.util.ArrayList;
import java.util.List;


public class RencontreMapper {

    private RencontreMapper() {}

    public static RencontreDTO rencontreToRencontreDTO(Rencontre rencontre) {
        return new RencontreDTO(rencontre.getIdRencontre(), rencontre.getNombreTours());
    }

    public static List<RencontreDTO> rencontreToRencontreDTO(List<Rencontre> rencontreList) {
        List<RencontreDTO> rencontreDTOList = new ArrayList<>();
        for (Rencontre rencontre : rencontreList) {
            rencontreDTOList.add(rencontreToRencontreDTO(rencontre));
        }
        return rencontreDTOList;
    }

}
