package com.projetl2.bibliotheque.repository;

import com.projetl2.bibliotheque.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {
    
    // Trouver tous les emprunts d'un adhérent spécifique
    List<Emprunt> findByAdherentNumAdher(Integer numAdher);

    // Trouver tous les emprunts pour un statut précis (ex: "en cours")
    List<Emprunt> findByStatutEmp(String statutEmp);
}