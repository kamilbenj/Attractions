package view;

import model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class ClientDashboardView extends JFrame {

    private final Utilisateur client;

    public ClientDashboardView(Utilisateur client) {
        this.client = client;
        initUI();
    }

    private void initUI() {
        setTitle("Espace Client - Parc Attractions");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel welcomeLabel = new JLabel("Bienvenue, " + client.getNom(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton reserverButton = new JButton("Réserver une attraction");
        JButton historiqueButton = new JButton("Mes réservations");
        JButton facturesButton = new JButton("Mes factures");
        JButton deconnexionButton = new JButton("Se déconnecter");

        panel.add(welcomeLabel);
        panel.add(reserverButton);
        panel.add(historiqueButton);
        panel.add(facturesButton);
        panel.add(deconnexionButton);

        add(panel);

        // Action : déconnexion
        deconnexionButton.addActionListener(e -> {
            new ConnexionView();
            dispose();
        });

        // TODO : brancher les autres boutons vers des vues dédiées
        reserverButton.addActionListener(e -> new ReservationView(client.getId()));
        historiqueButton.addActionListener(e -> new HistoriqueReservationsView(client.getId()));
        facturesButton.addActionListener(e -> new FacturesView(client.getId()));

        setVisible(true);
    }
}