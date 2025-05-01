package dao;

import model.Reservation;
import model.Reservation.StatutReservation;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public int insertReservation(Reservation r) {
        String sql = "INSERT INTO Reservation (id_utilisateur, id_attraction, date_reservation, heure_reservation, nombre_billets, statut) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Si idUtilisateur = 0 (invité), on met NULL en base
            if (r.getIdUtilisateur() == 0) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, r.getIdUtilisateur());
            }

            ps.setInt(2, r.getIdAttraction());
            ps.setDate(3, Date.valueOf(r.getDateReservation()));

            if (r.getHeureReservation() != null) {
                ps.setTime(4, Time.valueOf(r.getHeureReservation()));
            } else {
                ps.setNull(4, Types.TIME);
            }

            ps.setInt(5, r.getNombreBillets());
            ps.setString(6, r.getStatut().name());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Échec de l'insertion de la réservation.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
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


    public boolean deleteReservation(int id) {
        String deleteFactureSQL = "DELETE FROM Facture WHERE id_reservation = ?";
        String deleteReservationSQL = "DELETE FROM Reservation WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // début transaction

            try (PreparedStatement psFacture = conn.prepareStatement(deleteFactureSQL);
                 PreparedStatement psReservation = conn.prepareStatement(deleteReservationSQL)) {

                psFacture.setInt(1, id);
                psFacture.executeUpdate(); // Supprime la facture liée

                psReservation.setInt(1, id);
                int affectedRows = psReservation.executeUpdate(); // Supprime la réservation

                conn.commit(); // Valide la transaction
                return affectedRows > 0;

            } catch (SQLException e) {
                conn.rollback(); // Rollback si problème
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Reservation mapReservation(ResultSet rs) throws SQLException {
        LocalTime heureReservation = null;
        Time sqlTime = rs.getTime("heure_reservation");
        if (sqlTime != null) {
            heureReservation = sqlTime.toLocalTime();
        }

        return new Reservation(
                rs.getInt("id"),
                rs.getInt("id_utilisateur"),
                rs.getInt("id_attraction"),
                rs.getDate("date_reservation").toLocalDate(),
                heureReservation,
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