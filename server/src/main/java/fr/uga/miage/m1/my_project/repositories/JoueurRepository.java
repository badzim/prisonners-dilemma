package fr.uga.miage.m1.my_project.repositories;

import fr.uga.miage.m1.my_project.models.JoueurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoueurRepository extends JpaRepository<JoueurEntity,Long> {
}
