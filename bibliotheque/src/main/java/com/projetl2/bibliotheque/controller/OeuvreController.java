package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Oeuvre;
import com.projetl2.bibliotheque.entity.Livre;
import com.projetl2.bibliotheque.entity.Auteur;
import com.projetl2.bibliotheque.repository.OeuvreRepository;
import com.projetl2.bibliotheque.repository.AuteurRepository;

import jakarta.transaction.Transactional;

import com.projetl2.bibliotheque.repository.LivreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oeuvres")
@CrossOrigin("*") // À remplacer par ton domaine front en production
public class OeuvreController {

    private final OeuvreRepository oeuvreRepository;
    private final LivreRepository livreRepository;
    private final AuteurRepository auteurRepository;

    public OeuvreController(OeuvreRepository oeuvreRepository, LivreRepository livreRepository, AuteurRepository auteurRepository) {
        this.oeuvreRepository = oeuvreRepository;
        this.livreRepository = livreRepository;
        this.auteurRepository= auteurRepository;
    }

    // GET /api/oeuvres?titre=...&genre=...&disponible=...
    @GetMapping
    public ResponseEntity<List<Oeuvre>> getOeuvres(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean disponible) {
            
        // Logique de filtrage unifiée (à implémenter avec des Specifications Spring ou des if/else sur les appels Repository)
        if (Boolean.TRUE.equals(disponible)) {
            return ResponseEntity.ok(oeuvreRepository.findOeuvresEnStock());
        }
        if (titre != null) {
            return ResponseEntity.ok(oeuvreRepository.findByNomOeuvreContainingIgnoreCase(titre));
        }
        if (genre != null) {
            return ResponseEntity.ok(oeuvreRepository.findByGenreContainingIgnoreCase(genre));
        }
        return ResponseEntity.ok(oeuvreRepository.findAll());
    }

    // GET /api/oeuvres/{isbn}
    @GetMapping("/{isbn}")
    public ResponseEntity<Oeuvre> getOeuvreByIsbn(@PathVariable String isbn) {
        return oeuvreRepository.findById(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/oeuvres
    @PostMapping
    public ResponseEntity<Oeuvre> createOeuvre(@RequestBody Oeuvre oeuvre) {
        Oeuvre saved = oeuvreRepository.save(oeuvre);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/oeuvres/{isbn}
    @PutMapping("/{isbn}")
    public ResponseEntity<Oeuvre> updateOeuvre(@PathVariable String isbn, @RequestBody Oeuvre details) {
        return oeuvreRepository.findById(isbn).map(oeuvre -> {
            oeuvre.setNomOeuvre(details.getNomOeuvre());
            oeuvre.setEdit(details.getEdit());
            oeuvre.setGenre(details.getGenre());
            oeuvre.setAnneeParu(details.getAnneeParu());
            return ResponseEntity.ok(oeuvreRepository.save(oeuvre));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/oeuvres/{isbn}
    @Transactional
    @DeleteMapping("/{isbn}")
    public ResponseEntity<?> deleteOeuvre(@PathVariable String isbn) {
        if (!oeuvreRepository.existsById(isbn)) {
            return ResponseEntity.notFound().build();
        }

        List<Livre> livres = livreRepository.findByOeuvreIsbn(isbn);
        if(!livres.isEmpty())
            return ResponseEntity.badRequest().body("impossible de supprimer cette oeuvre, des exemplaires sont encores présents");
        oeuvreRepository.deleteById(isbn);
        return ResponseEntity.noContent().build();
    }

    // NOUVEAU : GET /api/oeuvres/{isbn}/exemplaires?statut=disponible
    @GetMapping("/{isbn}/exemplaires")
    public ResponseEntity<List<Livre>> getExemplairesByOeuvre(
            @PathVariable String isbn,
            @RequestParam(required = false) String statut) {
        
        if (!oeuvreRepository.existsById(isbn)) {
            return ResponseEntity.notFound().build();
        }

        if ("disponible".equalsIgnoreCase(statut)) {
            return ResponseEntity.ok(livreRepository.findByOeuvreIsbnAndStatuLivre(isbn, "En rayon"));
        }
        return ResponseEntity.ok(livreRepository.findByOeuvreIsbn(isbn));
    }

    @Transactional
    @PostMapping("/{isbn}/auteurs/{numAut}")
    public ResponseEntity<?> lierEntity(@PathVariable String isbn, @PathVariable int numAut){
        Oeuvre oeuvre = oeuvreRepository.findById(isbn).orElse(null);
        Auteur auteur = auteurRepository.findById(numAut).orElse(null);

        if (( oeuvre == null ) || ( auteur == null) ) return ResponseEntity.notFound().build();
        if (oeuvre == null || auteur == null) return ResponseEntity.notFound().build();
        if (auteur.getOeuvres() == null) auteur.setOeuvres(new java.util.ArrayList<>());
        if (!auteur.getOeuvres().contains(oeuvre)) {
            auteur.getOeuvres().add(oeuvre);   // Auteur est le côté PROPRIÉTAIRE → c'est lui qu'on sauvegarde
            auteurRepository.save(auteur);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{isbn}/auteurs/{numAut}")
    @Transactional
    public ResponseEntity<?> delierAuteur(@PathVariable String isbn, @PathVariable Integer numAut) {
        Auteur auteur = auteurRepository.findById(numAut).orElse(null);
        if (auteur == null) return ResponseEntity.notFound().build();
        if (auteur.getOeuvres() != null) {
            auteur.getOeuvres().removeIf(o -> o.getIsbn().equals(isbn));
            auteurRepository.save(auteur);
        }
        return ResponseEntity.noContent().build();
    }

}