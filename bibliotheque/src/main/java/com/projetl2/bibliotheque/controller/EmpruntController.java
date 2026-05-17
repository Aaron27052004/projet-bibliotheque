package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.entity.Adherent;
import com.projetl2.bibliotheque.entity.Emprunt;
import com.projetl2.bibliotheque.entity.Livre;
import com.projetl2.bibliotheque.repository.AdherentRepository;
import com.projetl2.bibliotheque.repository.EmpruntRepository;
import com.projetl2.bibliotheque.repository.LivreRepository;
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

    public EmpruntController(EmpruntRepository empruntRepository, 
                             LivreRepository livreRepository, 
                             AdherentRepository adherentRepository) {
        this.empruntRepository = empruntRepository;
        this.livreRepository = livreRepository;
        this.adherentRepository = adherentRepository;
    }

    @GetMapping
    public List<Emprunt> getAllEmprunts() {
        return empruntRepository.findAll();
    }

    // Créer un emprunt en passant les IDs du livre et de l'adhérent dans l'URL
    @PostMapping("/adherent/{numAdher}/livre/{numLivre}")
    public Emprunt creerEmprunt(@PathVariable Integer numAdher, @PathVariable Integer numLivre) {
        
        Adherent adherent = adherentRepository.findById(numAdher).orElse(null);
        Livre livre = livreRepository.findById(numLivre).orElse(null);

        // On vérifie que le livre et l'adhérent existent bien
        if (adherent != null && livre != null) {
            Emprunt nouvelEmprunt = new Emprunt();
            nouvelEmprunt.setAdherent(adherent);
            nouvelEmprunt.setLivre(livre);
            
            // On calcule automatiquement les dates (emprunté aujourd'hui, retour dans 14 jours)
            LocalDate aujourdhui = LocalDate.now();
            nouvelEmprunt.setDateDebEmp(aujourdhui);
            nouvelEmprunt.setDateRetourPrevue(aujourdhui.plusDays(14));
            nouvelEmprunt.setStatutEmp("en cours");

            // Optionnel mais recommandé : On change le statut du livre
            livre.setStatuLivre("Emprunté");
            livreRepository.save(livre);

            return empruntRepository.save(nouvelEmprunt);
        }
        return null;
    }

    // Déclarer un livre comme rendu
    @PutMapping("/{numEmp}/retour")
    public Emprunt retournerLivre(@PathVariable Integer numEmp) {
        Emprunt emprunt = empruntRepository.findById(numEmp).orElse(null);
        
        if (emprunt != null) {
            emprunt.setDateRetournee(LocalDate.now());
            emprunt.setStatutEmp("cloturé");
            
            // On remet le livre "En rayon"
            Livre livre = emprunt.getLivre();
            livre.setStatuLivre("En rayon");
            livreRepository.save(livre);

            return empruntRepository.save(emprunt);
        }
        return null;
    }
}