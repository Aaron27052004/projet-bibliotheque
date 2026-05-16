package com.projetl2.bibliotheque.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.util.List;

@Entity // Dit à Hibernate : "Ceci est une table de la base de données"
@Table(name = "AUTEUR") // Précise le nom exact de la table dans MySQL
public class Auteur {

    @Id // Dit à Hibernate : "Ceci est la clé primaire"
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Correspond au AUTO_INCREMENT de MySQL
    @Column(name = "num_aut") // Fait le lien avec le nom exact de la colonne MySQL
    private Integer numAut;

    @Column(name = "nom_aut")
    private String nomAut;

    @Column(name = "prenom_aut")
    private String prenomAut;


    // ==========================================
    // RELATION AVEC LA TABLE OEUVRE (Via ECRIRE)
    // ==========================================
    @ManyToMany
    @JoinTable(
        name = "ECRIRE", 
        joinColumns = @JoinColumn(name = "num_aut"), 
        inverseJoinColumns = @JoinColumn(name = "isbn") 
    )
    // Quand on affiche les oeuvres de cet auteur, on ne ré-affiche pas les auteurs de ces oeuvres
    @JsonIgnoreProperties("auteurs") 
    private List<Oeuvre> oeuvres;

    // ==========================================
    // GETTERS ET SETTERS (Indispensables pour Spring)
    // ==========================================

    // Constructeur vide obligatoire pour Hibernate
    public Auteur() {
    }

    public Integer getNumAut() {
        return numAut;
    }

    public void setNumAut(Integer numAut) {
        this.numAut = numAut;
    }

    public String getNomAut() {
        return nomAut;
    }

    public void setNomAut(String nomAut) {
        this.nomAut = nomAut;
    }

    public String getPrenomAut() {
        return prenomAut;
    }

    public void setPrenomAut(String prenomAut) {
        this.prenomAut = prenomAut;
    }

    public List<Oeuvre> getOeuvres() {
        return oeuvres;
    }

    public void setOeuvres(List<Oeuvre> oeuvres) {
        this.oeuvres = oeuvres;
    }
}