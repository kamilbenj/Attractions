package controleur;

import modele.Attraction;
import modele.Reservation;
import modele.Utilisateur;
import service.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Contrôleur principal de l'application
 */
public class Controleur {
    private final ServiceUtilisateur serviceUtilisateur;
    private final ServiceAttraction serviceAttraction;
    private final ServiceReservation serviceReservation;
    private Utilisateur utilisateurConnecte;

    /**
     * Constructeur du contrôleur
     */
    public Controleur() {
        this.serviceUtilisateur = ServiceUtilisateur.getInstance();
        this.serviceAttraction = ServiceAttraction.getInstance();
        this.serviceReservation = ServiceReservation.getInstance();
    }

    /**
     * Connecte un utilisateur
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe de l'utilisateur
     * @throws ControleurException En cas d'échec de connexion
     */
    public void connecter(String email, String motDePasse) throws ControleurException {
        try {
            utilisateurConnecte = serviceUtilisateur.connecter(email, motDePasse);
        } catch (ServiceException e) {
            throw new ControleurException("Échec de connexion", e);
        }
    }

    /**
     * Déconnecte l'utilisateur courant
     */
    public void deconnecter() {
        utilisateurConnecte = null;
    }

    /**
     * Vérifie si un utilisateur est connecté
     * @return true si un utilisateur est connecté, false sinon
     */
    public boolean estConnecte() {
        return utilisateurConnecte != null;
    }

    /**
     * Vérifie si l'utilisateur connecté est un administrateur
     * @return true si administrateur, false sinon
     * @throws ControleurException Si aucun utilisateur n'est connecté
     */
    public boolean estAdmin() throws ControleurException {
        if (!estConnecte()) {
            throw new ControleurException("Aucun utilisateur connecté");
        }
        return utilisateurConnecte.isAdmin();
    }

    /**
     * Obtient l'utilisateur connecté
     * @return Utilisateur connecté
     * @throws ControleurException Si aucun utilisateur n'est connecté
     */
    public Utilisateur getUtilisateurConnecte() throws ControleurException {
        if (!estConnecte()) {
            throw new ControleurException("Aucun utilisateur connecté");
        }
        return utilisateurConnecte;
    }

    /**
     * Inscrit un nouvel utilisateur
     * @param nom Nom
     * @param prenom Prénom
     * @param email Email (unique)
     * @param motDePasse Mot de passe
     * @param telephone Téléphone
     * @return L'utilisateur créé
     * @throws ControleurException En cas d'échec d'inscription
     */
    public Utilisateur inscrire(String nom, String prenom, String email,
                                String motDePasse, String telephone)
            throws ControleurException {
        try {
            return serviceUtilisateur.creer(nom, prenom, email, motDePasse, telephone, false);
        } catch (ServiceException e) {
            throw new ControleurException("Échec d'inscription", e);
        }
    }

    /**
     * Obtient la liste de toutes les attractions
     * @return Liste d'attractions
     * @throws ControleurException En cas d'erreur
     */
    public List<Attraction> obtenirToutesLesAttractions() throws ControleurException {
        try {
            return serviceAttraction.obtenirToutes();
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de la récupération des attractions", e);
        }
    }

    /**
     * Obtient une attraction par son ID
     * @param id ID de l'attraction
     * @return L'attraction correspondante
     * @throws ControleurException Si l'attraction n'existe pas
     */
    public Attraction obtenirAttractionParId(Long id) throws ControleurException {
        try {
            Attraction attraction = serviceAttraction.obtenirParId(id);
            if (attraction == null) {
                throw new ControleurException("Attraction non trouvée avec l'ID: " + id);
            }
            return attraction;
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de la récupération de l'attraction", e);
        }
    }

    /**
     * Crée une nouvelle attraction (admin seulement)
     * @param nom Nom
     * @param description Description
     * @param capacite Capacité
     * @param duree Durée en minutes
     * @param prix Prix
     * @param ageMinimum Âge minimum requis
     * @param tailleMinimum Taille minimum requise en cm
     * @param estDisponible Disponibilité
     * @param imageUrl URL de l'image
     * @return L'attraction créée
     * @throws ControleurException En cas d'erreur
     */
    public Attraction creerAttraction(String nom, String description, int capacite,
                                      int duree, BigDecimal prix, int ageMinimum,
                                      int tailleMinimum, boolean estDisponible, String imageUrl)
            throws ControleurException {
        try {
            if (!estAdmin()) {
                throw new ControleurException("Seuls les administrateurs peuvent créer des attractions");
            }
            return serviceAttraction.creer(nom, description, capacite, duree, prix,
                    ageMinimum, tailleMinimum, estDisponible, imageUrl);
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de la création de l'attraction", e);
        }
    }

    /**
     * Met à jour une attraction existante (admin seulement)
     * @param id ID de l'attraction
     * @param nom Nouveau nom
     * @param description Nouvelle description
     * @param capacite Nouvelle capacité
     * @param duree Nouvelle durée
     * @param prix Nouveau prix
     * @param ageMinimum Nouvel âge minimum
     * @param tailleMinimum Nouvelle taille minimum
     * @param estDisponible Nouvelle disponibilité
     * @param imageUrl Nouvel URL d'image
     * @return L'attraction mise à jour
     * @throws ControleurException En cas d'erreur
     */
    public Attraction mettreAJourAttraction(Long id, String nom, String description, int capacite,
                                            int duree, BigDecimal prix, int ageMinimum,
                                            int tailleMinimum, boolean estDisponible, String imageUrl)
            throws ControleurException {
        try {
            if (!estAdmin()) {
                throw new ControleurException("Seuls les administrateurs peuvent modifier des attractions");
            }
            return serviceAttraction.mettreAJour(id, nom, description, capacite, duree, prix,
                    ageMinimum, tailleMinimum, estDisponible, imageUrl);
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de la mise à jour de l'attraction", e);
        }
    }

