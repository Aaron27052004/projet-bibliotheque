package com.projetl2.bibliotheque.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ADHERENT")
public class Adherent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_adher")
    private Integer numAdher;

    @Column(name = "nom_adher", length = 100)
    private String nomAdher;

    @Column(name = "prenom_adher", length = 100)
    private String prenomAdher;

    @Column(name = "naiss_adher")
    private LocalDate naissAdher;

    @Column(name = "num_rue_adher", length = 10)
    private String numRueAdher;

    @Column(name = "rue_adher", length = 150)
    private String rueAdher;

    @Column(name = "cp_adher", length = 10)
    private String cpAdher;

    @Column(name = "ville_adher", length = 100)
    private String villeAdher;

    @Column(name = "mail_adher", length = 150)
    private String mailAdher;

    @Column(name = "date_adhesion")
    private LocalDate dateAdhesion;

    @Column(name = "date_dernier_pay")
    private LocalDate dateDernierPay;

    @Column(name = "num_tel", nullable = false, length = 20)
    private String numTel;

    // ==========================================
    // CONSTRUCTEURS, GETTERS ET SETTERS
    // ==========================================

    public Adherent() {
    }

    public Integer getNumAdher() { return numAdher; }
    public void setNumAdher(Integer numAdher) { this.numAdher = numAdher; }

    public String getNomAdher() { return nomAdher; }
    public void setNomAdher(String nomAdher) { this.nomAdher = nomAdher; }

    public String getPrenomAdher() { return prenomAdher; }
    public void setPrenomAdher(String prenomAdher) { this.prenomAdher = prenomAdher; }

    public LocalDate getNaissAdher() { return naissAdher; }
    public void setNaissAdher(LocalDate naissAdher) { this.naissAdher = naissAdher; }

    public String getNumRueAdher() { return numRueAdher; }
    public void setNumRueAdher(String numRueAdher) { this.numRueAdher = numRueAdher; }

    public String getRueAdher() { return rueAdher; }
    public void setRueAdher(String rueAdher) { this.rueAdher = rueAdher; }

    public String getCpAdher() { return cpAdher; }
    public void setCpAdher(String cpAdher) { this.cpAdher = cpAdher; }

    public String getVilleAdher() { return villeAdher; }
    public void setVilleAdher(String villeAdher) { this.villeAdher = villeAdher; }

    public String getMailAdher() { return mailAdher; }
    public void setMailAdher(String mailAdher) { this.mailAdher = mailAdher; }

    public LocalDate getDateAdhesion() { return dateAdhesion; }
    public void setDateAdhesion(LocalDate dateAdhesion) { this.dateAdhesion = dateAdhesion; }

    public LocalDate getDateDernierPay() { return dateDernierPay; }
    public void setDateDernierPay(LocalDate dateDernierPay) { this.dateDernierPay = dateDernierPay; }

    public String getNumTel() { return numTel; }
    public void setNumTel(String numTel) { this.numTel = numTel; }
}