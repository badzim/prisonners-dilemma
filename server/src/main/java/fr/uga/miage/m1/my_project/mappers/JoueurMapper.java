package fr.uga.miage.m1.my_project.mappers;

import fr.uga.miage.m1.my_project.models.JoueurEntity;
import fr.uga.miage.m1.my_project.responses.JoueurResponse;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface JoueurMapper {

    List<JoueurResponse> toResponses(List<JoueurEntity> joueurEntityList);
}
