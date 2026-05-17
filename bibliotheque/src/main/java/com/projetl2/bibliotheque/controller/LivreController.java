package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Livre;
import com.projetl2.bibliotheque.entity.Oeuvre;
import com.projetl2.bibliotheque.repository.LivreRepository;
import com.projetl2.bibliotheque.repository.OeuvreRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livres")
@CrossOrigin("*")
public class LivreController {

    private final LivreRepository livreRepository;
    private final OeuvreRepository oeuvreRepository;

    // On injecte aussi OeuvreRepository pour pouvoir vérifier que l'oeuvre existe
    // au moment de créer un nouveau livre
    public LivreController(LivreRepository livreRepository, OeuvreRepository oeuvreRepository) {
        this.livreRepository = livreRepository;
        this.oeuvreRepository = oeuvreRepository;
    }

    // 1. Récupérer tous les exemplaires
    @GetMapping
    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    // 2. Voir les livres disponibles (En rayon)
    @GetMapping("/search/disponibles")
    public List<Livre> getLivresDisponibles() {
        return livreRepository.findByStatuLivre("En rayon");
    }

    // 3. Ajouter un exemplaire
    // L'URL attend l'ISBN de l'oeuvre pour lier le livre directement
    @PostMapping("/oeuvre/{isbn}")
    public Livre createLivre(@PathVariable String isbn, @RequestBody Livre livre) {
        // On cherche l'oeuvre en base de données
        Oeuvre oeuvreExistante = oeuvreRepository.findById(isbn).orElse(null);
        
        if (oeuvreExistante != null) {
            // Si l'oeuvre existe, on l'attache au livre et on sauvegarde
            livre.setOeuvre(oeuvreExistante);
            return livreRepository.save(livre);
        }
        // Si l'ISBN n'existe pas, on renvoie une erreur (null)
        return null;
    }

    // 4. Mettre à jour l'état ou le statut d'un livre
    @PutMapping("/{id}")
    public Livre updateLivre(@PathVariable Integer id, @RequestBody Livre details) {
        Livre livre = livreRepository.findById(id).orElse(null);
        if (livre != null) {
            livre.setEtatLivre(details.getEtatLivre());
            livre.setStatuLivre(details.getStatuLivre());
            return livreRepository.save(livre);
        }
        return null;
    }

    // 5. Supprimer un livre du système (ex: livre perdu ou détruit)
    @DeleteMapping("/{id}")
    public void deleteLivre(@PathVariable Integer id) {
        livreRepository.deleteById(id);
    }
}