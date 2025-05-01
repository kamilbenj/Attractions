package controller;

import dao.UtilisateurDAO; //bdd
import model.Utilisateur; //objet métier
import model.Utilisateur.TypeUtilisateur; //gérer les roles

/**
 * Contrôleur chargé de gérer la connexion et l'inscription des utilisateurs
 * Fait le lien entre l'interface utilisateur (vue) et les opérations sur les données (DAO)
 * Elle utilise {@link UtilisateurDAO} pour accéder à la base de données.
 */
public class ConnexionController {

    /** DAO utilisé pour accéder aux données des utilisateurs sur la bdd */
    private final UtilisateurDAO utilisateurDAO;
    // + final car l'objet ne va jamais changer

    /**
     * Constructeur : initialise le contrôleur avec un DAO
     */
    public ConnexionController() {
        this.utilisateurDAO = new UtilisateurDAO();
    }
    //instancie un nouvel objet UtilisateurDAO

    /**
     * Tente de connecter un utilisateur en vérifiant son email et mot de passe
     *
     * @param email L'email saisi par l'utilisateur
     * @param motDePasse Le mot de passe saisi
     * @return L'utilisateur connecté si les identifiants sont valides, sinon {@code null}
     */
    public Utilisateur connecter(String email, String motDePasse) {
        Utilisateur utilisateur = utilisateurDAO.getUtilisateurByEmail(email);
        if (utilisateur != null && utilisateur.getMotDePasse().equals(motDePasse)) {
            return utilisateur; //vérifie que l'utilisateur existe et que le mdp correspond
        }
        return null; // Connexion échouée
    }

    /**
     * Inscrit un nouvel utilisateur dans la base de données
     * Vérifie d'abord que l'email n'est pas déjà utilisé
     *
     * @param nom Nom de l'utilisateur
     * @param email Adresse email (doit être unique)
     * @param motDePasse Mot de passe
     * @param type Type d'utilisateur (CLIENT, MEMBRE, etc...)
     * @param age Âge de l'utilisateur
     * @return {@code true} si l'inscription a réussi, {@code false} sinon
     */
    public boolean inscrire(String nom, String email, String motDePasse, TypeUtilisateur type, int age) {
        if (utilisateurDAO.getUtilisateurByEmail(email) != null) {
            return false; // Email déjà pris
        }

        Utilisateur nouveau = new Utilisateur(nom, email, motDePasse, type, age, java.time.LocalDate.now());
        return utilisateurDAO.insertUtilisateur(nouveau);
    }
}