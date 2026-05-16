package com.projetl2.bibliotheque.repository;

import com.projetl2.bibliotheque.entity.Oeuvre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OeuvreRepository extends JpaRepository<Oeuvre, String> {
    // String ici car la clé primaire (ISBN) est une chaîne de caractères

    List<Oeuvre> findByNomOeuvre(String nomOeuvre);   
    List<Oeuvre> findByGenre(String genre);  
}