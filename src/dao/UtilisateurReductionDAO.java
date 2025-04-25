package dao;

import model.UtilisateurReduction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurReductionDAO {

    public boolean assignerReduction(int idUtilisateur, int idReduction) {
        String sql = "INSERT INTO UtilisateurReduction (id_utilisateur, id_reduction) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idReduction);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<UtilisateurReduction> getReductionsByUtilisateur(int idUtilisateur) {
        List<UtilisateurReduction> list = new ArrayList<>();
        String sql = "SELECT * FROM UtilisateurReduction WHERE id_utilisateur = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new UtilisateurReduction(rs.getInt("id_utilisateur"), rs.getInt("id_reduction")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean supprimerReductionPourUtilisateur(int idUtilisateur, int idReduction) {
        String sql = "DELETE FROM UtilisateurReduction WHERE id_utilisateur = ? AND id_reduction = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ps.setInt(2, idReduction);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}