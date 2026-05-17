package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Auteur;
import com.projetl2.bibliotheque.entity.Oeuvre;
import com.projetl2.bibliotheque.repository.AuteurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auteurs")
@CrossOrigin("*")
public class AuteurController {

    private final AuteurRepository auteurRepository;

    public AuteurController(AuteurRepository auteurRepository) {
        this.auteurRepository = auteurRepository;
    }

    // GET /api/auteurs?nom=...
    @GetMapping
    public ResponseEntity<List<Auteur>> getAuteurs(@RequestParam(required = false) String nom) {
        if (nom != null) {
            return ResponseEntity.ok(auteurRepository.findByNomAut(nom)); // Nécessite de créer cette méthode dans le repo si absente
        }
        return ResponseEntity.ok(auteurRepository.findAll());
    }

    // GET /api/auteurs/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Auteur> getAuteurById(@PathVariable Integer id) {
        return auteurRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // NOUVEAU : GET /api/auteurs/{id}/oeuvres
    @GetMapping("/{id}/oeuvres")
    public ResponseEntity<List<Oeuvre>> getOeuvresByAuteur(@PathVariable Integer id) {
        if (!auteurRepository.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(auteurRepository.findOeuvresByAuteurId(id));
    }

    @PostMapping
    public ResponseEntity<Auteur> createAuteur(@RequestBody Auteur auteur) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auteurRepository.save(auteur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Auteur> updateAuteur(@PathVariable Integer id, @RequestBody Auteur auteurDetails) {
        return auteurRepository.findById(id).map(auteur -> {
            auteur.setNomAut(auteurDetails.getNomAut());
            auteur.setPrenomAut(auteurDetails.getPrenomAut());
            return ResponseEntity.ok(auteurRepository.save(auteur));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuteur(@PathVariable Integer id) {
        if (!auteurRepository.existsById(id)) return ResponseEntity.notFound().build();
        auteurRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}