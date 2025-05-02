package controller;

import dao.FactureDAO;
import dao.ReservationDAO;
import dao.AttractionDAO;
import dao.UtilisateurDAO;
import model.Attraction;
import model.Facture;
import model.Reservation;
import model.Utilisateur;
import model.Reduction;
import dao.ReductionDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Contrôleur chargé de la gestion métier des réservations
 * Permet de créer des réservations avec ou sans réduction, d'afficher l'historique d'un utilisateur,
 * de lister toutes les réservations et de supprimer une réservation
 *
 * Fait le lien entre les vues (interfaces utilisateur) et les DAO de données
 *
 * @see dao.ReservationDAO
 * @see model.Reservation
 */
public class ReservationController {

    private final ReservationDAO reservationDAO;
    private final FactureDAO factureDAO;
    private final AttractionDAO attractionDAO;
    private final UtilisateurDAO utilisateurDAO;
    private final ReductionDAO reductionDAO;

    /**
     * Initialise les DAO utilisés par le contrôleur
     */
    public ReservationController() {
        this.reservationDAO = new ReservationDAO();
        this.factureDAO = new FactureDAO();
        this.attractionDAO = new AttractionDAO();
        this.utilisateurDAO = new UtilisateurDAO();
        this.reductionDAO = new ReductionDAO();
    }

    /**
     * Crée une réservation et une facture associée
     * Si l'utilisateur est éligible, applique une réduction (enfant, senior, fidélité)
     *
     * @param idUtilisateur ID de l'utilisateur (0 pour un invité)
     * @param idAttraction ID de l'attraction à réserver
     * @param date Date de la réservation (doit être aujourd'hui ou plus tard)
     * @param heure Heure prévue (peut être {@code null})
     * @param nbBillets Nombre de billets réservés (> 0)
     * @return {@code true} si la réservation et la facture ont été créées avec succès, sinon {@code false}
     */
    //Crée une nouvelle réservation avec date et heure.
    public boolean reserverAttraction(int idUtilisateur, int idAttraction, LocalDate date, LocalTime heure, int nbBillets) {
        if (nbBillets <= 0 || date.isBefore(LocalDate.now())) {
            return false;
        }

        Reservation reservation = new Reservation(
                idUtilisateur,
                idAttraction,
                date,
                heure,
                nbBillets,
                Reservation.StatutReservation.CONFIRMEE
        );

        int reservationId = reservationDAO.insertReservation(reservation);

        if (reservationId > 0) {
            reservation.setId(reservationId);

            Attraction attraction = attractionDAO.getAttractionById(idAttraction);
            Utilisateur utilisateur = utilisateurDAO.getUtilisateurById(idUtilisateur);

            double prixUnitaire = attraction.getPrix();
            double montantTotal = prixUnitaire * nbBillets;
            boolean reductionAppliquee = false;

            // Nouvelle vérification ici
            if (utilisateur != null && utilisateur.getType() != Utilisateur.TypeUtilisateur.INVITE) {
                int age = utilisateur.getAge();
                List<Reduction> reductions = reductionDAO.getAllReductions();

                for (Reduction r : reductions) {
                    boolean applicable =
                            (r.getCritere() == Reduction.CritereReduction.ENFANT && age < 12) ||
                                    (r.getCritere() == Reduction.CritereReduction.SENIOR && age > 60) ||
                                    (r.getCritere() == Reduction.CritereReduction.FIDELITE && utilisateur.getType() == Utilisateur.TypeUtilisateur.MEMBRE);

                    if (applicable) {
                        montantTotal *= (1 - (r.getPourcentage() / 100.0));
                        reductionAppliquee = true;
                        break;
                    }
                }
            }

            Facture facture = new Facture(
                    reservationId,
                    montantTotal,
                    LocalDate.now(),
                    reductionAppliquee
            );

            factureDAO.insertFacture(facture);

            return true;
        }

        return false;
    }

    /**
     * Retourne toutes les réservations passées par un utilisateur
     *
     * @param idUtilisateur L'identifiant de l'utilisateur
     * @return Liste des réservations effectuées par cet utilisateur
     */
    public List<Reservation> getHistoriqueUtilisateur(int idUtilisateur) {//Historique des réservations du client
        return reservationDAO.getReservationsByUtilisateur(idUtilisateur);
    }

    /**
     * Retourne toutes les réservations du système (accès administrateur)
     *
     * @return Liste de toutes les réservations enregistrées
     */
    public List<Reservation> getToutesReservations() { //Liste de toutes les réservations (admin)
        return reservationDAO.getAllReservations();
    }

    /**
     * Supprime une réservation (et sa facture associée) par son identifiant
     *
     * @param idReservation Identifiant de la réservation à supprimer
     * @return {@code true} si la suppression a réussi, {@code false} sinon
     */
    public boolean supprimerReservation(int idReservation) {
        return reservationDAO.deleteReservation(idReservation);
    }
}