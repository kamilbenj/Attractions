package dao;

import model.Reservation;
import model.Reservation.StatutReservation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public int insertReservation(Reservation r) {
        String sql = "INSERT INTO Reservation (id_utilisateur, id_attraction, date_reservation, nombre_billets, statut) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getIdUtilisateur());
            ps.setInt(2, r.getIdAttraction());
            ps.setDate(3, Date.valueOf(r.getDateReservation()));
            ps.setInt(4, r.getNombreBillets());
            ps.setString(5, r.getStatut().name());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de l'insertion de la réservation.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // retourne l'ID de la réservation
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // insertion échouée
    }

    public Reservation getReservationById(int id) {
        String sql = "SELECT * FROM Reservation WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapReservation(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Reservation> getReservationsByUtilisateur(int idUtilisateur) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE id_utilisateur = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapReservation(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM Reservation";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapReservation(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateReservation(Reservation r) {
        String sql = "UPDATE Reservation SET id_utilisateur = ?, id_attraction = ?, date_reservation = ?, nombre_billets = ?, statut = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getIdUtilisateur());
            ps.setInt(2, r.getIdAttraction());
            ps.setDate(3, Date.valueOf(r.getDateReservation()));
            ps.setInt(4, r.getNombreBillets());
            ps.setString(5, r.getStatut().name());
            ps.setInt(6, r.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteReservation(int id) {
        String sql = "DELETE FROM Reservation WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Reservation mapReservation(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getInt("id"),
                rs.getInt("id_utilisateur"),
                rs.getInt("id_attraction"),
                rs.getDate("date_reservation").toLocalDate(),
                rs.getInt("nombre_billets"),
                StatutReservation.valueOf(
                        rs.getString("statut")
                                .toUpperCase()
                                .replace("É", "E")
                                .replace("È", "E")
                                .replace("À", "A")
                )
        );
    }
}