package view;

import controller.ConnexionController;
import model.Utilisateur;
import model.Utilisateur.TypeUtilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Représente la vue de connexion de l'application parc attractions
 * Cette fenêtre permet à l'utilisateur de saisir son email, son mot de passe,
 * de se connecter et de créer un compte ou de se connecter en tant qu'invité.
 *
 * Elle s'appuie sur un contrôleur {@link ConnexionController} pour vérifier
 * les identifiants saisis et rediriger vers les vues appropriées selon le type
 * d'utilisateur (admin, client, invité).
 */
public class ConnexionView extends JFrame {

    private final ConnexionController controller;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton inscriptionButton;
    private JButton inviteButton;
    private JLabel messageLabel;

    /**
     * Constructeur de la vue de connexion
     * Initialise le contrôleur et l'interface graphique
     */
    public ConnexionView() {
        this.controller = new ConnexionController(); //On instancie un nouveau controleur de connexion
        initUI();
    }

    /**
     * Initialise les composants graphiques de la fenêtre :
     * champs de texte, boutons, gestionnaires d'événements
     * Affiche ensuite la fenêtre
     */
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

    /**
     * Appelée lorsqu’un utilisateur clique sur un bouton "Connexion"
     * Elle va récupérer les champs saisis (email, mot de passe)
     * Vérifier qu’ils ne sont pas vides
     * Demander au contrôleur de valider les identifiants
     * Selon le résultat, rediriger vers le bon tableau de bord ou afficher un message d’érreur
     */
    private void handleLogin(ActionEvent e) { //Méthode privée, appelée par un listener
        String email = emailField.getText().trim(); //récupère le texte saisi dans emailField et trim supprime les espaces
        String password = new String(passwordField.getPassword()); // JPasswordField retourne un tableau de char[] converti en string.

        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Champs requis !", Color.RED);
            return;
        }

        Utilisateur utilisateur = controller.connecter(email, password); //appelle méthode de connexion du controleur

        if (utilisateur != null) { // si utilisateur trouvé, on le redirige vers son interface dédiée
            if (utilisateur.getType() == TypeUtilisateur.ADMIN) {
                new AdminDashboardView(utilisateur);
            } else {
                new ClientDashboardView(utilisateur);
            }
            dispose(); // ferme la fenêtre
        } else {
            showMessage("Email ou mot de passe invalide.", Color.RED);
        }
    }

    /**
     * Affiche un message en bas de la fenêtre avec une certaine couleur.
     * @param message Le message à afficher
     * @param color   La couleur du texte
     */
    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }
}