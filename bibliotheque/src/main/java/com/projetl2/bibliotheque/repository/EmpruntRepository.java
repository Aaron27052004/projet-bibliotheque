package com.projetl2.bibliotheque.repository;

import com.projetl2.bibliotheque.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
     // Compteur par statut, calculé en base (pour le dashboard).
    long countByStatutEmp(String statut);

    // ── LE COEUR DE TA DEMANDE ──
    // Une seule requête SQL : passe en "en retard" TOUS les emprunts "en cours"
    // dont la date de retour prévue est dépassée. C'est la base qui décide, pas le JS.
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Emprunt e SET e.statutEmp = 'en retard' " +
           "WHERE e.statutEmp = 'en cours' AND e.dateRetourPrevue < :today")
    int marquerRetards(@Param("today") LocalDate today);
}