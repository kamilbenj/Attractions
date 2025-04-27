package dao;

import model.Attraction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttractionDAO {

    public List<Attraction> getAllAttractions() {
        List<Attraction> attractions = new ArrayList<>();
        String sql = "SELECT * FROM Attraction";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                attractions.add(mapAttraction(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attractions;
    }

    public Attraction getAttractionById(int id) {
        String sql = "SELECT * FROM Attraction WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapAttraction(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertAttraction(Attraction a) {
        String sql = "INSERT INTO Attraction (nom, description, prix, capacite, disponible) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getNom());
            ps.setString(2, a.getDescription());
            ps.setDouble(3, a.getPrix());
            ps.setInt(4, a.getCapacite());
            ps.setBoolean(5, a.isDisponible());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateAttraction(Attraction a) {
        String sql = "UPDATE Attraction SET nom = ?, description = ?, prix = ?, capacite = ?, disponible = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getNom());
            ps.setString(2, a.getDescription());
            ps.setDouble(3, a.getPrix());
            ps.setInt(4, a.getCapacite());
            ps.setBoolean(5, a.isDisponible());
            ps.setInt(6, a.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteAttraction(int id) {
        String sql = "DELETE FROM Attraction WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Attraction mapAttraction(ResultSet rs) throws SQLException {
        return new Attraction(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("description"),
                rs.getDouble("prix"),
                rs.getInt("capacite"),
                rs.getBoolean("disponible")
        );
    }
}