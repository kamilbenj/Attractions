package dao;

import modele.Attraction;
import modele.Reservation;
import modele.Utilisateur;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Fabrique singleton pour créer des instances de DAO
 */
public class FabriqueDAO {
    private static FabriqueDAO instance;
    private Connection connexion; // Ajout de la connexion

    private FabriqueDAO() {
        // Initialiser la connexion ici ou dans une méthode dédiée
        try {
            // Exemple de connexion à une base de données
            // Remplacez ces valeurs par les vôtres
            String url = "jdbc:mysql://localhost:3306/attractions_db";
            String user = "root";
            String password = "";
            connexion = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtient l'instance unique de la fabrique
     * @return L'instance de la fabrique
     */
    public static synchronized FabriqueDAO getInstance() {
        if (instance == null) {
            instance = new FabriqueDAO();
        }
        return instance;
    }

    /**
     * Crée un DAO pour les utilisateurs
     * @return DAO pour les utilisateurs
     */
    public DAOGenerique<Utilisateur, Long> creerDAOUtilisateur() {
        return new DAOUtilisateur(connexion);
    }

    /**
     * Crée un DAO pour les attractions
     * @return DAO pour les attractions
     */
    public DAOGenerique<Attraction, Long> creerDAOAttraction() {
        return new DAOAttraction(connexion);
    }

    /**
     * Crée un DAO pour les réservations
     * @return DAO pour les réservations
     */
    public DAOGenerique<Reservation, Long> creerDAOReservation() {
        return new DAOReservation(connexion,
                creerDAOUtilisateur(),
                creerDAOAttraction());
    }

    /**
     * Classe privée implémentant DAO pour les utilisateurs
     */
    private class DAOUtilisateur implements DAOGenerique<Utilisateur, Long> {
        private Connection connexion;

        public DAOUtilisateur(Connection connexion) {
            this.connexion = connexion;
        }

        @Override
        public Utilisateur creer(Utilisateur entite) throws ExceptionDAO {
            try {
                String sql = "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, telephone, admin, date_creation, date_modification) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                stmt.setString(1, entite.getNom());
                stmt.setString(2, entite.getPrenom());
                stmt.setString(3, entite.getEmail());
                stmt.setString(4, entite.getMotDePasse());
                stmt.setString(5, entite.getTelephone());
                stmt.setBoolean(6, entite.isAdmin());
                stmt.setTimestamp(7, new Timestamp(entite.getDateCreation().getTime()));
                stmt.setTimestamp(8, new Timestamp(entite.getDateModification().getTime()));

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new ExceptionDAO("La création de l'utilisateur a échoué, aucune ligne affectée.");
                }

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entite.setId(generatedKeys.getLong(1));
                } else {
                    throw new ExceptionDAO("La création de l'utilisateur a échoué, aucun ID obtenu.");
                }

                return entite;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la création de l'utilisateur", e);
            }
        }

        @Override
        public Optional<Utilisateur> trouver(Long id) throws ExceptionDAO {
            try {
                String sql = "SELECT * FROM utilisateurs WHERE id = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getLong("id"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                    utilisateur.setTelephone(rs.getString("telephone"));
                    utilisateur.setAdmin(rs.getBoolean("admin"));
                    utilisateur.setDateCreation(rs.getTimestamp("date_creation"));
                    utilisateur.setDateModification(rs.getTimestamp("date_modification"));
                    return Optional.of(utilisateur);
                }
                return Optional.empty();
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la recherche de l'utilisateur", e);
            }
        }

