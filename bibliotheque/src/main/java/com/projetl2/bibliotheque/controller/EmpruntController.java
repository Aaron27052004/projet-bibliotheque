package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Adherent;
import com.projetl2.bibliotheque.entity.Emprunt;
import com.projetl2.bibliotheque.entity.Livre;
import com.projetl2.bibliotheque.repository.AdherentRepository;
import com.projetl2.bibliotheque.repository.EmpruntRepository;
import com.projetl2.bibliotheque.repository.LivreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    public List<Emprunt> getAllEmprunts() {
        return empruntRepository.findAll();
    }

    // =========================================================
    // CREATION EMPRUNT AVEC VERIFICATION DES REGLES DE GESTION
    // =========================================================
    @PostMapping("/adherent/{numAdher}/livre/{numLivre}")
    public ResponseEntity<?> creerEmprunt(@PathVariable Integer numAdher, @PathVariable Integer numLivre) {
        
        Adherent adherent = adherentRepository.findById(numAdher).orElse(null);
        Livre livre = livreRepository.findById(numLivre).orElse(null);

        if (adherent == null || livre == null) {
            return ResponseEntity.badRequest().body("Erreur : Adhérent ou Livre introuvable.");
        }

        // --- RG9 : Un livre au statut emprunté ne peut pas être emprunté ---
        if (!"En rayon".equals(livre.getStatuLivre())) {
            return ResponseEntity.badRequest().body("Refusé (RG9) : Ce livre est déjà emprunté ou indisponible.");
        }

        // --- RG13 : Vérification de la cotisation (On part du principe qu'elle dure 1 an) ---
        if (adherent.getDateDernierPay() == null || adherent.getDateDernierPay().plusYears(1).isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body("Refusé (RG13) : La cotisation de l'adhérent a expiré ou n'est pas réglée.");
        }

        // --- RG13 : Vérification des retards ---
        List<Emprunt> empruntsEnCours = empruntRepository.findByAdherentNumAdherAndStatutEmp(numAdher, "en cours");
        for (Emprunt emp : empruntsEnCours) {
            if (emp.getDateRetourPrevue().isBefore(LocalDate.now())) {
                // S'il a un livre dont la date prévue est dépassée, on bloque
                // Et on peut même en profiter pour mettre à jour son statut en base !
                emp.setStatutEmp("en retard");
                empruntRepository.save(emp);
                return ResponseEntity.badRequest().body("Refusé (RG13) : L'adhérent a des livres en retard à restituer.");
            }
        }

        // Toutes les règles sont respectées, on crée l'emprunt !
        Emprunt nouvelEmprunt = new Emprunt();
        nouvelEmprunt.setAdherent(adherent);
        nouvelEmprunt.setLivre(livre);
        
        LocalDate aujourdhui = LocalDate.now();
        nouvelEmprunt.setDateDebEmp(aujourdhui); // RG10 : Date début = Aujourd'hui (donc retour >= emprunt)
        nouvelEmprunt.setDateRetourPrevue(aujourdhui.plusDays(30)); // RG6 : Ne dépasse pas 30 jours
        nouvelEmprunt.setStatutEmp("en cours");

        // RG9 (Conséquence) : Le livre passe en emprunté
        livre.setStatuLivre("Emprunté");
        livreRepository.save(livre);

        Emprunt savedEmprunt = empruntRepository.save(nouvelEmprunt);
        return ResponseEntity.ok(savedEmprunt);
    }

    // =========================================================
    // RETOUR EMPRUNT (RG11 et RG7)
    // =========================================================
    @PutMapping("/{numEmp}/retour")
    public ResponseEntity<?> retournerLivre(@PathVariable Integer numEmp) {
        Emprunt emprunt = empruntRepository.findById(numEmp).orElse(null);
        
        if (emprunt != null) {
            emprunt.setDateRetournee(LocalDate.now());
            // RG7 : On ne supprime pas l'emprunt, on le garde dans l'historique
            emprunt.setStatutEmp("cloturé"); 
            
            // RG11 : Le livre redevient 'En rayon'
            Livre livre = emprunt.getLivre();
            livre.setStatuLivre("En rayon");
            livreRepository.save(livre);

            return ResponseEntity.ok(empruntRepository.save(emprunt));
        }
        return ResponseEntity.badRequest().body("Emprunt introuvable.");
    }
}