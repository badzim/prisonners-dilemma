package fr.uga.miage.m1.my_project.repositories;


import fr.uga.miage.m1.my_project.enums.TypeAction;
import fr.uga.miage.m1.my_project.models.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<TourEntity, Long> {
    @Query("SELECT t.actionJoueur1 FROM TourEntity t WHERE t.rencontre.rencontreId = :rencontreId AND t.rencontre.joueur1.id = :joueurId")
    List<TypeAction> findActionsByRencontreAndJoueur1(@Param("rencontreId") Long rencontreId, @Param("joueurId") Long joueurId);

    @Query("SELECT t.actionJoueur2 FROM TourEntity t WHERE t.rencontre.rencontreId = :rencontreId AND t.rencontre.joueur2.id = :joueurId")
    List<TypeAction> findActionsByRencontreAndJoueur2(@Param("rencontreId") Long rencontreId, @Param("joueurId") Long joueurId);

}
