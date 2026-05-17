package com.projetl2.bibliotheque.repository;

import com.projetl2.bibliotheque.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {
    
    // Ajoute cette ligne spécifique pour la RG13
    List<Emprunt> findByAdherentNumAdherAndStatutEmp(Integer numAdher, String statutEmp);
    List<Emprunt> findByAdherentNumAdher(Integer numAdher);
    List<Emprunt> findByLivreNumLivre(Integer numLivre);
    List<Emprunt> findByStatutEmp(String statut);
    List<Emprunt> findByStatutEmpAndDateRetourPrevueBefore(String statut, LocalDate date);
}