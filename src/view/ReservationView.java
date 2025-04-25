package view;

import controller.ReservationController;
import dao.AttractionDAO;
import model.Attraction;
import model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ReservationView extends JFrame {

    private final ReservationController reservationController;
    private final AttractionDAO attractionDAO;
    private final int idClient;

    private JComboBox<Attraction> attractionBox;
    private JSpinner nbBilletsSpinner;
    private JTextField dateField;
    private JButton reserverButton;
    private JLabel messageLabel;

    public ReservationView(int idClient) {
        this.idClient = idClient;
        this.reservationController = new ReservationController();
        this.attractionDAO = new AttractionDAO();
        initUI();
    }

    private void initUI() {
        setTitle("Réserver une attraction");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        attractionBox = new JComboBox<>();
        nbBilletsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        dateField = new JTextField("2025-04-30"); // format ISO
        reserverButton = new JButton("Réserver");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        panel.add(new JLabel("Attraction :"));
        panel.add(attractionBox);
        panel.add(new JLabel("Nombre de billets :"));
        panel.add(nbBilletsSpinner);
        panel.add(new JLabel("Date (AAAA-MM-JJ) :"));
        panel.add(dateField);
        panel.add(new JLabel(""));
        panel.add(reserverButton);

        add(panel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        reserverButton.addActionListener(e -> handleReservation());

        chargerAttractionsDisponibles();
        setVisible(true);
    }

    private void chargerAttractionsDisponibles() {
        List<Attraction> attractions = attractionDAO.getAllAttractions();
        for (Attraction a : attractions) {
            if (a.isDisponible()) {
                attractionBox.addItem(a);
            }
        }
    }

    private void handleReservation() {
        Attraction attraction = (Attraction) attractionBox.getSelectedItem();
        int nbBillets = (int) nbBilletsSpinner.getValue();
        String dateStr = dateField.getText().trim();

        if (attraction == null || dateStr.isEmpty()) {
            messageLabel.setText("Champs requis !");
            messageLabel.setForeground(Color.RED);
            return;
        }

        try {
            LocalDate date = LocalDate.parse(dateStr);
            boolean success = reservationController.reserverAttraction(idClient, attraction.getId(), date, nbBillets);
            if (success) {
                messageLabel.setText("✅ Réservation confirmée !");
                messageLabel.setForeground(Color.GREEN);
            } else {
                messageLabel.setText("❌ Réservation échouée.");
                messageLabel.setForeground(Color.RED);
            }
        } catch (Exception ex) {
            messageLabel.setText("❌ Date invalide (format AAAA-MM-JJ)");
            messageLabel.setForeground(Color.RED);
        }
    }
}