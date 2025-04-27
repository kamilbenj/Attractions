package view;

import controller.ClientController;
import controller.ReservationController;
import dao.AttractionDAO;
import model.Attraction;
import model.Facture;
import model.Reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacturesView extends JFrame {

    private final int idClient;
    private final ClientController clientController;
    private final ReservationController reservationController;
    private final AttractionDAO attractionDAO;

    private JTable table;
    private DefaultTableModel tableModel;

    public FacturesView(int idClient) {
        this.idClient = idClient;
        this.clientController = new ClientController();
        this.reservationController = new ReservationController();
        this.attractionDAO = new AttractionDAO();
        initUI();
        loadFactures();
    }

    private void initUI() {
        setTitle("Mes factures");
        setSize(700, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID Facture", "Attraction", "Date", "Heure", "Montant", "Réduction"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadFactures() {
        tableModel.setRowCount(0);
        List<Facture> factures = clientController.getFacturesClient(idClient);
        List<Reservation> reservations = reservationController.getHistoriqueUtilisateur(idClient);
        Map<Integer, Reservation> resMap = new HashMap<>();
        Map<Integer, String> attractionMap = new HashMap<>();

        for (Reservation r : reservations) {
            resMap.put(r.getId(), r);
        }

        for (Attraction a : attractionDAO.getAllAttractions()) {
            attractionMap.put(a.getId(), a.getNom());
        }

        for (Facture f : factures) {
            Reservation r = resMap.get(f.getIdReservation());
            String attraction = (r != null) ? attractionMap.getOrDefault(r.getIdAttraction(), "Inconnue") : "Inconnue";
            tableModel.addRow(new Object[]{
                    f.getId(),
                    attraction,
                    (r != null ? r.getDateReservation() : "-"),
                    (r != null && r.getHeureReservation() != null ? r.getHeureReservation() : "-"),
                    f.getMontantTotal() + " €",
                    f.isReductionAppliquee() ? "✅ Oui" : "❌ Non"
            });
        }
    }
}