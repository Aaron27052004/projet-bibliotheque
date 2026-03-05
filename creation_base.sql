-- Création de la base (si elle n'existe pas) et sélection
CREATE DATABASE IF NOT EXISTS bibliotheque;
USE bibliotheque;

-- ==========================================
-- 1. TABLES PARENTS (Sans clés étrangères)
-- ==========================================

CREATE TABLE OEUVRE (
    isbn VARCHAR(20) PRIMARY KEY,
    nom_oeuvre VARCHAR(255) NOT NULL,
    edit VARCHAR(150),
    genre VARCHAR(50),
    annee_paru INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE AUTEUR (
    num_aut INT AUTO_INCREMENT PRIMARY KEY,
    nom_aut VARCHAR(100),
    prenom_aut VARCHAR(100)
) ENGINE=InnoDB;

CREATE TABLE ADHERENT (
    num_adher INT AUTO_INCREMENT PRIMARY KEY,
    nom_adher VARCHAR(100),
    prenom_adher VARCHAR(100),
    naiss_adher DATE,
    num_rue_adher VARCHAR(10),
    rue_adher VARCHAR(150),
    cp_adher VARCHAR(10),
    ville_adher VARCHAR(100),
    mail_adher VARCHAR(150),
    date_adhesion DATE,
    date_dernier_pay DATE,
    num_tel VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

-- ==========================================
-- 2. TABLES ENFANTS (Avec clés étrangères)
-- ==========================================

-- Table d'association entre AUTEUR et OEUVRE
CREATE TABLE ECRIRE (
    num_aut INT,
    isbn VARCHAR(20),
    PRIMARY KEY (num_aut, isbn),
    FOREIGN KEY (num_aut) REFERENCES AUTEUR(num_aut),
    FOREIGN KEY (isbn) REFERENCES OEUVRE(isbn)
) ENGINE=InnoDB;

CREATE TABLE LIVRE (
    num_livre INT AUTO_INCREMENT PRIMARY KEY,
    etat_livre ENUM('Neuf', 'Bon', 'Abimé') DEFAULT 'Neuf',
    statu_livre ENUM('En rayon', 'Emprunté') DEFAULT 'En rayon',
    isbn VARCHAR(20) NOT NULL,
    FOREIGN KEY (isbn) REFERENCES OEUVRE(isbn)
) ENGINE=InnoDB;

CREATE TABLE EMPRUNT (
    num_emp INT AUTO_INCREMENT PRIMARY KEY,
    date_deb_emp DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retournee DATE, -- Peut être NULL si le livre n'est pas encore rendu
    statut_emp ENUM('en cours', 'cloturé', 'en retard') DEFAULT 'en cours',
    num_livre INT NOT NULL,
    num_adher INT NOT NULL,
    FOREIGN KEY (num_livre) REFERENCES LIVRE(num_livre),
    FOREIGN KEY (num_adher) REFERENCES ADHERENT(num_adher)
) ENGINE=InnoDB;