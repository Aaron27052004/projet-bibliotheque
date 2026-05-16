package com.projetl2.bibliotheque.repository;

import com.projetl2.bibliotheque.entity.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdherentRepository extends JpaRepository<Adherent, Integer> {
    
    // Pour chercher les adhérents par leur nom
    List<Adherent> findByNomAdher(String nomAdher);

    // Pour chercher les adhérents par ville
    List<Adherent> findByVilleAdher(String villeAdher);
}