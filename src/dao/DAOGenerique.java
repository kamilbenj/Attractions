package dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour les opérations DAO
 * @param <T> Type d'entité
 * @param <ID> Type de l'identifiant
 */
public interface DAOGenerique<T, ID> {

    /**
     * Crée une nouvelle entité
     * @param entite Entité à créer
     * @return Entité créée avec son ID
     * @throws ExceptionDAO Si une erreur survient
     */
    T creer(T entite) throws ExceptionDAO;

    /**
     * Recherche une entité par son ID
     * @param id ID de l'entité
     * @return Entité trouvée ou Optional vide
     * @throws ExceptionDAO Si une erreur survient
     */
    Optional<T> trouver(ID id) throws ExceptionDAO;

    /**
     * Récupère toutes les entités
     * @return Liste des entités
     * @throws ExceptionDAO Si une erreur survient
     */
    List<T> trouverTous() throws ExceptionDAO;

    /**
     * Trouve des entités par la valeur d'un attribut
     * @param nomAttribut Nom de l'attribut
     * @param valeur Valeur recherchée
     * @return Liste des entités correspondantes
     * @throws ExceptionDAO Si une erreur survient
     */
    List<T> trouverParAttribut(String nomAttribut, Object valeur) throws ExceptionDAO;

    /**
     * Met à jour une entité existante
     * @param entite Entité à mettre à jour
     * @return Entité mise à jour
     * @throws ExceptionDAO Si une erreur survient
     */
    T mettreAJour(T entite) throws ExceptionDAO;

    /**
     * Supprime une entité
     * @param id ID de l'entité à supprimer
     * @return true si supprimé, false sinon
     * @throws ExceptionDAO Si une erreur survient
     */
    boolean supprimer(ID id) throws ExceptionDAO;
}
