package com.projetl2.bibliotheque.repository;

import com.projetl2.bibliotheque.entity.Auteur;
import com.projetl2.bibliotheque.entity.Oeuvre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AuteurRepository extends JpaRepository<Auteur, Integer> {
    // C'est tout ! L'interface est vide, mais grâce à JpaRepository, 
    // Spring Boot génère automatiquement des dizaines de méthodes 
    // comme findAll(), save(), deleteById(), findById(), etc.   
    List<Auteur> findByNomAut(String nomAut);     

    @Query("SELECT o FROM Oeuvre o JOIN o.auteurs a WHERE a.numAut = :idAuteur")
    List<Oeuvre> findOeuvresByAuteurId(@Param("idAuteur") Integer idAuteur);
}