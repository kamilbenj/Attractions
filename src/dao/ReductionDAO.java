package dao;

import model.Reduction;
import model.Reduction.CritereReduction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReductionDAO {

    public boolean insertReduction(Reduction r) {
        String sql = "INSERT INTO Reduction (nom, pourcentage, critere) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getNom());
            ps.setInt(2, r.getPourcentage());
            ps.setString(3, r.getCritere().name());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    public List<Reduction> getAllReductions() {
        List<Reduction> list = new ArrayList<>();
        String sql = "SELECT * FROM Reduction";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapReduction(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean deleteReduction(int id) {
        String sql = "DELETE FROM Reduction WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Reduction mapReduction(ResultSet rs) throws SQLException {
        return new Reduction(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getInt("pourcentage"),
                CritereReduction.valueOf(
                        rs.getString("critere")
                                .toUpperCase()
                                .replace("É", "E")
                                .replace("È", "E")
                                .replace("À", "A")
                )
        );
    }
}