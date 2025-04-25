package view;

import controller.ReservationController;
import dao.AttractionDAO;
import dao.UtilisateurDAO;
import model.Attraction;
import model.Reservation;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllReservationsView extends JFrame {

    private final ReservationController reservationController;
    private final UtilisateurDAO utilisateurDAO;
    private final AttractionDAO attractionDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public AllReservationsView() {
        this.reservationController = new ReservationController();
        this.utilisateurDAO = new UtilisateurDAO();
        this.attractionDAO = new AttractionDAO();
        initUI();
        loadReservations();
    }

    private void initUI() {
        setTitle("Toutes les réservations");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Client", "Attraction", "Date", "Billets", "Statut"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadReservations() {
        tableModel.setRowCount(0);
        List<Reservation> reservations = reservationController.getToutesReservations();

        // Caches pour éviter de requêter en boucle
        Map<Integer, String> utilisateurs = new HashMap<>();
        Map<Integer, String> attractions = new HashMap<>();

        for (Utilisateur u : utilisateurDAO.getAllUtilisateurs()) {
            utilisateurs.put(u.getId(), u.getNom());
        }

        for (Attraction a : attractionDAO.getAllAttractions()) {
            attractions.put(a.getId(), a.getNom());
        }

        for (Reservation r : reservations) {
            tableModel.addRow(new Object[]{
                    r.getId(),
                    utilisateurs.getOrDefault(r.getIdUtilisateur(), "Inconnu"),
                    attractions.getOrDefault(r.getIdAttraction(), "Inconnue"),
                    r.getDateReservation(),
                    r.getNombreBillets(),
                    r.getStatut()
            });
        }
    }
}