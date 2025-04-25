package view;

import model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardView extends JFrame {

    private final Utilisateur admin;

    public AdminDashboardView(Utilisateur admin) {
        this.admin = admin;
        initUI();
    }

    private void initUI() {
        setTitle("Espace Administrateur - Parc Attractions");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel welcomeLabel = new JLabel("Bienvenue, " + admin.getNom() + " (Admin)", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton gestionAttractionsButton = new JButton("Gérer les attractions");
        JButton gestionReductionsButton = new JButton("Gérer les réductions");
        JButton voirStatistiquesButton = new JButton("Voir les statistiques");
        JButton voirReservationsButton = new JButton("Toutes les réservations");
        JButton deconnexionButton = new JButton("Se déconnecter");

        panel.add(welcomeLabel);
        panel.add(gestionAttractionsButton);
        panel.add(gestionReductionsButton);
        panel.add(voirStatistiquesButton);
        panel.add(voirReservationsButton);
        panel.add(deconnexionButton);

        add(panel);

        // Actions
        deconnexionButton.addActionListener(e -> {
            new ConnexionView();
            dispose();
        });

        gestionAttractionsButton.addActionListener(e -> new AttractionManagerView());
        gestionReductionsButton.addActionListener(e -> new ReductionManagerView());
        voirStatistiquesButton.addActionListener(e -> new ReportingView());
        voirReservationsButton.addActionListener(e -> new AllReservationsView());

        setVisible(true);
    }
}