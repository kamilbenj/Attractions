package model;

public class UtilisateurReduction {

    private int idUtilisateur;
    private int idReduction;

    // Constructeurs
    public UtilisateurReduction() {}

    public UtilisateurReduction(int idUtilisateur, int idReduction) {
        this.idUtilisateur = idUtilisateur;
        this.idReduction = idReduction;
    }

    // Getters & Setters
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdReduction() {
        return idReduction;
    }

    public void setIdReduction(int idReduction) {
        this.idReduction = idReduction;
    }

    @Override
    public String toString() {
        return "Utilisateur ID : " + idUtilisateur + " ⇄ Réduction ID : " + idReduction;
    }
}