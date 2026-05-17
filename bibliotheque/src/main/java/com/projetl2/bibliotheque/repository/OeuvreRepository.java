package com.projetl2.bibliotheque.repository;

import com.projetl2.bibliotheque.entity.Oeuvre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OeuvreRepository extends JpaRepository<Oeuvre, String> {
    // String ici car la clé primaire (ISBN) est une chaîne de caractères

    List<Oeuvre> findByNomOeuvre(String nomOeuvre);   
    List<Oeuvre> findByGenre(String genre);  

    // Chercher par nom (LIKE %nom%)
    List<Oeuvre> findByNomOeuvreContainingIgnoreCase(String nomOeuvre);
    List<Oeuvre> findByGenreContainingIgnoreCase(String genre);
    
    // Requête SQL magique : Ne récupérer que les œuvres qui ont des livres liés !
    @Query("SELECT DISTINCT o FROM Oeuvre o JOIN Livre l ON o.isbn = l.oeuvre.isbn")
    List<Oeuvre> findOeuvresEnStock();
}