        @Override
        public List<Utilisateur> trouverTous() throws ExceptionDAO {
            try {
                String sql = "SELECT * FROM utilisateurs";
                PreparedStatement stmt = connexion.prepareStatement(sql);

                ResultSet rs = stmt.executeQuery();
                List<Utilisateur> utilisateurs = new ArrayList<>();

                while (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getLong("id"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                    utilisateur.setTelephone(rs.getString("telephone"));
                    utilisateur.setAdmin(rs.getBoolean("admin"));
                    utilisateur.setDateCreation(rs.getTimestamp("date_creation"));
                    utilisateur.setDateModification(rs.getTimestamp("date_modification"));
                    utilisateurs.add(utilisateur);
                }

                return utilisateurs;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la récupération des utilisateurs", e);
            }
        }

        @Override
        public List<Utilisateur> trouverParAttribut(String nomAttribut, Object valeur) throws ExceptionDAO {
            try {
                String sql = "SELECT * FROM utilisateurs WHERE " + nomAttribut + " = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);

                // Définir le type de paramètre selon la valeur
                if (valeur instanceof String) {
                    stmt.setString(1, (String) valeur);
                } else if (valeur instanceof Long) {
                    stmt.setLong(1, (Long) valeur);
                } else if (valeur instanceof Integer) {
                    stmt.setInt(1, (Integer) valeur);
                } else if (valeur instanceof Boolean) {
                    stmt.setBoolean(1, (Boolean) valeur);
                } else {
                    stmt.setObject(1, valeur);
                }

                ResultSet rs = stmt.executeQuery();
                List<Utilisateur> utilisateurs = new ArrayList<>();

                while (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getLong("id"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                    utilisateur.setTelephone(rs.getString("telephone"));
                    utilisateur.setAdmin(rs.getBoolean("admin"));
                    utilisateur.setDateCreation(rs.getTimestamp("date_creation"));
                    utilisateur.setDateModification(rs.getTimestamp("date_modification"));
                    utilisateurs.add(utilisateur);
                }

                return utilisateurs;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la recherche par attribut des utilisateurs", e);
            }
        }

        @Override
        public Utilisateur mettreAJour(Utilisateur entite) throws ExceptionDAO {
            try {
                String sql = "UPDATE utilisateurs SET nom = ?, prenom = ?, email = ?, " +
                        "mot_de_passe = ?, telephone = ?, admin = ?, date_modification = ? " +
                        "WHERE id = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);

                stmt.setString(1, entite.getNom());
                stmt.setString(2, entite.getPrenom());
                stmt.setString(3, entite.getEmail());
                stmt.setString(4, entite.getMotDePasse());
                stmt.setString(5, entite.getTelephone());
                stmt.setBoolean(6, entite.isAdmin());
                stmt.setTimestamp(7, new Timestamp(new java.util.Date().getTime()));
                stmt.setLong(8, entite.getId());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new ExceptionDAO("La mise à jour de l'utilisateur a échoué, aucune ligne affectée.");
                }

                return entite;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la mise à jour de l'utilisateur", e);
            }
        }

        @Override
        public boolean supprimer(Long id) throws ExceptionDAO {
            try {
                String sql = "DELETE FROM utilisateurs WHERE id = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);
                stmt.setLong(1, id);

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la suppression de l'utilisateur", e);
            }
        }
    }

    /**
     * Classe privée implémentant DAO pour les attractions
     */
    private class DAOAttraction implements DAOGenerique<Attraction, Long> {
        private Connection connexion;

        public DAOAttraction(Connection connexion) {
            this.connexion = connexion;
        }

        @Override
        public Attraction creer(Attraction entite) throws ExceptionDAO {
            try {
                String sql = "INSERT INTO attractions (nom, description, capacite, duree, prix, age_mini_requis, taille_min_requise, est_disponible, image_url, date_creation, date_modification) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                stmt.setString(1, entite.getNom());
                stmt.setString(2, entite.getDescription());
                stmt.setInt(3, entite.getCapacite());
                stmt.setInt(4, entite.getDuree());
                stmt.setBigDecimal(5, entite.getPrix());
                stmt.setInt(6, entite.getAgeMiniRequis());
                stmt.setInt(7, entite.getTailleMinRequise());
                stmt.setBoolean(8, entite.isEstDisponible());
                stmt.setString(9, entite.getImageUrl());
                stmt.setTimestamp(10, new Timestamp(entite.getDateCreation().getTime()));
                stmt.setTimestamp(11, new Timestamp(entite.getDateModification().getTime()));

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new ExceptionDAO("La création de l'attraction a échoué, aucune ligne affectée.");
                }

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entite.setId(generatedKeys.getLong(1));
                } else {
                    throw new ExceptionDAO("La création de l'attraction a échoué, aucun ID obtenu.");
                }

