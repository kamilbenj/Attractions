package view;

import controller.ConnexionController;
import model.Utilisateur;
import model.Utilisateur.TypeUtilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConnexionView extends JFrame {

    private final ConnexionController controller;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton inscriptionButton;
    private JButton inviteButton;
    private JLabel messageLabel;

    public ConnexionView() {
        this.controller = new ConnexionController();
        initUI();
    }

    private void initUI() {
        setTitle("Connexion - Parc Attractions");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Se connecter");
        inscriptionButton = new JButton("Créer un compte");
        inviteButton = new JButton("Connexion Invité");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        panel.add(new JLabel("Email :"));
        panel.add(emailField);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(inscriptionButton);
        panel.add(inviteButton);

        add(panel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        loginButton.addActionListener(this::handleLogin);
        inscriptionButton.addActionListener(e -> {
            new InscriptionView();
            dispose();
        });
        inviteButton.addActionListener(e -> {
            Utilisateur invite = new Utilisateur(
                    0,
                    "Invité",
                    "invite@parc.com",
                    "",
                    TypeUtilisateur.INVITE,
                    30,
                    java.time.LocalDate.now()
            );
            new ClientDashboardView(invite);
            dispose();
        });

        setVisible(true);
    }

    private void handleLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Champs requis !", Color.RED);
            return;
        }

        Utilisateur utilisateur = controller.connecter(email, password);

        if (utilisateur != null) {
            if (utilisateur.getType() == TypeUtilisateur.ADMIN) {
                new AdminDashboardView(utilisateur);
            } else {
                new ClientDashboardView(utilisateur);
            }
            dispose();
        } else {
            showMessage("Email ou mot de passe invalide.", Color.RED);
        }
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }
}