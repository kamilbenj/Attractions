package service;

import dao.DAOGenerique;
import dao.ExceptionDAO;
import dao.FabriqueDAO;
import modele.Attraction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Service pour la gestion des attractions
 */
public class ServiceAttraction {
    private static ServiceAttraction instance;
    private final DAOGenerique<Attraction, Long> daoAttraction;

    private ServiceAttraction() {
        this.daoAttraction = FabriqueDAO.getInstance().creerDAOAttraction();
    }

    /**
     * Obtient l'instance unique du service
     * @return L'instance du service
     */
    public static synchronized ServiceAttraction getInstance() {
        if (instance == null) {
            instance = new ServiceAttraction();
        }
        return instance;
    }

    /**
     * Obtenir toutes les attractions
     * @return Liste des attractions
     * @throws ServiceException Si une erreur survient
     */
    public List<Attraction> obtenirToutes() throws ServiceException {
        try {
            return daoAttraction.trouverTous();
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la récupération des attractions", e);
        }
    }

    /**
     * Obtenir une attraction par son ID
     * @param id ID de l'attraction
     * @return Attraction trouvée ou null si inexistante
     * @throws ServiceException Si une erreur survient
     */
    public Attraction obtenirParId(Long id) throws ServiceException {
        try {
            return daoAttraction.trouver(id).orElse(null);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la récupération de l'attraction avec l'ID " + id, e);
        }
    }

    /**
     * Crée une nouvelle attraction
     * @param nom Nom de l'attraction
     * @param description Description de l'attraction
     * @param capacite Capacité de l'attraction
     * @param duree Durée de l'attraction en minutes
     * @param prix Prix de l'attraction
     * @param ageMinimum Âge minimum pour l'attraction
     * @param tailleMinimum Taille minimum pour l'attraction en cm
     * @param estDisponible Disponibilité de l'attraction
     * @param imageUrl URL de l'image de l'attraction
     * @return Attraction créée
     * @throws ServiceException Si la création échoue
     */
    public Attraction creer(String nom, String description, int capacite,
                            int duree, BigDecimal prix, int ageMinimum,
                            int tailleMinimum, boolean estDisponible, String imageUrl)
            throws ServiceException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ServiceException("Le nom de l'attraction ne peut pas être vide");
        }
        if (capacite <= 0) {
            throw new ServiceException("La capacité doit être supérieure à zéro");
        }
        if (duree <= 0) {
            throw new ServiceException("La durée doit être supérieure à zéro");
        }
        if (prix == null || prix.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("Le prix ne peut pas être négatif");
        }

        try {
            Attraction attraction = new Attraction();
            attraction.setNom(nom);
            attraction.setDescription(description);
            attraction.setCapacite(capacite);
            attraction.setDuree(duree);
            attraction.setPrix(prix);
            attraction.setAgeMiniRequis(ageMinimum);
            attraction.setTailleMinRequise(tailleMinimum);
            attraction.setEstDisponible(estDisponible);
            attraction.setImageUrl(imageUrl);
            attraction.setDateCreation(new Date());
            attraction.setDateModification(new Date());

            return daoAttraction.creer(attraction);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la création de l'attraction", e);
        }
    }

    /**
     * Mettre à jour une attraction existante
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
     * @return Attraction mise à jour
     * @throws ServiceException Si la mise à jour échoue
     */
    public Attraction mettreAJour(Long id, String nom, String description, int capacite,
                                  int duree, BigDecimal prix, int ageMinimum,
                                  int tailleMinimum, boolean estDisponible, String imageUrl)
            throws ServiceException {
        if (id == null) {
            throw new ServiceException("L'ID de l'attraction ne peut pas être null");
        }

        try {
            Attraction attraction = daoAttraction.trouver(id)
                    .orElseThrow(() -> new ServiceException("Attraction non trouvée avec l'ID: " + id));

            if (nom != null && !nom.trim().isEmpty()) {
                attraction.setNom(nom);
            }

            attraction.setDescription(description);

            if (capacite > 0) {
                attraction.setCapacite(capacite);
            }

            if (duree > 0) {
                attraction.setDuree(duree);
            }

            if (prix != null && prix.compareTo(BigDecimal.ZERO) >= 0) {
                attraction.setPrix(prix);
            }

            if (ageMinimum >= 0) {
                attraction.setAgeMiniRequis(ageMinimum);
            }

            if (tailleMinimum >= 0) {
                attraction.setTailleMinRequise(tailleMinimum);
            }

            attraction.setEstDisponible(estDisponible);
            attraction.setImageUrl(imageUrl);
            attraction.setDateModification(new Date());

            return daoAttraction.mettreAJour(attraction);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la mise à jour de l'attraction", e);
        }
    }

    /**
     * Supprimer une attraction
     * @param id ID de l'attraction à supprimer
     * @throws ServiceException Si la suppression échoue
     */
    public void supprimer(Long id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("L'ID de l'attraction ne peut pas être null");
        }

        try {
            boolean supprime = daoAttraction.supprimer(id);
            if (!supprime) {
                throw new ServiceException("Impossible de supprimer l'attraction avec l'ID " + id);
            }
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la suppression de l'attraction", e);
        }
    }
}
