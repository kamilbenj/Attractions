package model;

import java.time.LocalDate;

public class Utilisateur {

    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private TypeUtilisateur type;
    private int age;
    private LocalDate dateInscription;

    // Enumération pour les types d'utilisateurs
    public enum TypeUtilisateur {
        INVITE, CLIENT, MEMBRE, ADMIN
    }

    // Constructeurs


    public Utilisateur(int id, String nom, String email, String motDePasse, TypeUtilisateur type, int age, LocalDate dateInscription) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
        this.age = age;
        this.dateInscription = dateInscription;
    }

    public Utilisateur(String nom, String email, String motDePasse, TypeUtilisateur type, int age, LocalDate dateInscription) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
        this.age = age;
        this.dateInscription = dateInscription;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }


    public String getMotDePasse() {
        return motDePasse;
    }


    public TypeUtilisateur getType() {
        return type;
    }


    public int getAge() {
        return age;
    }


    public LocalDate getDateInscription() {
        return dateInscription;
    }


    @Override
    public String toString() {
        return nom + " (" + email + ") - " + type;
    }
}