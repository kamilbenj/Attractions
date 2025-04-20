package dao;

import java.util.List;

/**
 * Interface générique pour les objets DAO
 * @param <T> Le type d'entité
 */
public interface DAO<T> {

    /**
     * Créer une nouvelle entité
     * @param entite L'entité à créer
     * @return L'entité créée
     * @throws ExceptionDAO En cas d'erreur d'accès aux données
     */
    T creer(T entite) throws ExceptionDAO;

    /**
     * Lire une entité par son identifiant
     * @param id L'identifiant de l'entité
     * @return L'entité ou null si non trouvée
     * @throws ExceptionDAO En cas d'erreur d'accès aux données
     */
    T lire(Long id) throws ExceptionDAO;

    /**
     * Mettre à jour une entité
     * @param entite L'entité à mettre à jour
     * @return L'entité mise à jour
     * @throws ExceptionDAO En cas d'erreur d'accès aux données
     */
    T mettreAJour(T entite) throws ExceptionDAO;

    /**
     * Supprimer une entité
     * @param id L'identifiant de l'entité à supprimer
     * @throws ExceptionDAO En cas d'erreur d'accès aux données
     */
    void supprimer(Long id) throws ExceptionDAO;

    /**
     * Lister toutes les entités
     * @return La liste de toutes les entités
     * @throws ExceptionDAO En cas d'erreur d'accès aux données
     */
    List<T> listerTous() throws ExceptionDAO;
}
