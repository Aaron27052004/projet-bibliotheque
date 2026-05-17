package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Adherent;
import com.projetl2.bibliotheque.entity.Emprunt;
import com.projetl2.bibliotheque.entity.Livre;
import com.projetl2.bibliotheque.repository.AdherentRepository;
import com.projetl2.bibliotheque.repository.EmpruntRepository;
import com.projetl2.bibliotheque.repository.LivreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emprunts")
@CrossOrigin("*")
public class EmpruntController {

    private final EmpruntRepository empruntRepository;
    private final LivreRepository livreRepository;
    private final AdherentRepository adherentRepository;

    public EmpruntController(EmpruntRepository empruntRepository, LivreRepository livreRepository, AdherentRepository adherentRepository) {
        this.empruntRepository = empruntRepository;
        this.livreRepository = livreRepository;
        this.adherentRepository = adherentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Emprunt>> getEmprunts(@RequestParam(required = false) String statut) {
        if (statut != null) {
            return ResponseEntity.ok(empruntRepository.findByStatutEmp(statut));
        }
        return ResponseEntity.ok(empruntRepository.findAll());
    }

    // GET /api/emprunts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Emprunt> getEmpruntById(@PathVariable Integer id) {
        return empruntRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/emprunts (adherentId + exemplaireId dans le body)
    @PostMapping
    public ResponseEntity<?> createEmprunt(@RequestBody Map<String, Integer> payload) {
        Integer adherentId = payload.get("adherentId");
        Integer exemplaireId = payload.get("exemplaireId");

        if (adherentId == null || exemplaireId == null) {
            return ResponseEntity.badRequest().body("adherentId et exemplaireId sont requis.");
        }

        Adherent adherent = adherentRepository.findById(adherentId).orElse(null);
        Livre livre = livreRepository.findById(exemplaireId).orElse(null);

        if (adherent == null || livre == null) return ResponseEntity.notFound().build();
        if (!"En rayon".equals(livre.getStatuLivre())) return ResponseEntity.badRequest().body("Livre non disponible.");

        // RG13 : cotisation valide
        if (adherent.getDateDernierPay() == null ||
            adherent.getDateDernierPay().plusYears(1).isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body("Cotisation expirée (RG13).");
        }

        // RG13 : pas de retard en cours
        List<Emprunt> enCours = empruntRepository.findByAdherentNumAdherAndStatutEmp(adherentId, "en cours");
        for (Emprunt emp : enCours) {
            if (emp.getDateRetourPrevue().isBefore(LocalDate.now())) {
                emp.setStatutEmp("en retard");
                empruntRepository.save(emp);
                return ResponseEntity.badRequest().body("Retard non régularisé (RG13).");
            }
        }

        // Création
        Emprunt e = new Emprunt();
        e.setAdherent(adherent);
        e.setLivre(livre);
        e.setDateDebEmp(LocalDate.now());
        e.setDateRetourPrevue(LocalDate.now().plusDays(30));
        e.setStatutEmp("en cours");

        livre.setStatuLivre("Emprunté");
        livreRepository.save(livre);

        return ResponseEntity.status(HttpStatus.CREATED).body(empruntRepository.save(e));
    }

    // PATCH /api/emprunts/{id}/retour
    @PatchMapping("/{id}/retour")
    public ResponseEntity<?> retournerLivre(@PathVariable Integer id) {
        return empruntRepository.findById(id).map(e -> {
            e.setDateRetournee(LocalDate.now());
            e.setStatutEmp("cloturé");
            
            Livre l = e.getLivre();
            l.setStatuLivre("En rayon");
            livreRepository.save(l);

            return ResponseEntity.ok(empruntRepository.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PATCH /api/emprunts/{id}/prolonger
    @PatchMapping("/{id}/prolonger")
    public ResponseEntity<?> prolongerEmprunt(@PathVariable Integer id) {
        return empruntRepository.findById(id).map(e -> {
            if ("cloturé".equals(e.getStatutEmp())) {
                return ResponseEntity.badRequest().body("Impossible de prolonger un emprunt clôturé.");
            }
            // Ajoute 15 jours à la date prévue
            e.setDateRetourPrevue(e.getDateRetourPrevue().plusDays(15));
            return ResponseEntity.ok(empruntRepository.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    // GET /api/emprunts/retards
    @GetMapping("/retards")
    public ResponseEntity<List<Emprunt>> getRetards() {
        return ResponseEntity.ok(
            empruntRepository.findByStatutEmpAndDateRetourPrevueBefore("en cours", LocalDate.now())
        );
    }
}