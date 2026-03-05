package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Auteur;
import com.projetl2.bibliotheque.repository.AuteurRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // Dit à Spring : "Cette classe va répondre aux requêtes Web (API REST)"
@RequestMapping("/api/auteurs") // L'URL de base pour tous les trucs liés aux auteurs
public class AuteurController {

    // On "injecte" notre repository pour pouvoir l'utiliser
    private final AuteurRepository auteurRepository;

    public AuteurController(AuteurRepository auteurRepository) {
        this.auteurRepository = auteurRepository;
    }

    // Quand quelqu'un fera un GET sur http://localhost:8080/api/auteurs
    @GetMapping
    public List<Auteur> getAllAuteurs() {
        // On demande au repository de chercher tous les auteurs, 
        // et Spring va automatiquement les transformer en texte (JSON) !
        return auteurRepository.findAll();
    }
}