package dao;

import model.Utilisateur;
import model.Utilisateur.TypeUtilisateur;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) pour la gestion des utilisateurs
 * Cette classe permet d'effectuer les opérations CRUD de base
 * sur la table {@code Utilisateur} de la base de données
 *
 * Elle utilise la classe {@link DatabaseConnection} pour établir les connexions SQL
 */
public class UtilisateurDAO {

    /**
     * Insère un nouvel utilisateur dans la base de données
     *
     * @param u L'utilisateur à insérer
     * @return true si l'insertion a réussi, false sinon
     */
    public boolean insertUtilisateur(Utilisateur u) {
        String sql = "INSERT INTO Utilisateur (nom, email, mot_de_passe, type, age, date_inscription) VALUES (?, ?, ?, ?, ?, ?)"; // ? pour proteger contre les injections sql

        try (Connection conn = DatabaseConnection.getConnection(); //On récupère la connection
             PreparedStatement ps = conn.prepareStatement(sql)) { //préparation requête SQL à exécuter

            ps.setString(1, u.getNom()); //on remplace les ?
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getMotDePasse());
            ps.setString(4, u.getType().name());
            ps.setInt(5, u.getAge());
            ps.setDate(6, Date.valueOf(u.getDateInscription())); //Date.valueOf(LocalDate) convertit une date Java (LocalDate) en java.sql.Date

            return ps.executeUpdate() > 0; //execute la requete

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Récupère un utilisateur à partir de son identifiant unique
     *
     * @param id L'identifiant de l'utilisateur
     * @return L'utilisateur correspondant, ou null s'il n'existe pas
     */
    public Utilisateur getUtilisateurById(int id) { //recherche utilisateur
        String sql = "SELECT * FROM Utilisateur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) { //si ligne trouvé -> converti en objet Utilisateur
                return mapUtilisateur(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupère un utilisateur à partir de son adresse email
     *
     * @param email L'adresse email de l'utilisateur
     * @return L'utilisateur correspondant, ou null s'il n'existe pas
     */
    public Utilisateur getUtilisateurByEmail(String email) { //authentification d'un utilisateur
        String sql = "SELECT * FROM Utilisateur WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapUtilisateur(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupère tous les utilisateurs de la base de données
     *
     * @return Une liste contenant tous les utilisateurs
     */
    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> liste = new ArrayList<>(); //liste vide pour stocker les utilisateurs
        String sql = "SELECT * FROM Utilisateur"; //requete pour récupérer tous les utilisateurs

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) { //On parcourt les résultats et on remplit la liste avec les utilisateurs
                liste.add(mapUtilisateur(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }

    /**
     * Convertit une ligne de résultat SQL en un objet {@link Utilisateur}
     *
     * @param rs Le résultat de la requête SQL
     * @return Un objet Utilisateur rempli avec les données de la ligne
     * @throws SQLException En cas d'erreur lors de l'accès aux données
     */
    private Utilisateur mapUtilisateur(ResultSet rs) throws SQLException { //Transforme un ResultSet SQL en objet Java Utilisateur
        return new Utilisateur(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("email"),
                rs.getString("mot_de_passe"),
                TypeUtilisateur.valueOf(rs.getString("type").toUpperCase()),
                rs.getInt("age"),
                rs.getDate("date_inscription") != null ? rs.getDate("date_inscription").toLocalDate() : null
        );
    }
}