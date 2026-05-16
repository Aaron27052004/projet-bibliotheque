package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Adherent;
import com.projetl2.bibliotheque.repository.AdherentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adherents")
@CrossOrigin("*") // Autorise les requêtes depuis ton interface web de test
public class AdherentController {

    private final AdherentRepository adherentRepository;

    public AdherentController(AdherentRepository adherentRepository) {
        this.adherentRepository = adherentRepository;
    }

    // 1. Récupérer tous les adhérents
    @GetMapping
    public List<Adherent> getAllAdherents() {
        return adherentRepository.findAll();
    }

    // 2. Récupérer un adhérent par son ID
    @GetMapping("/{id}")
    public Adherent getAdherentById(@PathVariable Integer id) {
        return adherentRepository.findById(id).orElse(null);
    }

    // 3. Chercher par ville via l'URL (ex: /api/adherents/search?ville=Marseille)
    @GetMapping("/search")
    public List<Adherent> getAdherentsByVille(@RequestParam String ville) {
        return adherentRepository.findByVilleAdher(ville);
    }

    // 4. Ajouter un nouvel adhérent
    @PostMapping
    public Adherent createAdherent(@RequestBody Adherent adherent) {
        return adherentRepository.save(adherent);
    }

    // 5. Mettre à jour un adhérent existant
    @PutMapping("/{id}")
    public Adherent updateAdherent(@PathVariable Integer id, @RequestBody Adherent details) {
        Adherent adherent = adherentRepository.findById(id).orElse(null);
        if (adherent != null) {
            adherent.setNomAdher(details.getNomAdher());
            adherent.setPrenomAdher(details.getPrenomAdher());
            adherent.setNaissAdher(details.getNaissAdher());
            adherent.setNumRueAdher(details.getNumRueAdher());
            adherent.setRueAdher(details.getRueAdher());
            adherent.setCpAdher(details.getCpAdher());
            adherent.setVilleAdher(details.getVilleAdher());
            adherent.setMailAdher(details.getMailAdher());
            adherent.setDateAdhesion(details.getDateAdhesion());
            adherent.setDateDernierPay(details.getDateDernierPay());
            adherent.setNumTel(details.getNumTel());
            return adherentRepository.save(adherent);
        }
        return null;
    }

    // 6. Supprimer un adhérent
    @DeleteMapping("/{id}")
    public void deleteAdherent(@PathVariable Integer id) {
        adherentRepository.deleteById(id);
    }
}