    /**
     * Supprime une attraction (admin seulement)
     * @param id ID de l'attraction à supprimer
     * @throws ControleurException En cas d'erreur
     */
    public void supprimerAttraction(Long id) throws ControleurException {
        try {
            if (!estAdmin()) {
                throw new ControleurException("Seuls les administrateurs peuvent supprimer des attractions");
            }
            serviceAttraction.supprimer(id);
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de la suppression de l'attraction", e);
        }
    }

    /**
     * Crée une réservation pour l'utilisateur connecté
     * @param attractionId ID de l'attraction
     * @param dateReservation Date de réservation
     * @param heureReservation Heure de réservation
     * @param nombrePersonnes Nombre de personnes
     * @param commentaire Commentaire optionnel
     * @return La réservation créée
     * @throws ControleurException En cas d'erreur
     */
    public Reservation creerReservation(Long attractionId, Date dateReservation,
                                        Date heureReservation, int nombrePersonnes, String commentaire)
            throws ControleurException {
        try {
            if (!estConnecte()) {
                throw new ControleurException("Vous devez être connecté pour faire une réservation");
            }
            return serviceReservation.reserver(utilisateurConnecte.getId(), attractionId,
                    dateReservation, heureReservation, nombrePersonnes, commentaire);
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de la création de la réservation", e);
        }
    }

    /**
     * Obtient toutes les réservations (admin seulement)
     * @return Liste des réservations
     * @throws ControleurException En cas d'erreur
     */
    public List<Reservation> obtenirToutesLesReservations() throws ControleurException {
        try {
            if (!estAdmin()) {
                throw new ControleurException("Seuls les administrateurs peuvent voir toutes les réservations");
            }
            return serviceReservation.obtenirToutes();
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de la récupération des réservations", e);
        }
    }

    /**
     * Obtient les réservations de l'utilisateur connecté
     * @return Liste des réservations de l'utilisateur
     * @throws ControleurException En cas d'erreur
     */
    public List<Reservation> obtenirMesReservations() throws ControleurException {
        try {
            if (!estConnecte()) {
                throw new ControleurException("Vous devez être connecté pour voir vos réservations");
            }
            return serviceReservation.obtenirParUtilisateur(utilisateurConnecte.getId());
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de la récupération des réservations", e);
        }
    }

    /**
     * Obtient les réservations pour une attraction (admin seulement)
     * @param attractionId ID de l'attraction
     * @return Liste des réservations pour cette attraction
     * @throws ControleurException En cas d'erreur
     */
    public List<Reservation> obtenirReservationsParAttraction(Long attractionId) throws ControleurException {
        try {
            if (!estAdmin()) {
                throw new ControleurException("Seuls les administrateurs peuvent voir les réservations par attraction");
            }
            return serviceReservation.obtenirParAttraction(attractionId);
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de la récupération des réservations", e);
        }
    }

    /**
     * Met à jour le statut d'une réservation (admin seulement)
     * @param id ID de la réservation
     * @param statut Nouveau statut
     * @return La réservation mise à jour
     * @throws ControleurException En cas d'erreur
     */
    public Reservation mettreAJourStatutReservation(Long id, String statut) throws ControleurException {
        try {
            if (!estAdmin()) {
                throw new ControleurException("Seuls les administrateurs peuvent modifier le statut des réservations");
            }
            return serviceReservation.mettreAJourStatut(id, statut);
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de la mise à jour du statut de la réservation", e);
        }
    }

    /**
     * Annule une réservation
     * @param id ID de la réservation à annuler
     * @throws ControleurException En cas d'erreur
     */
    public void annulerReservation(Long id) throws ControleurException {
        try {
            if (!estConnecte()) {
                throw new ControleurException("Vous devez être connecté pour annuler une réservation");
            }

            Reservation reservation = serviceReservation.obtenirParId(id);
            if (reservation == null) {
                throw new ControleurException("Réservation non trouvée avec l'ID: " + id);
            }

            // Vérifier si c'est la réservation de l'utilisateur ou si c'est un admin
            if (!utilisateurConnecte.isAdmin() &&
                    !reservation.getUtilisateur().getId().equals(utilisateurConnecte.getId())) {
                throw new ControleurException("Vous ne pouvez annuler que vos propres réservations");
            }

            serviceReservation.annuler(id);
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de l'annulation de la réservation", e);
        }
    }

    /**
     * Obtient la liste de tous les utilisateurs (admin seulement)
     * @return Liste des utilisateurs
     * @throws ControleurException En cas d'erreur
     */
    public List<Utilisateur> obtenirTousLesUtilisateurs() throws ControleurException {
        try {
            if (!estAdmin()) {
                throw new ControleurException("Seuls les administrateurs peuvent voir la liste des utilisateurs");
            }
            return serviceUtilisateur.obtenirTous();
        } catch (ServiceException e) {
            throw new ControleurException("Erreur lors de la récupération des utilisateurs", e);
        }
    }
}
