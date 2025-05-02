package dao;

import model.Reservation;
import model.Reservation.StatutReservation;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO permettant d'effectuer les opérations CRUD sur la table Reservation
 * Ce DAO utilise JDBC pour accéder à la base de données
 * Il permet d'insérer, de récupérer, de supprimer des réservations, et de les lier à un utilisateur
 */
public class ReservationDAO {

    /**
     * Insère une réservation en base de données et retourne son ID
     * Si l'utilisateur est un invité (id = 0), NULL est inséré
     *
     * @param r Réservation à insérer
     * @return L'identifiant généré si succès, -1 sinon
     */
    public int insertReservation(Reservation r) {
        String sql = "INSERT INTO Reservation (id_utilisateur, id_attraction, date_reservation, heure_reservation, nombre_billets, statut) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            //Si idUtilisateur = 0 (invité), on met NULL en base
            if (r.getIdUtilisateur() == 0) { //Si l’utilisateur est un invité (id = 0), on insère NULL
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, r.getIdUtilisateur());
            }

            ps.setInt(2, r.getIdAttraction()); //Données obligatoires : attraction et date.
            ps.setDate(3, Date.valueOf(r.getDateReservation()));

            if (r.getHeureReservation() != null) {
                ps.setTime(4, Time.valueOf(r.getHeureReservation()));
            } else {
                ps.setNull(4, Types.TIME);
            }

            ps.setInt(5, r.getNombreBillets()); //Nombre de billets et statut sous forme de texte
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

    /**
     * Récupère toutes les réservations associées à un utilisateur
     *
     * @param idUtilisateur ID de l'utilisateur
     * @return Liste des réservations trouvées
     */
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

    /**
     * Récupère toutes les réservations de la base de données
     *
     * @return Liste de toutes les réservations
     */
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


    /**
     * Supprime une réservation ainsi que la facture associée
     * Cette opération est exécutée en transaction
     *
     * @param id ID de la réservation à supprimer
     * @return true si la suppression a réussi, false sinon
     */
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
                int affectedRows = psReservation.executeUpdate(); //supprime la réservation

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

    /**
     * Transforme un résultat SQL en objet {@link Reservation}
     *
     * @param rs Résultat SQL
     * @return Objet Reservation correspondant
     * @throws SQLException si erreur d'accès aux colonnes
     */
    private Reservation mapReservation(ResultSet rs) throws SQLException { //Convertit une ligne SQL en objet Java Reservation
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