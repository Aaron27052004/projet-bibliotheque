package com.projetl2.bibliotheque.entity;

import jakarta.persistence.*;

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
}