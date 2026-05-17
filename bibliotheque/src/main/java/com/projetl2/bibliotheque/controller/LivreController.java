package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Livre;
import com.projetl2.bibliotheque.repository.LivreRepository;
import com.projetl2.bibliotheque.repository.OeuvreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exemplaires") // Nommage RESTful métier
@CrossOrigin("*")
public class LivreController {

    private final LivreRepository livreRepository;
    private final OeuvreRepository oeuvreRepository;

    public LivreController(LivreRepository livreRepository, OeuvreRepository oeuvreRepository) {
        this.livreRepository = livreRepository;
        this.oeuvreRepository = oeuvreRepository;
    }

    // GET /api/exemplaires?statut=disponible
    @GetMapping
    public ResponseEntity<List<Livre>> getExemplaires(@RequestParam(required = false) String statut) {
        if ("disponible".equalsIgnoreCase(statut)) {
            return ResponseEntity.ok(livreRepository.findByStatuLivre("En rayon"));
        }
        return ResponseEntity.ok(livreRepository.findAll());
    }

    // POST /api/exemplaires (isbn dans le body)
    @PostMapping
    public ResponseEntity<?> createExemplaire(@RequestBody Map<String, String> payload) {
        String isbn = payload.get("isbn");
        if (isbn == null) return ResponseEntity.badRequest().body("L'ISBN est requis dans le body.");

        return oeuvreRepository.findById(isbn).map(oeuvre -> {
            Livre nouveauLivre = new Livre();
            nouveauLivre.setOeuvre(oeuvre);
            nouveauLivre.setEtatLivre(payload.getOrDefault("etatLivre", "Neuf"));
            nouveauLivre.setStatuLivre("En rayon");
            return ResponseEntity.status(HttpStatus.CREATED).body(livreRepository.save(nouveauLivre));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // PATCH /api/exemplaires/{id} (Etat / Statut seulement)
    @PatchMapping("/{id}")
    public ResponseEntity<Livre> updateExemplairePartiel(@PathVariable Integer id, @RequestBody Map<String, String> updates) {
        return livreRepository.findById(id).map(livre -> {
            if (updates.containsKey("etatLivre")) livre.setEtatLivre(updates.get("etatLivre"));
            if (updates.containsKey("statuLivre")) livre.setStatuLivre(updates.get("statuLivre"));
            return ResponseEntity.ok(livreRepository.save(livre));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/exemplaires/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExemplaire(@PathVariable Integer id) {
        if (!livreRepository.existsById(id)) return ResponseEntity.notFound().build();
        livreRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}