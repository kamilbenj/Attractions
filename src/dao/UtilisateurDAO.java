package dao;

import model.Utilisateur;
import model.Utilisateur.TypeUtilisateur;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    public boolean insertUtilisateur(Utilisateur u) {
        String sql = "INSERT INTO Utilisateur (nom, email, mot_de_passe, type, age, date_inscription) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNom());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getMotDePasse());
            ps.setString(4, u.getType().name());
            ps.setInt(5, u.getAge());
            ps.setDate(6, Date.valueOf(u.getDateInscription()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Utilisateur getUtilisateurById(int id) {
        String sql = "SELECT * FROM Utilisateur WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapUtilisateur(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Utilisateur getUtilisateurByEmail(String email) {
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

    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                liste.add(mapUtilisateur(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }



    private Utilisateur mapUtilisateur(ResultSet rs) throws SQLException {
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