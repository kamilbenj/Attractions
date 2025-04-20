package service;

import dao.DAOGenerique;
import dao.ExceptionDAO;
import dao.FabriqueDAO;
import modele.Utilisateur;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des utilisateurs
 */
public class ServiceUtilisateur {
    private static ServiceUtilisateur instance;
    private final DAOGenerique<Utilisateur, Long> daoUtilisateur;

    private ServiceUtilisateur() {
        this.daoUtilisateur = FabriqueDAO.getInstance().creerDAOUtilisateur();
    }

    /**
     * Obtient l'instance unique du service
     * @return L'instance du service
     */
    public static synchronized ServiceUtilisateur getInstance() {
        if (instance == null) {
            instance = new ServiceUtilisateur();
        }
        return instance;
    }

    /**
     * Obtenir tous les utilisateurs
     * @return Liste des utilisateurs
     * @throws ServiceException Si une erreur survient
     */
    public List<Utilisateur> obtenirTous() throws ServiceException {
        try {
            return daoUtilisateur.trouverTous();
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la récupération des utilisateurs", e);
        }
    }

    /**
     * Obtenir un utilisateur par son ID
     * @param id ID de l'utilisateur
     * @return Utilisateur trouvé ou null si inexistant
     * @throws ServiceException Si une erreur survient
     */
    public Utilisateur obtenirParId(Long id) throws ServiceException {
        try {
            Optional<Utilisateur> optUtilisateur = daoUtilisateur.trouver(id);
            return optUtilisateur.orElse(null);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la récupération de l'utilisateur avec l'ID " + id, e);
        }
    }

    /**
     * Obtenir un utilisateur par son email
     * @param email Email de l'utilisateur
     * @return Utilisateur trouvé ou null si inexistant
     * @throws ServiceException Si une erreur survient
     */
    public Utilisateur obtenirParEmail(String email) throws ServiceException {
        if (email == null || email.trim().isEmpty()) {
            throw new ServiceException("L'email ne peut pas être vide");
        }

        try {
            List<Utilisateur> utilisateurs = daoUtilisateur.trouverParAttribut("email", email);
            return utilisateurs.isEmpty() ? null : utilisateurs.get(0);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la récupération de l'utilisateur avec l'email " + email, e);
        }
    }

    /**
     * Connexion d'un utilisateur
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe non haché
     * @return Utilisateur connecté
     * @throws ServiceException Si la connexion échoue
     */
    public Utilisateur connecter(String email, String motDePasse) throws ServiceException {
        if (email == null || email.trim().isEmpty()) {
            throw new ServiceException("L'email ne peut pas être vide");
        }
        if (motDePasse == null || motDePasse.trim().isEmpty()) {
            throw new ServiceException("Le mot de passe ne peut pas être vide");
        }

        Utilisateur utilisateur = obtenirParEmail(email);
        if (utilisateur == null) {
            throw new ServiceException("Utilisateur non trouvé avec l'email: " + email);
        }

        String motDePasseHache = hacher(motDePasse);
        if (!utilisateur.getMotDePasse().equals(motDePasseHache)) {
            throw new ServiceException("Mot de passe incorrect");
        }

        return utilisateur;
    }

    /**
     * Crée un nouvel utilisateur
     * @param nom Nom de l'utilisateur
     * @param prenom Prénom de l'utilisateur
     * @param email Email de l'utilisateur (unique)
     * @param motDePasse Mot de passe non haché
     * @param telephone Téléphone de l'utilisateur
     * @param estAdmin Flag indiquant si l'utilisateur est admin
     * @return Utilisateur créé
     * @throws ServiceException Si la création échoue
     */
    public Utilisateur creer(String nom, String prenom, String email,
                             String motDePasse, String telephone, boolean estAdmin)
            throws ServiceException {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ServiceException("Le nom ne peut pas être vide");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new ServiceException("Le prénom ne peut pas être vide");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ServiceException("L'email ne peut pas être vide");
        }
        if (motDePasse == null || motDePasse.trim().isEmpty()) {
            throw new ServiceException("Le mot de passe ne peut pas être vide");
        }

