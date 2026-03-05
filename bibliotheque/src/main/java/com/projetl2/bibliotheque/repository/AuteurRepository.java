package com.projetl2.bibliotheque.repository;

import com.projetl2.bibliotheque.entity.Auteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuteurRepository extends JpaRepository<Auteur, Integer> {
    // C'est tout ! L'interface est vide, mais grâce à JpaRepository, 
    // Spring Boot génère automatiquement des dizaines de méthodes 
    // comme findAll(), save(), deleteById(), findById(), etc.
}