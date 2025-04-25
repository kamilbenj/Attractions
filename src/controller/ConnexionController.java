package controller;

import dao.UtilisateurDAO;
import model.Utilisateur;
import model.Utilisateur.TypeUtilisateur;

public class ConnexionController {

    private final UtilisateurDAO utilisateurDAO;

    public ConnexionController() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    /**
     * Vérifie les identifiants et retourne l'utilisateur connecté s'ils sont valides.
     */
    public Utilisateur connecter(String email, String motDePasse) {
        Utilisateur utilisateur = utilisateurDAO.getUtilisateurByEmail(email);
        if (utilisateur != null && utilisateur.getMotDePasse().equals(motDePasse)) {
            return utilisateur;
        }
        return null; // Connexion échouée
    }

    /**
     * Inscription d’un nouvel utilisateur (client ou membre uniquement ici).
     */
    public boolean inscrire(String nom, String email, String motDePasse, TypeUtilisateur type, int age) {
        if (utilisateurDAO.getUtilisateurByEmail(email) != null) {
            return false; // Email déjà pris
        }

        Utilisateur nouveau = new Utilisateur(nom, email, motDePasse, type, age, java.time.LocalDate.now());
        return utilisateurDAO.insertUtilisateur(nouveau);
    }
}