        try {
            // Vérifier que l'email n'est pas déjà utilisé
            Utilisateur existant = obtenirParEmail(email);
            if (existant != null) {
                throw new ServiceException("Un utilisateur avec cet email existe déjà");
            }

            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setEmail(email);
            utilisateur.setMotDePasse(hacher(motDePasse));
            utilisateur.setTelephone(telephone);
            utilisateur.setAdmin(estAdmin);

            Date maintenant = new Date();
            utilisateur.setDateCreation(maintenant);
            utilisateur.setDateModification(maintenant);

            return daoUtilisateur.creer(utilisateur);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la création de l'utilisateur", e);
        }
    }

    /**
     * Met à jour un utilisateur existant
     * @param id ID de l'utilisateur
     * @param nom Nouveau nom
     * @param prenom Nouveau prénom
     * @param telephone Nouveau téléphone
     * @param motDePasse Nouveau mot de passe (nullable)
     * @return Utilisateur mis à jour
     * @throws ServiceException Si la mise à jour échoue
     */
    public Utilisateur mettreAJour(Long id, String nom, String prenom,
                                   String telephone, String motDePasse)
            throws ServiceException {
        if (id == null) {
            throw new ServiceException("L'ID de l'utilisateur ne peut pas être null");
        }

        try {
            Optional<Utilisateur> optUtilisateur = daoUtilisateur.trouver(id);
            if (!optUtilisateur.isPresent()) {
                throw new ServiceException("Utilisateur non trouvé avec l'ID: " + id);
            }

            Utilisateur utilisateur = optUtilisateur.get();

            if (nom != null && !nom.trim().isEmpty()) {
                utilisateur.setNom(nom);
            }

            if (prenom != null && !prenom.trim().isEmpty()) {
                utilisateur.setPrenom(prenom);
            }

            utilisateur.setTelephone(telephone);

            if (motDePasse != null && !motDePasse.trim().isEmpty()) {
                utilisateur.setMotDePasse(hacher(motDePasse));
            }

            utilisateur.setDateModification(new Date());

            return daoUtilisateur.mettreAJour(utilisateur);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }

    /**
     * Définit le statut admin d'un utilisateur (admin seulement)
     * @param id ID de l'utilisateur
     * @param estAdmin Nouveau statut admin
     * @return Utilisateur mis à jour
     * @throws ServiceException Si la mise à jour échoue
     */
    public Utilisateur definirAdmin(Long id, boolean estAdmin) throws ServiceException {
        if (id == null) {
            throw new ServiceException("L'ID de l'utilisateur ne peut pas être null");
        }

        try {
            Optional<Utilisateur> optUtilisateur = daoUtilisateur.trouver(id);
            if (!optUtilisateur.isPresent()) {
                throw new ServiceException("Utilisateur non trouvé avec l'ID: " + id);
            }

            Utilisateur utilisateur = optUtilisateur.get();
            utilisateur.setAdmin(estAdmin);
            utilisateur.setDateModification(new Date());

            return daoUtilisateur.mettreAJour(utilisateur);
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la définition du statut admin", e);
        }
    }

    /**
     * Supprime un utilisateur
     * @param id ID de l'utilisateur à supprimer
     * @throws ServiceException Si la suppression échoue
     */
    public void supprimer(Long id) throws ServiceException {
        if (id == null) {
            throw new ServiceException("L'ID de l'utilisateur ne peut pas être null");
        }

        try {
            boolean supprime = daoUtilisateur.supprimer(id);
            if (!supprime) {
                throw new ServiceException("Impossible de supprimer l'utilisateur avec l'ID " + id);
            }
        } catch (ExceptionDAO e) {
            throw new ServiceException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    /**
     * Hache un mot de passe avec SHA-256
     * @param motDePasse Mot de passe en clair
     * @return Mot de passe haché
     * @throws ServiceException Si le hachage échoue
     */
    private String hacher(String motDePasse) throws ServiceException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(motDePasse.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("Erreur lors du hachage du mot de passe", e);
        }
    }
}
