package view;

import controller.ConnexionController;
import model.Utilisateur.TypeUtilisateur;

import javax.swing.*;
import java.awt.*;

public class InscriptionView extends JFrame {

    private final ConnexionController controller;

    private JTextField nomField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<TypeUtilisateur> typeBox;
    private JTextField ageField;
    private JButton inscriptionButton;
    private JLabel messageLabel;

    public InscriptionView() {
        this.controller = new ConnexionController();
        initUI();
    }

    private void initUI() {
        setTitle("Inscription - Parc Attractions");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        nomField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        ageField = new JTextField();
        typeBox = new JComboBox<>(new TypeUtilisateur[]{TypeUtilisateur.CLIENT, TypeUtilisateur.MEMBRE});
        inscriptionButton = new JButton("Créer mon compte");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        panel.add(new JLabel("Nom :"));
        panel.add(nomField);
        panel.add(new JLabel("Email :"));
        panel.add(emailField);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(passwordField);
        panel.add(new JLabel("Âge :"));
        panel.add(ageField);
        panel.add(new JLabel("Type de compte :"));
        panel.add(typeBox);
        panel.add(inscriptionButton);

        add(panel, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        inscriptionButton.addActionListener(e -> handleInscription());

        setVisible(true);
    }

    private void handleInscription() {
        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();
        String motDePasse = new String(passwordField.getPassword()).trim();
        String ageText = ageField.getText().trim();
        TypeUtilisateur type = (TypeUtilisateur) typeBox.getSelectedItem();

        if (nom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || ageText.isEmpty()) {
            messageLabel.setText("Tous les champs sont requis.");
            messageLabel.setForeground(Color.RED);
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            boolean success = controller.inscrire(nom, email, motDePasse, type, age);
            if (success) {
                JOptionPane.showMessageDialog(this, "Compte créé avec succès !");
                new ConnexionView();  // retour à la connexion
                dispose();
            } else {
                messageLabel.setText("Email déjà utilisé.");
                messageLabel.setForeground(Color.RED);
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Âge invalide.");
            messageLabel.setForeground(Color.RED);
        }
    }
}