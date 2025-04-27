package dao;

import model.Facture;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO {

    public boolean insertFacture(Facture f) {
        String sql = "INSERT INTO Facture (id_reservation, montant_total, date_facture, reduction_appliquee) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getIdReservation());
            ps.setDouble(2, f.getMontantTotal());
            ps.setDate(3, Date.valueOf(f.getDateFacture()));
            ps.setBoolean(4, f.isReductionAppliquee());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Facture> getFacturesByReservation(int reservationId) {
        List<Facture> list = new ArrayList<>();
        String sql = "SELECT * FROM Facture WHERE id_reservation = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapFacture(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    private Facture mapFacture(ResultSet rs) throws SQLException {
        return new Facture(
                rs.getInt("id"),
                rs.getInt("id_reservation"),
                rs.getDouble("montant_total"),
                rs.getDate("date_facture").toLocalDate(),
                rs.getBoolean("reduction_appliquee")
        );
    }
}