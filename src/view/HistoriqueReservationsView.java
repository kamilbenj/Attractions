package view;

import controller.ReservationController;
import dao.AttractionDAO;
import model.Attraction;
import model.Reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoriqueReservationsView extends JFrame {

    private final int idClient;
    private final ReservationController reservationController;
    private final AttractionDAO attractionDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public HistoriqueReservationsView(int idClient) {
        this.idClient = idClient;
        this.reservationController = new ReservationController();
        this.attractionDAO = new AttractionDAO();
        initUI();
        loadReservations();
    }

    private void initUI() {
        setTitle("Mes réservations");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Attraction", "Date", "Heure", "Billets", "Statut"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadReservations() {
        tableModel.setRowCount(0);
        List<Reservation> reservations = reservationController.getHistoriqueUtilisateur(idClient);

        // Chargement des noms d'attractions (évite requêtes multiples)
        Map<Integer, String> attractionMap = new HashMap<>();
        attractionDAO.getAllAttractions().forEach(a -> attractionMap.put(a.getId(), a.getNom()));

        for (Reservation r : reservations) {
            tableModel.addRow(new Object[]{
                    r.getId(),
                    attractionMap.getOrDefault(r.getIdAttraction(), "Inconnue"),
                    r.getDateReservation(),
                    (r.getHeureReservation() != null ? r.getHeureReservation() : "-"),
                    r.getNombreBillets(),
                    r.getStatut()
            });
        }
    }
}