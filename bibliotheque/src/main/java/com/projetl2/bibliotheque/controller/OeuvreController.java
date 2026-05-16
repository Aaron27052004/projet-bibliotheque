package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Oeuvre;
import com.projetl2.bibliotheque.repository.OeuvreRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oeuvres")
public class OeuvreController {

    private final OeuvreRepository oeuvreRepository;

    public OeuvreController(OeuvreRepository oeuvreRepository) {
        this.oeuvreRepository = oeuvreRepository;
    }

    // 1. Récupérer toutes les œuvres
    @GetMapping
    public List<Oeuvre> getAllOeuvres() {
        return oeuvreRepository.findAll();
    }

    // 2. Récupérer une œuvre par son ISBN
    @GetMapping("/{isbn}")
    public Oeuvre getOeuvreByIsbn(@PathVariable String isbn) {
        return oeuvreRepository.findById(isbn).orElse(null);
    }

    // 3. Créer une nouvelle œuvre
    @PostMapping
    public Oeuvre createOeuvre(@RequestBody Oeuvre oeuvre) {
        return oeuvreRepository.save(oeuvre);
    }

    // 4. Mettre à jour une œuvre
    @PutMapping("/{isbn}")
    public Oeuvre updateOeuvre(@PathVariable String isbn, @RequestBody Oeuvre oeuvreDetails) {
        Oeuvre oeuvre = oeuvreRepository.findById(isbn).orElse(null);
        if (oeuvre != null) {
            oeuvre.setNomOeuvre(oeuvreDetails.getNomOeuvre());
            oeuvre.setEdit(oeuvreDetails.getEdit());
            oeuvre.setGenre(oeuvreDetails.getGenre());
            oeuvre.setAnneeParu(oeuvreDetails.getAnneeParu());
            return oeuvreRepository.save(oeuvre);
        }
        return null;
    }

    // 5. Supprimer une œuvre
    @DeleteMapping("/{isbn}")
    public void deleteOeuvre(@PathVariable String isbn) {
        oeuvreRepository.deleteById(isbn);
    }

     // rechercher une oeuvre
    @GetMapping("/searchName")
    public List<Oeuvre> searchByNom(@RequestParam String nomOeuvre) {
        return oeuvreRepository.findByNomOeuvre(nomOeuvre);
    }

    // recherche oeuvre par genre
    @GetMapping("/searchGenre")
    public List<Oeuvre> searchByGenre(@RequestParam String genre){
        return oeuvreRepository.findByGenre(genre);
    }
}