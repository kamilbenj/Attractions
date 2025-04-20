package modele;

import java.math.BigDecimal;
import java.util.Date;

public class Attraction {
    private Long id;
    private String nom;
    private String description;
    private BigDecimal prix;
    private int capacite;
    private int duree; // en minutes
    private int ageMiniRequis;
    private int tailleMinRequise; // en cm
    private String imageUrl;
    private boolean estDisponible;
    private Date dateCreation;
    private Date dateModification;

    // Constructeurs
    public Attraction() {
        this.dateCreation = new Date();
        this.dateModification = new Date();
        this.estDisponible = true;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getAgeMiniRequis() {
        return ageMiniRequis;
    }

    public void setAgeMiniRequis(int ageMiniRequis) {
        this.ageMiniRequis = ageMiniRequis;
    }

    public int getTailleMinRequise() {
        return tailleMinRequise;
    }

    public void setTailleMinRequise(int tailleMinRequise) {
        this.tailleMinRequise = tailleMinRequise;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isEstDisponible() {
        return estDisponible;
    }

    public void setEstDisponible(boolean estDisponible) {
        this.estDisponible = estDisponible;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", capacite=" + capacite +
                ", duree=" + duree +
                ", estDisponible=" + estDisponible +
                '}';
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
