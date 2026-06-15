package com.projetl2.bibliotheque.controller;

import com.projetl2.bibliotheque.repository.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin("*")
public class StatController {
    
    private final OeuvreRepository oeuvreRepository;
    private final LivreRepository livreRepository;
    private final EmpruntRepository empruntRepository;

    public StatController(OeuvreRepository o, LivreRepository l, EmpruntRepository e){
        this.oeuvreRepository= o;
        this.livreRepository=l;
        this.empruntRepository= e;
    }

    @GetMapping
    @Transactional
    public Map<String,Long> stats(){
        empruntRepository.marquerRetards(LocalDate.now());
        return Map.of(
            "oeuvres", oeuvreRepository.count(),
            "dispo", livreRepository.countByStatuLivre("En rayon"),
            "emprunts", empruntRepository.countByStatutEmp("en cours"),
            "retards", empruntRepository.countByStatutEmp("en retard")
        );
    }
}
