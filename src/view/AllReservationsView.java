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
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Client", "Attraction", "Date", "Heure", "Billets", "Statut"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton supprimerButton = new JButton("Supprimer réservation");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(supprimerButton);

        add(buttonPanel, BorderLayout.SOUTH);

        supprimerButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int reservationId = (int) tableModel.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Êtes-vous sûr de vouloir supprimer cette réservation ?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = reservationController.supprimerReservation(reservationId);

                    if (deleted) {
                        JOptionPane.showMessageDialog(this, "Réservation supprimée avec succès !");
                        loadReservations();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void loadReservations() {
        tableModel.setRowCount(0);
        List<Reservation> reservations = reservationController.getToutesReservations();

        Map<Integer, String> utilisateurs = new HashMap<>();
        Map<Integer, String> attractions = new HashMap<>();

        for (Utilisateur u : utilisateurDAO.getAllUtilisateurs()) {
            utilisateurs.put(u.getId(), u.getNom());
        }

        for (Attraction a : attractionDAO.getAllAttractions()) {
            attractions.put(a.getId(), a.getNom());
        }

        for (Reservation r : reservations) {
            String clientNom = utilisateurs.getOrDefault(r.getIdUtilisateur(), "Invité");
            String attractionNom = attractions.getOrDefault(r.getIdAttraction(), "Inconnue");

            tableModel.addRow(new Object[]{
                    r.getId(),
                    clientNom,
                    attractionNom,
                    r.getDateReservation(),
                    (r.getHeureReservation() != null ? r.getHeureReservation() : "-"),
                    r.getNombreBillets(),
                    r.getStatut()
            });
        }
    }
}