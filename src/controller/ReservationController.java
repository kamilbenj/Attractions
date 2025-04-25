package controller;

import dao.*;
import model.*;

import java.time.LocalDate;
import java.util.List;

public class ReservationController {

    private final ReservationDAO reservationDAO;

    public ReservationController() {
        this.reservationDAO = new ReservationDAO();
    }

    /**
     * Crée une nouvelle réservation.
     */
    public boolean reserverAttraction(int idUtilisateur, int idAttraction, LocalDate date, int nbBillets) {
        if (nbBillets <= 0 || date.isBefore(LocalDate.now())) return false;

        Reservation reservation = new Reservation(
                idUtilisateur,
                idAttraction,
                date,
                nbBillets,
                Reservation.StatutReservation.CONFIRMEE
        );

        int idReservation = reservationDAO.insertReservation(reservation);

        if (idReservation > 0) {
            reservation.setId(idReservation);

            Attraction a = new AttractionDAO().getAttractionById(idAttraction);
            Utilisateur u = new UtilisateurDAO().getUtilisateurById(idUtilisateur);

            double prixUnitaire = a.getPrix();
            double montantTotal = prixUnitaire * nbBillets;
            boolean reductionAppliquee = false;

            // 💸 Application automatique de réduction
            int age = u.getAge();
            if (age < 12 || age > 60 || u.getType() == Utilisateur.TypeUtilisateur.MEMBRE) {
                ReductionDAO reductionDAO = new ReductionDAO();
                List<Reduction> reductions = reductionDAO.getAllReductions();

                for (Reduction r : reductions) {
                    boolean applicable =
                            (r.getCritere() == Reduction.CritereReduction.ENFANT && age < 12) ||
                                    (r.getCritere() == Reduction.CritereReduction.SENIOR && age > 60) ||
                                    (r.getCritere() == Reduction.CritereReduction.FIDELITE && u.getType() == Utilisateur.TypeUtilisateur.MEMBRE);

                    if (applicable) {
                        montantTotal *= (1 - (r.getPourcentage() / 100.0));
                        reductionAppliquee = true;
                        break; // applique la première réduction trouvée
                    }
                }
            }

            Facture facture = new Facture(
                    idReservation,
                    montantTotal,
                    LocalDate.now(),
                    reductionAppliquee
            );

            new FactureDAO().insertFacture(facture);
            return true;
        }

        return false;
    }

    /**
     * Récupère toutes les réservations d’un utilisateur.
     */
    public List<Reservation> getHistoriqueUtilisateur(int idUtilisateur) {
        return reservationDAO.getReservationsByUtilisateur(idUtilisateur);
    }

    /**
     * Permet à un admin d'afficher toutes les réservations.
     */
    public List<Reservation> getToutesReservations() {
        return reservationDAO.getAllReservations();
    }

    /**
     * Annule une réservation par son ID.
     */
    public boolean annulerReservation(int idReservation) {
        Reservation r = reservationDAO.getReservationById(idReservation);
        if (r != null) {
            r.setStatut(Reservation.StatutReservation.ANNULEE);
            return reservationDAO.updateReservation(r);
        }
        return false;
    }
}