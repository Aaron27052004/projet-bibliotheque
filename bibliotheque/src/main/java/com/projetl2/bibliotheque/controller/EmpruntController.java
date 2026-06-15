package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Adherent;
import com.projetl2.bibliotheque.entity.Emprunt;
import com.projetl2.bibliotheque.entity.Livre;
import com.projetl2.bibliotheque.repository.AdherentRepository;
import com.projetl2.bibliotheque.repository.EmpruntRepository;
import com.projetl2.bibliotheque.repository.LivreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emprunts")
@CrossOrigin("*")
public class EmpruntController {

    private static final int DUREE_PRET_JOURS = 30;
    private static final int DUREE_PROLONGATION_JOURS = 15;

    private final EmpruntRepository empruntRepository;
    private final LivreRepository livreRepository;
    private final AdherentRepository adherentRepository;

    public EmpruntController(EmpruntRepository empruntRepository, LivreRepository livreRepository, AdherentRepository adherentRepository) {
        this.empruntRepository = empruntRepository;
        this.livreRepository = livreRepository;
        this.adherentRepository = adherentRepository;
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<Emprunt>> getEmprunts(@RequestParam(required = false) String statut) {
        empruntRepository.marquerRetards(LocalDate.now()); // la base met à jour les statuts
        if (statut != null) {
            return ResponseEntity.ok(empruntRepository.findByStatutEmp(statut));
        }
        return ResponseEntity.ok(empruntRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emprunt> getEmpruntById(@PathVariable Integer id) {
        return empruntRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createEmprunt(@RequestBody Map<String, Integer> payload) {
        Integer adherentId = payload.get("adherentId");
        Integer exemplaireId = payload.get("exemplaireId");

        empruntRepository.marquerRetards(LocalDate.now());

        if (adherentId == null || exemplaireId == null) {
            return ResponseEntity.badRequest().body("adherentId et exemplaireId sont requis.");
        }

        Adherent adherent = adherentRepository.findById(adherentId).orElse(null);
        Livre livre = livreRepository.findById(exemplaireId).orElse(null);

        if (adherent == null || livre == null) return ResponseEntity.notFound().build();
        if (!"En rayon".equals(livre.getStatuLivre())) return ResponseEntity.badRequest().body("Livre non disponible.");

        // RG13 — cotisation valide
        if (adherent.getDateDernierPay() == null ||
            adherent.getDateDernierPay().plusYears(1).isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body("Cotisation expirée.");
        }

        boolean aUnRetard = !empruntRepository
                .findByAdherentNumAdherAndStatutEmp(adherentId, "en retard")
                .isEmpty();
        if (aUnRetard) {
            return ResponseEntity.badRequest().body("Retard non régularisé.");
        }

        Emprunt e = new Emprunt();
        e.setAdherent(adherent);
        e.setLivre(livre);
        e.setDateDebEmp(LocalDate.now());
        e.setDateRetourPrevue(LocalDate.now().plusDays(DUREE_PRET_JOURS));
        e.setStatutEmp("en cours");

        livre.setStatuLivre("Emprunté");
        livreRepository.save(livre);

        return ResponseEntity.status(HttpStatus.CREATED).body(empruntRepository.save(e));
    }

    @PatchMapping("/{id}/retour")
    @Transactional
    public ResponseEntity<?> retournerLivre(@PathVariable Integer id) {
        Emprunt e = empruntRepository.findById(id).orElse(null);
        if (e == null) return ResponseEntity.notFound().build();
        if ("cloturé".equals(e.getStatutEmp())) {
            return ResponseEntity.badRequest().body("Cet emprunt est déjà clôturé.");
        }
        // Marche que l'emprunt soit "en cours" OU "en retard" : il devient clôturé.
        e.setDateRetournee(LocalDate.now());
        e.setStatutEmp("cloturé");

        Livre l = e.getLivre();
        if (l != null) {
            l.setStatuLivre("En rayon");
            livreRepository.save(l);
        }
        return ResponseEntity.ok(empruntRepository.save(e));
    }

    @PatchMapping("/{id}/prolonger")
    @Transactional
    public ResponseEntity<?> prolongerEmprunt(@PathVariable Integer id) {
        Emprunt e = empruntRepository.findById(id).orElse(null);
        if (e == null) return ResponseEntity.notFound().build();
        if ("cloturé".equals(e.getStatutEmp())) {
            return ResponseEntity.badRequest().body("Impossible de prolonger un emprunt clôturé.");
        }
        e.setDateRetourPrevue(e.getDateRetourPrevue().plusDays(DUREE_PROLONGATION_JOURS));
        // Si la nouvelle date n'est plus dépassée, l'emprunt repasse "en cours".
        if (!e.getDateRetourPrevue().isBefore(LocalDate.now())) {
            e.setStatutEmp("en cours");
        }
        return ResponseEntity.ok(empruntRepository.save(e));
    }

    // GET /api/emprunts/retards : met à jour puis renvoie tous les "en retard"
    @GetMapping("/retards")
    @Transactional
    public ResponseEntity<List<Emprunt>> getRetards() {
        empruntRepository.marquerRetards(LocalDate.now());
        return ResponseEntity.ok(empruntRepository.findByStatutEmp("en retard"));
    }
}

