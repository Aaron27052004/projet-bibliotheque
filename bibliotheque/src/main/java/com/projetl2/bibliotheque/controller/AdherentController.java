package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Adherent;
import com.projetl2.bibliotheque.entity.Emprunt;
import com.projetl2.bibliotheque.repository.AdherentRepository;
import com.projetl2.bibliotheque.repository.EmpruntRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adherents")
@CrossOrigin("*")
public class AdherentController {

    private final AdherentRepository adherentRepository;
    private final EmpruntRepository empruntRepository; // Injecté pour l'historique

    public AdherentController(AdherentRepository adherentRepository, EmpruntRepository empruntRepository) {
        this.adherentRepository = adherentRepository;
        this.empruntRepository = empruntRepository;
    }

    // GET /api/adherents?ville=...
    @GetMapping
    public ResponseEntity<List<Adherent>> getAdherents(@RequestParam(required = false) String ville) {
        // La gestion des paramètres page et size (Pagination) nécessite l'objet Pageable de Spring Data.
        if (ville != null) {
            return ResponseEntity.ok(adherentRepository.findByVilleAdher(ville));
        }
        return ResponseEntity.ok(adherentRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adherent> getAdherentById(@PathVariable Integer id) {
        return adherentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // NOUVEAU : GET /api/adherents/{id}/emprunts
    @GetMapping("/{id}/emprunts")
    public ResponseEntity<List<Emprunt>> getHistoriqueEmprunts(@PathVariable Integer id) {
        if (!adherentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(empruntRepository.findByAdherentNumAdher(id));
    }

    @PostMapping
    public ResponseEntity<Adherent> createAdherent(@RequestBody Adherent adherent) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adherentRepository.save(adherent));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adherent> updateAdherent(@PathVariable Integer id, @RequestBody Adherent details) {
        return adherentRepository.findById(id).map(a -> {
            a.setNomAdher(details.getNomAdher());
            a.setPrenomAdher(details.getPrenomAdher());
            a.setVilleAdher(details.getVilleAdher());
            a.setNumTel(details.getNumTel());
            a.setCpAdher(details.getCpAdher());
            a.setDateDernierPay(details.getDateDernierPay());
            a.setDateAdhesion(details.getDateAdhesion());
            a.setMailAdher(details.getMailAdher());
            a.setNumRueAdher(details.getNumRueAdher());
            a.setRueAdher(details.getRueAdher());
            return ResponseEntity.ok(adherentRepository.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdherent(@PathVariable Integer id) {
        if (!adherentRepository.existsById(id)) return ResponseEntity.notFound().build();
        adherentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}