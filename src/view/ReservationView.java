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

/**
 * Vue Swing permettant à un client de réserver une attraction
 * Affiche une interface avec : sélection d'attraction, date, heure, nombre de billets,
 * et confirmation via une fenêtre de paiement
 *
 * Cette classe interagit avec {@link ReservationController}, {@link ReservationDAO} et {@link AttractionDAO}.
 */
public class ReservationView extends JFrame {
    //On instancie le contrôleur et les DAO nécessaires pour accéder aux données
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

    /**
     * Crée la fenêtre de réservation pour un client donné
     *
     * @param idClient L'identifiant du client connecté
     */
    public ReservationView(int idClient) {
        this.idClient = idClient;
        this.reservationController = new ReservationController();
        this.reservationDAO = new ReservationDAO();
        this.attractionDAO = new AttractionDAO();
        initUI();
    } //Le constructeur prend l’id du client connecté en paramètre et initialise l’interface

    /**
     * Initialise les composants de l'interface utilisateur
     */
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
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter()); //création sélecteur de date

        heureBox = new JComboBox<>();
        updateHeureBox();
        //Liste déroulante d’heures disponibles pour une attraction, qui dépend de la date et de l’attraction choisies

        nbBilletsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        //permet de choisir entre 1 et 10 billets

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
        reserverButton.addActionListener(e -> handleReservation()); //pour lorsqu'on clique sur "Réserver"

        setVisible(true);
    }

    /**
     * Remplit la liste déroulante avec les attractions disponibles
     * Remplit la combo box avec les attractions disponibles uniquement
     */
    private void initAttractionBox() {
        List<Attraction> attractions = attractionDAO.getAllAttractions();
        for (Attraction a : attractions) {
            if (a.isDisponible()) {
                attractionBox.addItem(a);
            }
        }
    }

    /**
     * Met à jour la liste des heures disponibles en fonction de l'attraction et de la date choisies
     * Filtre les heures entre 10h00 et 18h00.
     * Marque les horaires déjà réservés comme indisponibles.
     */
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

    /**
     * Récupère la date sélectionnée dans le sélecteur
     * Extrait une date valide du datePicker.
     * @return La date choisie ou {@code null} si aucune date sélectionnée
     */
    private LocalDate getSelectedDate() {
        if (datePicker.getModel().getValue() != null) {
            java.util.Date selected = (java.util.Date) datePicker.getModel().getValue();
            return new java.sql.Date(selected.getTime()).toLocalDate();
        }
        return null;
    }

    /**
     * Gère la logique de réservation après clic sur le bouton
     * Vérifie les champs, l'heure, puis lance la fenêtre de paiement
     * Puis appelle le contrôleur pour valider la réservation
     * Affiche un résumé de réservation ou un message d’erreur
     */
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
            showMessage(" Sélectionnez une heure disponible.", Color.RED);
            return;
        }

        int heure = Integer.parseInt(heureSelection.substring(0, heureSelection.indexOf("h")));
        LocalTime heureFinale = LocalTime.of(heure, 0);

        // Appel PaymentView pour valider avant la réservation
        new PaymentView(() -> {
            boolean success = reservationController.reserverAttraction(idClient, attraction.getId(), date, heureFinale, nbBillets);
            if (success) {
                String resume = "Attraction réservée : " + attraction.getNom() +
                        ", le " + date +
                        " à " + heure + "h00";
                JOptionPane.showMessageDialog(this, resume, "Résumé de réservation", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                showMessage(" Erreur lors de la réservation.", Color.RED);
            }
        });
    }

    /**
     * Affiche un message utilisateur en bas de la fenêtre
     *
     * @param message Le message à afficher
     * @param color La couleur du texte
     */
    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }
}