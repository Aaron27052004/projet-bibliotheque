package com.projetl2.bibliotheque.repository;

import com.projetl2.bibliotheque.entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Integer> {
    
    // Pour trouver tous les livres "En rayon" ou "Emprunté"
    List<Livre> findByStatuLivre(String statuLivre);

    // Pour trouver tous les exemplaires physiques d'une oeuvre précise (via son ISBN)
    List<Livre> findByOeuvreIsbn(String isbn);
}