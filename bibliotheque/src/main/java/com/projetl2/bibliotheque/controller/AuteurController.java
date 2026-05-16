package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Auteur;
import com.projetl2.bibliotheque.repository.AuteurRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auteurs")
@CrossOrigin("*") // <-- AJOUTE ÇA pour autoriser ton site web à parler au serveur
public class AuteurController {

    private final AuteurRepository auteurRepository;

    public AuteurController(AuteurRepository auteurRepository) {
        this.auteurRepository = auteurRepository;
    }

    // 1. Récupérer tous les auteurs
    @GetMapping
    public List<Auteur> getAllAuteurs() {
        return auteurRepository.findAll();
    }

    // 2. Récupérer un auteur par son ID (num_aut)
    @GetMapping("/{id}")
    public Auteur getAuteurById(@PathVariable Integer id) {
        return auteurRepository.findById(id).orElse(null);
    }

    // 3. Créer un nouvel auteur
    @PostMapping
    public Auteur createAuteur(@RequestBody Auteur auteur) {
        return auteurRepository.save(auteur);
    }

    // 4. Mettre à jour un auteur
    @PutMapping("/{id}")
    public Auteur updateAuteur(@PathVariable Integer id, @RequestBody Auteur auteurDetails) {
        Auteur auteur = auteurRepository.findById(id).orElse(null);
        if (auteur != null) {
            auteur.setNomAut(auteurDetails.getNomAut());
            auteur.setPrenomAut(auteurDetails.getPrenomAut());
            return auteurRepository.save(auteur);
        }
        return null;
    }

    // 5. Supprimer un auteur
    @DeleteMapping("/{id}")
    public void deleteAuteur(@PathVariable Integer id) {
        auteurRepository.deleteById(id);
    }

    // rechercher un auteur
    @GetMapping("/search")
    public List<Auteur> searchByNom(@RequestParam String nom) {
        return auteurRepository.findByNomAut(nom);
    }
}