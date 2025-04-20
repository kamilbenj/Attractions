package modele;

import java.util.Date;
import java.util.Objects;

/**
 * Classe abstraite servant de base pour toutes les entités du système
 */
public abstract class EntiteBase {
    protected Long id;
    protected Date dateCreation;
    protected Date dateModification;

    public EntiteBase() {
        this.dateCreation = new Date();
        this.dateModification = new Date();
    }

    public EntiteBase(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EntiteBase entiteBase = (EntiteBase) obj;
        return Objects.equals(id, entiteBase.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