                return entite;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la création de l'attraction", e);
            }
        }

        @Override
        public Optional<Attraction> trouver(Long id) throws ExceptionDAO {
            try {
                String sql = "SELECT * FROM attractions WHERE id = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Attraction attraction = new Attraction();
                    attraction.setId(rs.getLong("id"));
                    attraction.setNom(rs.getString("nom"));
                    attraction.setDescription(rs.getString("description"));
                    attraction.setCapacite(rs.getInt("capacite"));
                    attraction.setDuree(rs.getInt("duree"));
                    attraction.setPrix(rs.getBigDecimal("prix"));
                    attraction.setAgeMiniRequis(rs.getInt("age_mini_requis"));
                    attraction.setTailleMinRequise(rs.getInt("taille_min_requise"));
                    attraction.setEstDisponible(rs.getBoolean("est_disponible"));
                    attraction.setImageUrl(rs.getString("image_url"));
                    attraction.setDateCreation(rs.getTimestamp("date_creation"));
                    attraction.setDateModification(rs.getTimestamp("date_modification"));
                    return Optional.of(attraction);
                }
                return Optional.empty();
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la recherche de l'attraction", e);
            }
        }

        @Override
        public List<Attraction> trouverTous() throws ExceptionDAO {
            try {
                String sql = "SELECT * FROM attractions";
                PreparedStatement stmt = connexion.prepareStatement(sql);

                ResultSet rs = stmt.executeQuery();
                List<Attraction> attractions = new ArrayList<>();

                while (rs.next()) {
                    Attraction attraction = new Attraction();
                    attraction.setId(rs.getLong("id"));
                    attraction.setNom(rs.getString("nom"));
                    attraction.setDescription(rs.getString("description"));
                    attraction.setCapacite(rs.getInt("capacite"));
                    attraction.setDuree(rs.getInt("duree"));
                    attraction.setPrix(rs.getBigDecimal("prix"));
                    attraction.setAgeMiniRequis(rs.getInt("age_mini_requis"));
                    attraction.setTailleMinRequise(rs.getInt("taille_min_requise"));
                    attraction.setEstDisponible(rs.getBoolean("est_disponible"));
                    attraction.setImageUrl(rs.getString("image_url"));
                    attraction.setDateCreation(rs.getTimestamp("date_creation"));
                    attraction.setDateModification(rs.getTimestamp("date_modification"));
                    attractions.add(attraction);
                }

                return attractions;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la récupération des attractions", e);
            }
        }

        @Override
        public List<Attraction> trouverParAttribut(String nomAttribut, Object valeur) throws ExceptionDAO {
            try {
                String sql = "SELECT * FROM attractions WHERE " + nomAttribut + " = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);

                // Définir le type de paramètre selon la valeur
                if (valeur instanceof String) {
                    stmt.setString(1, (String) valeur);
                } else if (valeur instanceof Long) {
                    stmt.setLong(1, (Long) valeur);
                } else if (valeur instanceof Integer) {
                    stmt.setInt(1, (Integer) valeur);
                } else if (valeur instanceof Boolean) {
                    stmt.setBoolean(1, (Boolean) valeur);
                } else if (valeur instanceof BigDecimal) {
                    stmt.setBigDecimal(1, (BigDecimal) valeur);
                } else {
                    stmt.setObject(1, valeur);
                }

                ResultSet rs = stmt.executeQuery();
                List<Attraction> attractions = new ArrayList<>();

                while (rs.next()) {
                    Attraction attraction = new Attraction();
                    attraction.setId(rs.getLong("id"));
                    attraction.setNom(rs.getString("nom"));
                    attraction.setDescription(rs.getString("description"));
                    attraction.setCapacite(rs.getInt("capacite"));
                    attraction.setDuree(rs.getInt("duree"));
                    attraction.setPrix(rs.getBigDecimal("prix"));
                    attraction.setAgeMiniRequis(rs.getInt("age_mini_requis"));
                    attraction.setTailleMinRequise(rs.getInt("taille_min_requise"));
                    attraction.setEstDisponible(rs.getBoolean("est_disponible"));
                    attraction.setImageUrl(rs.getString("image_url"));
                    attraction.setDateCreation(rs.getTimestamp("date_creation"));
                    attraction.setDateModification(rs.getTimestamp("date_modification"));
                    attractions.add(attraction);
                }

                return attractions;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la recherche par attribut des attractions", e);
            }
        }

        @Override
        public Attraction mettreAJour(Attraction entite) throws ExceptionDAO {
            try {
                String sql = "UPDATE attractions SET nom = ?, description = ?, capacite = ?, " +
                        "duree = ?, prix = ?, age_mini_requis = ?, taille_min_requise = ?, " +
                        "est_disponible = ?, image_url = ?, date_modification = ? " +
                        "WHERE id = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);

                stmt.setString(1, entite.getNom());
                stmt.setString(2, entite.getDescription());
                stmt.setInt(3, entite.getCapacite());
                stmt.setInt(4, entite.getDuree());
                stmt.setBigDecimal(5, entite.getPrix());
                stmt.setInt(6, entite.getAgeMiniRequis());
                stmt.setInt(7, entite.getTailleMinRequise());
                stmt.setBoolean(8, entite.isEstDisponible());
                stmt.setString(9, entite.getImageUrl());
                stmt.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
                stmt.setLong(11, entite.getId());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new ExceptionDAO("La mise à jour de l'attraction a échoué, aucune ligne affectée.");
                }

                return entite;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la mise à jour de l'attraction", e);
            }
        }

        @Override
        public boolean supprimer(Long id) throws ExceptionDAO {
            try {
                String sql = "DELETE FROM attractions WHERE id = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);
                stmt.setLong(1, id);

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la suppression de l'attraction", e);
            }
        }
    }

    /**
     * Classe privée implémentant DAO pour les réservations
     */
    private class DAOReservation implements DAOGenerique<Reservation, Long> {
        private Connection connexion;
        private DAOGenerique<Utilisateur, Long> daoUtilisateur;
        private DAOGenerique<Attraction, Long> daoAttraction;

        public DAOReservation(Connection connexion,
                              DAOGenerique<Utilisateur, Long> daoUtilisateur,
                              DAOGenerique<Attraction, Long> daoAttraction) {
            this.connexion = connexion;
            this.daoUtilisateur = daoUtilisateur;
            this.daoAttraction = daoAttraction;
        }

        @Override
        public Reservation creer(Reservation entite) throws ExceptionDAO {
            try {
                String sql = "INSERT INTO reservations (utilisateur_id, attraction_id, date_reservation, " +
                        "heure_reservation, nombre_personnes, prix_total, statut, commentaire, date_creation, date_modification) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                stmt.setLong(1, entite.getUtilisateur().getId());
                stmt.setLong(2, entite.getAttraction().getId());
                stmt.setDate(3, new java.sql.Date(entite.getDateReservation().getTime()));
                stmt.setTime(4, new java.sql.Time(entite.getHeureReservation().getTime()));
                stmt.setInt(5, entite.getNombrePersonnes());
                stmt.setBigDecimal(6, entite.getPrixTotal());
                stmt.setString(7, entite.getStatut());
                stmt.setString(8, entite.getCommentaire());
                stmt.setTimestamp(9, new Timestamp(entite.getDateCreation().getTime()));
                stmt.setTimestamp(10, new Timestamp(entite.getDateModification().getTime()));

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new ExceptionDAO("La création de la réservation a échoué, aucune ligne affectée.");
                }

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entite.setId(generatedKeys.getLong(1));
                } else {
                    throw new ExceptionDAO("La création de la réservation a échoué, aucun ID obtenu.");
                }

                return entite;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la création de la réservation", e);
            }
        }

        @Override
        public Optional<Reservation> trouver(Long id) throws ExceptionDAO {
            try {
                String sql = "SELECT * FROM reservations WHERE id = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Long utilisateurId = rs.getLong("utilisateur_id");
                    Long attractionId = rs.getLong("attraction_id");

                    Optional<Utilisateur> utilisateurOpt = daoUtilisateur.trouver(utilisateurId);
                    Optional<Attraction> attractionOpt = daoAttraction.trouver(attractionId);

                    if (utilisateurOpt.isPresent() && attractionOpt.isPresent()) {
                        Reservation reservation = new Reservation();
                        reservation.setId(rs.getLong("id"));
                        reservation.setUtilisateur(utilisateurOpt.get());
                        reservation.setAttraction(attractionOpt.get());
                        reservation.setDateReservation(rs.getDate("date_reservation"));
                        reservation.setHeureReservation(rs.getTime("heure_reservation"));
                        reservation.setNombrePersonnes(rs.getInt("nombre_personnes"));
                        reservation.setPrixTotal(rs.getBigDecimal("prix_total"));
                        reservation.setStatut(rs.getString("statut"));
                        reservation.setCommentaire(rs.getString("commentaire"));
                        reservation.setDateCreation(rs.getTimestamp("date_creation"));
                        reservation.setDateModification(rs.getTimestamp("date_modification"));
                        return Optional.of(reservation);
                    }
                }
                return Optional.empty();
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la recherche de la réservation", e);
            }
        }

        @Override
        public List<Reservation> trouverTous() throws ExceptionDAO {
            try {
                String sql = "SELECT * FROM reservations";
                PreparedStatement stmt = connexion.prepareStatement(sql);

                ResultSet rs = stmt.executeQuery();
                List<Reservation> reservations = new ArrayList<>();

                while (rs.next()) {
                    Long utilisateurId = rs.getLong("utilisateur_id");
                    Long attractionId = rs.getLong("attraction_id");

                    Optional<Utilisateur> utilisateurOpt = daoUtilisateur.trouver(utilisateurId);
                    Optional<Attraction> attractionOpt = daoAttraction.trouver(attractionId);

                    if (utilisateurOpt.isPresent() && attractionOpt.isPresent()) {
                        Reservation reservation = new Reservation();
                        reservation.setId(rs.getLong("id"));
                        reservation.setUtilisateur(utilisateurOpt.get());
                        reservation.setAttraction(attractionOpt.get());
                        reservation.setDateReservation(rs.getDate("date_reservation"));
                        reservation.setHeureReservation(rs.getTime("heure_reservation"));
                        reservation.setNombrePersonnes(rs.getInt("nombre_personnes"));
                        reservation.setPrixTotal(rs.getBigDecimal("prix_total"));
                        reservation.setStatut(rs.getString("statut"));
                        reservation.setCommentaire(rs.getString("commentaire"));
                        reservation.setDateCreation(rs.getTimestamp("date_creation"));
                        reservation.setDateModification(rs.getTimestamp("date_modification"));
                        reservations.add(reservation);
                    }
                }

                return reservations;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la récupération des réservations", e);
            }
        }

        @Override
        public List<Reservation> trouverParAttribut(String nomAttribut, Object valeur) throws ExceptionDAO {
            try {
                String sql = "SELECT * FROM reservations WHERE " + nomAttribut + " = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);

                // Définir le type de paramètre selon la valeur
                if (valeur instanceof String) {
                    stmt.setString(1, (String) valeur);
                } else if (valeur instanceof Long) {
                    stmt.setLong(1, (Long) valeur);
                } else if (valeur instanceof Integer) {
                    stmt.setInt(1, (Integer) valeur);
                } else if (valeur instanceof BigDecimal) {
                    stmt.setBigDecimal(1, (BigDecimal) valeur);
                } else {
                    stmt.setObject(1, valeur);
                }

                ResultSet rs = stmt.executeQuery();
                List<Reservation> reservations = new ArrayList<>();

                while (rs.next()) {
                    Long utilisateurId = rs.getLong("utilisateur_id");
                    Long attractionId = rs.getLong("attraction_id");

                    Optional<Utilisateur> utilisateurOpt = daoUtilisateur.trouver(utilisateurId);
                    Optional<Attraction> attractionOpt = daoAttraction.trouver(attractionId);

                    if (utilisateurOpt.isPresent() && attractionOpt.isPresent()) {
                        Reservation reservation = new Reservation();
                        reservation.setId(rs.getLong("id"));
                        reservation.setUtilisateur(utilisateurOpt.get());
                        reservation.setAttraction(attractionOpt.get());
                        reservation.setDateReservation(rs.getDate("date_reservation"));
                        reservation.setHeureReservation(rs.getTime("heure_reservation"));
                        reservation.setNombrePersonnes(rs.getInt("nombre_personnes"));
                        reservation.setPrixTotal(rs.getBigDecimal("prix_total"));
                        reservation.setStatut(rs.getString("statut"));
                        reservation.setCommentaire(rs.getString("commentaire"));
                        reservation.setDateCreation(rs.getTimestamp("date_creation"));
                        reservation.setDateModification(rs.getTimestamp("date_modification"));
                        reservations.add(reservation);
                    }
                }

                return reservations;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la recherche par attribut des réservations", e);
            }
        }

        @Override
        public Reservation mettreAJour(Reservation entite) throws ExceptionDAO {
            try {
                String sql = "UPDATE reservations SET utilisateur_id = ?, attraction_id = ?, " +
                        "date_reservation = ?, heure_reservation = ?, nombre_personnes = ?, " +
                        "prix_total = ?, statut = ?, commentaire = ?, date_modification = ? " +
                        "WHERE id = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);

                stmt.setLong(1, entite.getUtilisateur().getId());
                stmt.setLong(2, entite.getAttraction().getId());
                stmt.setDate(3, new java.sql.Date(entite.getDateReservation().getTime()));
                stmt.setTime(4, new java.sql.Time(entite.getHeureReservation().getTime()));
                stmt.setInt(5, entite.getNombrePersonnes());
                stmt.setBigDecimal(6, entite.getPrixTotal());
                stmt.setString(7, entite.getStatut());
                stmt.setString(8, entite.getCommentaire());
                stmt.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
                stmt.setLong(10, entite.getId());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new ExceptionDAO("La mise à jour de la réservation a échoué, aucune ligne affectée.");
                }

                return entite;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la mise à jour de la réservation", e);
            }
        }

        @Override
        public boolean supprimer(Long id) throws ExceptionDAO {
            try {
                String sql = "DELETE FROM reservations WHERE id = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);
                stmt.setLong(1, id);

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                throw new ExceptionDAO("Erreur lors de la suppression de la réservation", e);
            }
        }

        /**
         * Trouver les réservations par utilisateur
         * @param utilisateurId ID de l'utilisateur
         * @return Liste des réservations de cet utilisateur
         * @throws ExceptionDAO Si une erreur survient
         */
        public List<Reservation> trouverParUtilisateur(Long utilisateurId) throws ExceptionDAO {
            return trouverParAttribut("utilisateur_id", utilisateurId);
        }

        /**
         * Trouver les réservations par attraction
         * @param attractionId ID de l'attraction
         * @return Liste des réservations pour cette attraction
         * @throws ExceptionDAO Si une erreur survient
         */
        public List<Reservation> trouverParAttraction(Long attractionId) throws ExceptionDAO {
            return trouverParAttribut("attraction_id", attractionId);
        }
    }
}

