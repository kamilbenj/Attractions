package model;

import java.time.LocalDate;

/**
 * Représente un utilisateur de l'application
 * Cette classe contient les informations personnelles de l'utilisateur
 * son type (client, invité, admin, etc.), et sa date d'inscription
 *
 * Elle sert de modèle de données dans le système
 */
public class Utilisateur { //objet métier

    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private TypeUtilisateur type;
    private int age;
    private LocalDate dateInscription;

    /**
     * Enumération représentant les différents types d'utilisateurs.
     */
    public enum TypeUtilisateur {
        INVITE, CLIENT, MEMBRE, ADMIN
    }

    /**
     * Constructeur complet avec id (utilisé généralement lors de la lecture depuis la bdd)
     *
     * @param id              Identifiant unique
     * @param nom             Nom de l'utilisateur
     * @param email           Adresse email
     * @param motDePasse      Mot de passe
     * @param type            Type d'utilisateur
     * @param age             Âge
     * @param dateInscription Date d'inscription
     */
    public Utilisateur(int id, String nom, String email, String motDePasse, TypeUtilisateur type, int age, LocalDate dateInscription) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
        this.age = age;
        this.dateInscription = dateInscription;
    }

    /**
     * Constructeur sans id (utilisé avant insertion en bdd)
     *
     * @param nom             Nom de l'utilisateur
     * @param email           Adresse email
     * @param motDePasse      Mot de passe
     * @param type            Type d'utilisateur
     * @param age             Âge
     * @param dateInscription Date d'inscription
     */
    public Utilisateur(String nom, String email, String motDePasse, TypeUtilisateur type, int age, LocalDate dateInscription) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
        this.age = age;
        this.dateInscription = dateInscription;
    }

    /** Getters et Setters */
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


    /**
     * Représentation lisible de l'utilisateur
     *
     * @return Une chaîne formatée contenant le nom, l'email et le type
     */
    @Override //surcharge d'une méthode héritée d’une interface
    public String toString() {
        return nom + " (" + email + ") - " + type;
    } //Ex : Alice (alice@mail.com) - CLIENT
}