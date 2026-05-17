package com.projetl2.bibliotheque.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "EMPRUNT")
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_emp")
    private Integer numEmp;

    @Column(name = "date_deb_emp", nullable = false)
    private LocalDate dateDebEmp;

    @Column(name = "date_retour_prevue", nullable = false)
    private LocalDate dateRetourPrevue;

    @Column(name = "date_retournee")
    private LocalDate dateRetournee; // Peut être null si non rendu

    @Column(name = "statut_emp")
    private String statutEmp = "en cours"; // 'en cours', 'cloturé', 'en retard'

    // ==========================================
    // RELATION AVEC LA TABLE LIVRE
    // ==========================================
    @ManyToOne
    @JoinColumn(name = "num_livre", nullable = false)
    private Livre livre;

    // ==========================================
    // RELATION AVEC LA TABLE ADHERENT
    // ==========================================
    @ManyToOne
    @JoinColumn(name = "num_adher", nullable = false)
    private Adherent adherent;

    // ==========================================
    // CONSTRUCTEURS, GETTERS ET SETTERS
    // ==========================================

    public Emprunt() {
    }

    public Integer getNumEmp() { return numEmp; }
    public void setNumEmp(Integer numEmp) { this.numEmp = numEmp; }

    public LocalDate getDateDebEmp() { return dateDebEmp; }
    public void setDateDebEmp(LocalDate dateDebEmp) { this.dateDebEmp = dateDebEmp; }

    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }

    public LocalDate getDateRetournee() { return dateRetournee; }
    public void setDateRetournee(LocalDate dateRetournee) { this.dateRetournee = dateRetournee; }

    public String getStatutEmp() { return statutEmp; }
    public void setStatutEmp(String statutEmp) { this.statutEmp = statutEmp; }

    public Livre getLivre() { return livre; }
    public void setLivre(Livre livre) { this.livre = livre; }

    public Adherent getAdherent() { return adherent; }
    public void setAdherent(Adherent adherent) { this.adherent = adherent; }
}