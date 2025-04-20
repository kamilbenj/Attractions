package vue;

import controleur.Controleur;
import modele.Attraction;
import modele.Reservation;
import modele.Utilisateur;
import service.ServiceException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Interface console pour l'application
 */
public class InterfaceConsole {
    private Controleur controleur;
    private Scanner scanner;
    private Utilisateur utilisateurConnecte;

    /**
     * Constructeur
     * @param controleur Controleur de l'application
     */
    public InterfaceConsole(Controleur controleur) {
        this.controleur = controleur;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Démarre l'interface console
     */
    public void demarrer() {
        boolean continuer = true;
        afficherBienvenue();

        while (continuer) {
            if (utilisateurConnecte == null) {
                afficherMenuPrincipal();
            } else {
                afficherMenuUtilisateur();
            }

            int choix = lireEntier("Votre choix: ");

            try {
                if (utilisateurConnecte == null) {
                    traiterMenuPrincipal(choix);
                } else {
                    traiterMenuUtilisateur(choix);
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    /**
     * Affiche un message de bienvenue
     */
    private void afficherBienvenue() {
        System.out.println("*********************************************");
        System.out.println("*       SYSTÈME DE GESTION D'ATTRACTIONS    *");
        System.out.println("*********************************************");
    }

    /**
     * Affiche le menu principal pour les utilisateurs non connectés
     */
    private void afficherMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Se connecter");
        System.out.println("2. S'inscrire");
        System.out.println("3. Voir les attractions");
        System.out.println("0. Quitter");
    }

    /**
     * Affiche le menu pour les utilisateurs connectés
     */
    private void afficherMenuUtilisateur() {
        System.out.println("\n--- MENU UTILISATEUR ---");
        System.out.println("Connecté en tant que: " + utilisateurConnecte.getPrenom() + " " + utilisateurConnecte.getNom());
        System.out.println("1. Voir les attractions");
        System.out.println("2. Réserver une attraction");
        System.out.println("3. Voir mes réservations");

        if (utilisateurConnecte.isAdmin()) {
            System.out.println("\n--- ADMINISTRATION ---");
            System.out.println("4. Gérer les attractions");
            System.out.println("5. Gérer les utilisateurs");
            System.out.println("6. Voir toutes les réservations");
        }

        System.out.println("0. Se déconnecter");
    }

    /**
     * Traite les choix du menu principal
     * @param choix Choix de l'utilisateur
     */
    private void traiterMenuPrincipal(int choix) throws Exception {
        switch (choix) {
            case 1:
                seConnecter();
                break;
            case 2:
                inscrireUtilisateur();
                break;
            case 3:
                afficherAttractions();
                break;
            case 0:
                System.out.println("Au revoir !");
                System.exit(0);
                break;
            default:
                System.out.println("Choix invalide !");
                break;
        }
    }

    /**
     * Traite les choix du menu utilisateur
     * @param choix Choix de l'utilisateur
     */
    private void traiterMenuUtilisateur(int choix) throws Exception {
        switch (choix) {
            case 1:
                afficherAttractions();
                break;
            case 2:
                reserverAttraction();
                break;
            case 3:
                afficherReservationsUtilisateur();
                break;
            case 4:
                if (utilisateurConnecte.isAdmin()) {
                    gererAttractions();
                } else {
                    System.out.println("Option non disponible");
                }
                break;
            case 5:
                if (utilisateurConnecte.isAdmin()) {
                    gererUtilisateurs();
                } else {
                    System.out.println("Option non disponible");
                }
                break;
            case 6:
                if (utilisateurConnecte.isAdmin()) {
                    afficherToutesReservations();
                } else {
                    System.out.println("Option non disponible");
                }
                break;
            case 0:
                utilisateurConnecte = null;
                System.out.println("Vous êtes déconnecté");
                break;
            default:
                System.out.println("Choix invalide !");
                break;
        }
    }

    /**
     * Connecte un utilisateur
     */
    private void seConnecter() throws ServiceException {
        System.out.println("\n--- CONNEXION ---");
        String email = lireChaine("Email: ");
        String motDePasse = lireChaine("Mot de passe: ");

        utilisateurConnecte = controleur.connecterUtilisateur(email, motDePasse);
        System.out.println("Connexion réussie! Bienvenue " + utilisateurConnecte.getPrenom() + "!");
    }

    /**
     * Inscrit un nouvel utilisateur
     */
    private void inscrireUtilisateur() throws ServiceException {
        System.out.println("\n--- INSCRIPTION ---");
        String nom = lireChaine("Nom: ");
        String prenom = lireChaine("Prénom: ");
        String email = lireChaine("Email: ");
        String motDePasse = lireChaine("Mot de passe: ");
        String telephone = lireChaine("Téléphone: ");

        // Inscription avec droits utilisateur standard (non admin)
        utilisateurConnecte = controleur.creerUtilisateur(nom, prenom, email, motDePasse, telephone, false);
        System.out.println("Inscription réussie! Bienvenue " + utilisateurConnecte.getPrenom() + "!");
    }

    /**
     * Affiche toutes les attractions
     */
    private void afficherAttractions() throws ServiceException {
        System.out.println("\n--- LISTE DES ATTRACTIONS ---");
        List<Attraction> attractions = controleur.obtenirToutesAttractions();

        if (attractions.isEmpty()) {
            System.out.println("Aucune attraction disponible.");
            return;
        }

        for (Attraction attraction : attractions) {
            afficherAttraction(attraction);
        }
    }

    /**
     * Affiche les détails d'une attraction
     */
    private void afficherAttraction(Attraction attraction) {
        System.out.println("\nID: " + attraction.getId());
        System.out.println("Nom: " + attraction.getNom());
        System.out.println("Description: " + attraction.getDescription());
        System.out.println("Durée: " + attraction.getDuree() + " minutes");
        System.out.println("Capacité: " + attraction.getCapacite() + " personnes");
        System.out.println("Prix: " + attraction.getPrix() + " €");
        System.out.println("Disponible: " + (attraction.isEstDisponible() ? "Oui" : "Non"));
        System.out.println("-----------------------------------");
    }

    /**
     * Réserve une attraction
     */
    private void reserverAttraction() throws ServiceException {
        System.out.println("\n--- RÉSERVER UNE ATTRACTION ---");
        afficherAttractions();

        Long attractionId = lireEntierLong("ID de l'attraction à réserver: ");
        Attraction attraction = controleur.obtenirAttractionParId(attractionId);

        if (attraction == null) {
            System.out.println("Attraction non trouvée!");
            return;
        }

        Date dateReservation = lireDate("Date de réservation (jj/mm/aaaa): ");
        Date heureReservation = lireHeure("Heure de réservation (HH:mm): ");
        int nombrePersonnes = lireEntier("Nombre de personnes: ");
        String commentaire = lireChaine("Commentaire (facultatif): ");

        // Calcul du prix total
        BigDecimal prixTotal = attraction.getPrix().multiply(new BigDecimal(nombrePersonnes));

        // Création de la réservation
        Reservation reservation = controleur.creerReservation(
                utilisateurConnecte,
                attraction,
                dateReservation,
                heureReservation,
                nombrePersonnes,
                prixTotal,
                "Confirmé",
                commentaire
        );

        System.out.println("Réservation confirmée! Numéro de réservation: " + reservation.getId());
    }

    /**
     * Affiche les réservations de l'utilisateur connecté
     */
    private void afficherReservationsUtilisateur() throws ServiceException {
        System.out.println("\n--- MES RÉSERVATIONS ---");
        List<Reservation> reservations = controleur.obtenirReservationsUtilisateur(utilisateurConnecte.getId());

        if (reservations.isEmpty()) {
            System.out.println("Vous n'avez aucune réservation.");
            return;
        }

        for (Reservation reservation : reservations) {
            afficherReservation(reservation);
        }
    }

    /**
     * Affiche toutes les réservations (admin)
     */
    private void afficherToutesReservations() throws ServiceException {
        System.out.println("\n--- TOUTES LES RÉSERVATIONS ---");
        List<Reservation> reservations = controleur.obtenirToutesReservations();

        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation dans le système.");
            return;
        }

        for (Reservation reservation : reservations) {
            afficherReservation(reservation);
        }
    }

    /**
     * Affiche les détails d'une réservation
     */
    private void afficherReservation(Reservation reservation) {
        System.out.println("\nID: " + reservation.getId());
        System.out.println("Attraction: " + reservation.getAttraction().getNom());
        System.out.println("Date: " + formatDate(reservation.getDateReservation()));
        System.out.println("Heure: " + formatHeure(reservation.getHeureReservation()));
        System.out.println("Nombre de personnes: " + reservation.getNombrePersonnes());
        System.out.println("Prix total: " + reservation.getPrixTotal() + " €");
        System.out.println("Statut: " + reservation.getStatut());
        System.out.println("-----------------------------------");
    }

    /**
     * Gestion des attractions (admin)
     */
    private void gererAttractions() throws ServiceException {
        System.out.println("\n--- GESTION DES ATTRACTIONS ---");
        System.out.println("1. Ajouter une attraction");
        System.out.println("2. Modifier une attraction");
        System.out.println("3. Supprimer une attraction");
        System.out.println("0. Retour");

        int choix = lireEntier("Votre choix: ");

        switch (choix) {
            case 1:
                ajouterAttraction();
                break;
            case 2:
                modifierAttraction();
                break;
            case 3:
                supprimerAttraction();
                break;
            case 0:
                return;
            default:
                System.out.println("Choix invalide!");
                break;
        }
    }

    /**
     * Ajoute une nouvelle attraction
     */
    private void ajouterAttraction() throws ServiceException {
        System.out.println("\n--- AJOUTER UNE ATTRACTION ---");

        String nom = lireChaine("Nom: ");
        String description = lireChaine("Description: ");
        int duree = lireEntier("Durée (minutes): ");
        int capacite = lireEntier("Capacité: ");
        BigDecimal prix = lireMontant("Prix: ");
        int ageMini = lireEntier("Âge minimum requis: ");
        int tailleMini = lireEntier("Taille minimum requise (cm): ");
        boolean disponible = lireBoolean("Disponible (oui/non): ");

        Attraction attraction = controleur.creerAttraction(
                nom, description, capacite, duree, prix, ageMini, tailleMini, disponible
        );

        System.out.println("Attraction ajoutée avec succès! ID: " + attraction.getId());
    }

    /**
     * Modifie une attraction existante
     */
    private void modifierAttraction() throws ServiceException {
        System.out.println("\n--- MODIFIER UNE ATTRACTION ---");
        afficherAttractions();

        Long attractionId = lireEntierLong("ID de l'attraction à modifier: ");
        Attraction attraction = controleur.obtenirAttractionParId(attractionId);

        if (attraction == null) {
            System.out.println("Attraction non trouvée!");
            return;
        }

        System.out.println("Laissez vide pour conserver les valeurs actuelles");

        String nom = lireChaine("Nom (" + attraction.getNom() + "): ");
        if (!nom.isEmpty()) attraction.setNom(nom);

        String description = lireChaine("Description (" + attraction.getDescription() + "): ");
        if (!description.isEmpty()) attraction.setDescription(description);

        String dureeStr = lireChaine("Durée (" + attraction.getDuree() + "): ");
        if (!dureeStr.isEmpty()) attraction.setDuree(Integer.parseInt(dureeStr));

        String capaciteStr = lireChaine("Capacité (" + attraction.getCapacite() + "): ");
        if (!capaciteStr.isEmpty()) attraction.setCapacite(Integer.parseInt(capaciteStr));

        String prixStr = lireChaine("Prix (" + attraction.getPrix() + "): ");
        if (!prixStr.isEmpty()) attraction.setPrix(new BigDecimal(prixStr));

        String ageMiniStr = lireChaine("Âge minimum (" + attraction.getAgeMiniRequis() + "): ");
        if (!ageMiniStr.isEmpty()) attraction.setAgeMiniRequis(Integer.parseInt(ageMiniStr));

        String tailleMiniStr = lireChaine("Taille minimum (" + attraction.getTailleMinRequise() + "): ");
        if (!tailleMiniStr.isEmpty()) attraction.setTailleMinRequise(Integer.parseInt(tailleMiniStr));

        String disponibleStr = lireChaine("Disponible (" + (attraction.isEstDisponible() ? "oui" : "non") + "): ");
        if (!disponibleStr.isEmpty()) {
            attraction.setEstDisponible(disponibleStr.equalsIgnoreCase("oui"));
        }

        attraction = controleur.mettreAJourAttraction(attraction);
        System.out.println("Attraction modifiée avec succès!");
    }

    /**
     * Supprime une attraction
     */
    private void supprimerAttraction() throws ServiceException {
        System.out.println("\n--- SUPPRIMER UNE ATTRACTION ---");
        afficherAttractions();

        Long attractionId = lireEntierLong("ID de l'attraction à supprimer: ");

        boolean confirme = lireBoolean("Êtes-vous sûr de vouloir supprimer cette attraction? (oui/non): ");
        if (!confirme) {
            System.out.println("Suppression annulée.");
            return;
        }

        boolean resultat = controleur.supprimerAttraction(attractionId);
        if (resultat) {
            System.out.println("Attraction supprimée avec succès!");
        } else {
            System.out.println("Échec de la suppression.");
        }
    }

    /**
     * Gestion des utilisateurs (admin)
     */
    private void gererUtilisateurs() throws ServiceException {
        System.out.println("\n--- GESTION DES UTILISATEURS ---");
        System.out.println("1. Voir tous les utilisateurs");
        System.out.println("2. Ajouter un administrateur");
        System.out.println("0. Retour");

        int choix = lireEntier("Votre choix: ");

        switch (choix) {
            case 1:
                afficherUtilisateurs();
                break;
            case 2:
                ajouterAdministrateur();
                break;
            case 0:
                return;
            default:
                System.out.println("Choix invalide!");
                break;
        }
    }

    /**
     * Affiche tous les utilisateurs
     */
    private void afficherUtilisateurs() throws ServiceException {
        System.out.println("\n--- LISTE DES UTILISATEURS ---");
        List<Utilisateur> utilisateurs = controleur.obtenirTousUtilisateurs();

        if (utilisateurs.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé.");
            return;
        }

        for (Utilisateur utilisateur : utilisateurs) {
            System.out.println("\nID: " + utilisateur.getId());
            System.out.println("Nom: " + utilisateur.getNom());
            System.out.println("Prénom: " + utilisateur.getPrenom());
            System.out.println("Email: " + utilisateur.getEmail());
            System.out.println("Téléphone: " + utilisateur.getTelephone());
            System.out.println("Admin: " + (utilisateur.isAdmin() ? "Oui" : "Non"));
            System.out.println("-----------------------------------");
        }
    }

    /**
     * Ajoute un administrateur
     */
    private void ajouterAdministrateur() throws ServiceException {
        System.out.println("\n--- AJOUTER UN ADMINISTRATEUR ---");
        String nom = lireChaine("Nom: ");
        String prenom = lireChaine("Prénom: ");
        String email = lireChaine("Email: ");
        String motDePasse = lireChaine("Mot de passe: ");
        String telephone = lireChaine("Téléphone: ");

        Utilisateur admin = controleur.creerUtilisateur(nom, prenom, email, motDePasse, telephone, true);
        System.out.println("Administrateur ajouté avec succès! ID: " + admin.getId());
    }

    // Méthodes utilitaires pour la lecture des entrées

    private String lireChaine(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    private int lireEntier(String message) {
        while (true) {
            try {
                System.out.print(message);
                int valeur = Integer.parseInt(scanner.nextLine());
                return valeur;
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre entier valide.");
            }
        }
    }

    private Long lireEntierLong(String message) {
        while (true) {
            try {
                System.out.print(message);
                Long valeur = Long.parseLong(scanner.nextLine());
                return valeur;
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre entier valide.");
            }
        }
    }

    private BigDecimal lireMontant(String message) {
        while (true) {
            try {
                System.out.print(message);
                BigDecimal valeur = new BigDecimal(scanner.nextLine());
                return valeur;
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un montant valide.");
            }
        }
    }

    private boolean lireBoolean(String message) {
        while (true) {
            System.out.print(message);
            String reponse = scanner.nextLine().toLowerCase();
            if (reponse.equals("oui") || reponse.equals("o")) {
                return true;
            } else if (reponse.equals("non") || reponse.equals("n")) {
                return false;
            } else {
                System.out.println("Veuillez répondre par 'oui' ou 'non'.");
            }
        }
    }

    private Date lireDate(String message) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);

        while (true) {
            try {
                System.out.print(message);
                String dateStr = scanner.nextLine();
                return format.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Format de date invalide. Utilisez le format jj/mm/aaaa.");
            }
        }
    }

    private Date lireHeure(String message) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setLenient(false);

        while (true) {
            try {
                System.out.print(message);
                String heureStr = scanner.nextLine();
                return format.parse(heureStr);
            } catch (ParseException e) {
                System.out.println("Format d'heure invalide. Utilisez le format HH:mm.");
            }
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    private String formatHeure(Date heure) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(heure);
    }
}

//Partie a supprimer, cétait pour les commits
import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class EtudiantDAOImpl implements EtudiantDAO {
    private Connection connection;

    // Constructeur avec connexion à la base de données
    public EtudiantDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void ajouter(Etudiant etudiant) {
        String sql = "INSERT INTO etudiants (nom, age) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, etudiant.getNom());
            stmt.setInt(2, etudiant.getAge());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Etudiant trouverParId(int id) {
        String sql = "SELECT * FROM etudiants WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Etudiant(rs.getInt("id"), rs.getString("nom"), rs.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Etudiant> listerTous() {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM etudiants";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                etudiants.add(new Etudiant(rs.getInt("id"), rs.getString("nom"), rs.getInt("age")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return etudiants;
    }

    @Override
    public void mettreAJour(Etudiant etudiant) {
        String sql = "UPDATE etudiants SET nom = ?, age = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, etudiant.getNom());
            stmt.setInt(2, etudiant.getAge());
            stmt.setInt(3, etudiant.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM etudiants WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

