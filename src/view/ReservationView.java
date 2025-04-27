package view;

import controller.ReservationController;
import dao.AttractionDAO;
import dao.ReservationDAO;
import model.Attraction;
import model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jdatepicker.impl.*;

public class ReservationView extends JFrame {

    private final ReservationController reservationController;
    private final ReservationDAO reservationDAO;
    private final AttractionDAO attractionDAO;
    private final int idClient;

    private JComboBox<Attraction> attractionBox;
    private JDatePickerImpl datePicker;
    private JComboBox<String> heureBox;
    private JSpinner nbBilletsSpinner;
    private JButton reserverButton;
    private JLabel messageLabel;

    public ReservationView(int idClient) {
        this.idClient = idClient;
        this.reservationController = new ReservationController();
        this.reservationDAO = new ReservationDAO();
        this.attractionDAO = new AttractionDAO();
        initUI();
    }

    private void initUI() {
        setTitle("Réserver une attraction");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        attractionBox = new JComboBox<>();
        initAttractionBox();

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Aujourd'hui");
        p.put("text.month", "Mois");
        p.put("text.year", "Année");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        heureBox = new JComboBox<>();
        updateHeureBox();

        nbBilletsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        reserverButton = new JButton("Réserver");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        panel.add(new JLabel("Attraction :"));
        panel.add(attractionBox);
        panel.add(new JLabel("Date :"));
        panel.add(datePicker);
        panel.add(new JLabel("Heure :"));
        panel.add(heureBox);
        panel.add(new JLabel("Nombre de billets :"));
        panel.add(nbBilletsSpinner);
        panel.add(new JLabel(""));
        panel.add(reserverButton);

        add(panel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        attractionBox.addActionListener(e -> updateHeureBox());
        datePicker.addActionListener(e -> updateHeureBox());
        reserverButton.addActionListener(e -> handleReservation());

        setVisible(true);
    }

    private void initAttractionBox() {
        List<Attraction> attractions = attractionDAO.getAllAttractions();
        for (Attraction a : attractions) {
            if (a.isDisponible()) {
                attractionBox.addItem(a);
            }
        }
    }

    private void updateHeureBox() {
        heureBox.removeAllItems();
        Attraction selectedAttraction = (Attraction) attractionBox.getSelectedItem();
        if (selectedAttraction == null) return;

        LocalDate selectedDate = getSelectedDate();
        if (selectedDate == null) return;

        List<Reservation> reservations = reservationDAO.getAllReservations();
        List<LocalTime> heuresReservees = new ArrayList<>();

        for (Reservation r : reservations) {
            if (r.getIdAttraction() == selectedAttraction.getId() &&
                    r.getDateReservation().isEqual(selectedDate)) {
                heuresReservees.add(r.getHeureReservation());
            }
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (int heure = 10; heure <= 18; heure++) {
            LocalTime t = LocalTime.of(heure, 0);
            if (heuresReservees.contains(t)) {
                model.addElement(heure + "h00 (indisponible)");
            } else {
                model.addElement(heure + "h00");
            }
        }
        heureBox.setModel(model);
    }

    private LocalDate getSelectedDate() {
        if (datePicker.getModel().getValue() != null) {
            java.util.Date selected = (java.util.Date) datePicker.getModel().getValue();
            return new java.sql.Date(selected.getTime()).toLocalDate();
        }
        return null;
    }

    private void handleReservation() {
        Attraction attraction = (Attraction) attractionBox.getSelectedItem();
        LocalDate date = getSelectedDate();
        String heureSelection = (String) heureBox.getSelectedItem();
        int nbBillets = (int) nbBilletsSpinner.getValue();

        if (attraction == null || date == null || heureSelection == null) {
            showMessage("Veuillez remplir tous les champs.", Color.RED);
            return;
        }

        if (heureSelection.contains("indisponible")) {
            showMessage("❌ Sélectionnez une heure disponible.", Color.RED);
            return;
        }

        int heure = Integer.parseInt(heureSelection.substring(0, heureSelection.indexOf("h")));
        LocalTime heureFinale = LocalTime.of(heure, 0);

        // 🎯 Appel PaymentView pour valider avant la réservation
        new PaymentView(() -> {
            boolean success = reservationController.reserverAttraction(idClient, attraction.getId(), date, heureFinale, nbBillets);
            if (success) {
                String resume = "Attraction réservée : " + attraction.getNom() +
                        ", le " + date +
                        " à " + heure + "h00";
                JOptionPane.showMessageDialog(this, resume, "Résumé de réservation", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                showMessage("❌ Erreur lors de la réservation.", Color.RED);
            }
        });
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }
}