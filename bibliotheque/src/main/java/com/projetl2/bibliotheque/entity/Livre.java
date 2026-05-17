package com.projetl2.bibliotheque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LIVRE")
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_livre")
    private Integer numLivre;

    // En SQL c'est un ENUM('Neuf', 'Bon', 'Abimé').
    // On utilise un String, mais il faudra envoyer exactement ces mots !
    @Column(name = "etat_livre")
    private String etatLivre = "Neuf"; // Valeur par défaut comme dans ton SQL

    // ENUM('En rayon', 'Emprunté')
    @Column(name = "statu_livre")
    private String statuLivre = "En rayon"; // Valeur par défaut

    // ==========================================
    // RELATION AVEC LA TABLE OEUVRE
    // ==========================================
    // Plusieurs livres (LIVRE) peuvent correspondre à une seule oeuvre (OEUVRE)
    @ManyToOne
    @JoinColumn(name = "isbn", nullable = false)
    private Oeuvre oeuvre;

    // ==========================================
    // CONSTRUCTEURS, GETTERS ET SETTERS
    // ==========================================

    public Livre() {
    }

    public Integer getNumLivre() { return numLivre; }
    public void setNumLivre(Integer numLivre) { this.numLivre = numLivre; }

    public String getEtatLivre() { return etatLivre; }
    public void setEtatLivre(String etatLivre) { this.etatLivre = etatLivre; }

    public String getStatuLivre() { return statuLivre; }
    public void setStatuLivre(String statuLivre) { this.statuLivre = statuLivre; }

    public Oeuvre getOeuvre() { return oeuvre; }
    public void setOeuvre(Oeuvre oeuvre) { this.oeuvre = oeuvre; }
}