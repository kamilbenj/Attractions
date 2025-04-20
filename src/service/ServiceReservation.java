package service;

import dao.DAOGenerique;
import dao.ExceptionDAO;
import dao.FabriqueDAO;
import modele.Attraction;
import modele.Reservation;
import modele.Utilisateur;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Service pour la gestion des réservations
 */
public class ServiceReservation {
    private static ServiceReservation instance;
    private final DAOGenerique<Reservation, Long> daoReservation;
    private final ServiceAttraction serviceAttraction;
    private final ServiceUtilisateur serviceUtilisateur;

    private ServiceReservation() {
        this.daoReservation = FabriqueDAO.getInstance().creerDAOReservation();
        this.serviceAttraction = ServiceAttraction.getInstance();
        this.serviceUtilisateur = ServiceUtilisateur.getInstance();
    }

    /**
     * Obtient l'instance unique du service
     * @return L'instance du service
     */
    public static synchronized ServiceReservation getInstance() {
        if (instance == null) {
            instance = new ServiceReservation();
        }
        return instance;
    }

    /**
     * Obtenir toutes les réservations
     * @return Liste des réservations
     * @throws ServiceException Si une erreur survient
     */
    public List<Reservation> obtenirToutes() throws ServiceException {
        try {
            return daoReservation.trouverTous();
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la récupération des réservations", e);
        }
    }

    /**
     * Obtenir une réservation par son ID
     * @param id ID de la réservation
     * @return Réservation trouvée ou null si inexistante
     * @throws ServiceException Si une erreur survient
     */
    public Reservation obtenirParId(Long id) throws ServiceException {
        try {
            return daoReservation.trouver(id).orElse(null);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la récupération de la réservation avec l'ID " + id, e);
        }
    }

    /**
     * Obtenir les réservations d'un utilisateur
     * @param utilisateurId ID de l'utilisateur
     * @return Liste des réservations de l'utilisateur
     * @throws ServiceException Si une erreur survient
     */
    public List<Reservation> obtenirParUtilisateur(Long utilisateurId) throws ServiceException {
        if (utilisateurId == null) {
            throw new ServiceException("L'ID de l'utilisateur ne peut pas être null");
        }

        try {
            // Adapter en fonction de votre implémentation DAO réelle
            return daoReservation.trouverParAttribut("utilisateur.id", utilisateurId);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la récupération des réservations de l'utilisateur " + utilisateurId, e);
        }
    }

    /**
     * Obtenir les réservations pour une attraction
     * @param attractionId ID de l'attraction
     * @return Liste des réservations pour cette attraction
     * @throws ServiceException Si une erreur survient
     */
    public List<Reservation> obtenirParAttraction(Long attractionId) throws ServiceException {
        if (attractionId == null) {
            throw new ServiceException("L'ID de l'attraction ne peut pas être null");
        }

        try {
            // Adapter en fonction de votre implémentation DAO réelle
            return daoReservation.trouverParAttribut("attraction.id", attractionId);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la récupération des réservations pour l'attraction " + attractionId, e);
        }
    }

    /**
     * Créer une nouvelle réservation
     * @param utilisateurId ID de l'utilisateur qui réserve
     * @param attractionId ID de l'attraction réservée
     * @param dateReservation Date de la réservation
     * @param heureReservation Heure de la réservation
     * @param nombrePersonnes Nombre de personnes
     * @param commentaire Commentaire optionnel
     * @return Réservation créée
     * @throws ServiceException Si la création échoue
     */
    public Reservation reserver(Long utilisateurId, Long attractionId, Date dateReservation,
                                Date heureReservation, int nombrePersonnes, String commentaire)
            throws ServiceException {

        if (utilisateurId == null) {
            throw new ServiceException("L'ID de l'utilisateur ne peut pas être null");
        }

        if (attractionId == null) {
            throw new ServiceException("L'ID de l'attraction ne peut pas être null");
        }

        if (dateReservation == null) {
            throw new ServiceException("La date de réservation ne peut pas être null");
        }

        if (nombrePersonnes <= 0) {
            throw new ServiceException("Le nombre de personnes doit être supérieur à zéro");
        }

        try {
            Utilisateur utilisateur = serviceUtilisateur.obtenirParId(utilisateurId);
            if (utilisateur == null) {
                throw new ServiceException("Utilisateur non trouvé avec l'ID: " + utilisateurId);
            }

            Attraction attraction = serviceAttraction.obtenirParId(attractionId);
            if (attraction == null) {
                throw new ServiceException("Attraction non trouvée avec l'ID: " + attractionId);
            }

            if (!attraction.isEstDisponible()) {
                throw new ServiceException("L'attraction n'est pas disponible");
            }

            Reservation reservation = new Reservation();
            reservation.setUtilisateur(utilisateur);
            reservation.setAttraction(attraction);
            reservation.setDateReservation(dateReservation);
            reservation.setHeureReservation(heureReservation);
            reservation.setNombrePersonnes(nombrePersonnes);
            reservation.setCommentaire(commentaire);

            // Calculer le prix total
            BigDecimal prixAttraction = attraction.getPrix();
            BigDecimal prixTotal = prixAttraction.multiply(BigDecimal.valueOf(nombrePersonnes));
            reservation.setPrixTotal(prixTotal);

            // Définir le statut initial
            reservation.setStatut("En attente");

            // Dates de création et modification
            Date maintenant = new Date();
            reservation.setDateCreation(maintenant);
            reservation.setDateModification(maintenant);

            return daoReservation.creer(reservation);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la création de la réservation", e);
        }
    }

    /**
     * Mettre à jour le statut d'une réservation
     * @param id ID de la réservation
     * @param statut Nouveau statut
     * @return Réservation mise à jour
     * @throws ServiceException Si la mise à jour échoue
     */
    public Reservation mettreAJourStatut(Long id, String statut) throws ServiceException {
        if (id == null) {
            throw new ServiceException("L'ID de la réservation ne peut pas être null");
        }

        if (statut == null || statut.trim().isEmpty()) {
            throw new ServiceException("Le statut ne peut pas être vide");
        }

        try {
            Reservation reservation = daoReservation.trouver(id)
                    .orElseThrow(() -> new ServiceException("Réservation non trouvée avec l'ID: " + id));

            reservation.setStatut(statut);
            reservation.setDateModification(new Date());

            return daoReservation.mettreAJour(reservation);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la mise à jour du statut de la réservation", e);
        }
    }

    /**
     * Supprimer une réservation
     * @param id ID de la réservation à supprimer
     * @throws ServiceException Si la suppression échoue
     */
    public void annuler(Long id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("L'ID de la réservation ne peut pas être null");
        }

        try {
            boolean supprime = daoReservation.supprimer(id);
            if (!supprime) {
                throw new ServiceException("Impossible de supprimer la réservation avec l'ID " + id);
            }
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la suppression de la réservation", e);
        }
    }
}
