package com.projetl2.bibliotheque.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "OEUVRE")
public class Oeuvre {

    @Id
    @Column(name = "isbn", length = 20)
    private String isbn;

    @Column(name = "nom_oeuvre", nullable = false, length = 255)
    private String nomOeuvre;

    @Column(name = "edit", length = 150)
    private String edit;

    @Column(name = "genre", length = 50)
    private String genre;

    @Column(name = "annee_paru", nullable = false)
    private Integer anneeParu;


    // ==========================================
    // RELATION INVERSE AVEC LA TABLE AUTEUR
    // ==========================================
    @ManyToMany(mappedBy = "oeuvres")
    // Quand on affiche les auteurs de cette oeuvre, on ne ré-affiche pas les oeuvres de ces auteurs
    @JsonIgnoreProperties("oeuvres") 
    private List<Auteur> auteurs;

    // ==========================================
    // CONSTRUCTEURS, GETTERS ET SETTERS
    // ==========================================

    public Oeuvre() {
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getNomOeuvre() {
        return nomOeuvre;
    }

    public void setNomOeuvre(String nomOeuvre) {
        this.nomOeuvre = nomOeuvre;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getAnneeParu() {
        return anneeParu;
    }

    public void setAnneeParu(Integer anneeParu) {
        this.anneeParu = anneeParu;
    }

    public List<Auteur> getAuteurs() {
        return auteurs;
    }

    public void setAuteurs(List<Auteur> auteurs) {
        this.auteurs = auteurs;
    }
}