package controller;

import dao.AttractionDAO;
import dao.ReductionDAO;
import model.Attraction;
import model.Reduction;

import java.util.List;

public class AdminController {

    private final AttractionDAO attractionDAO;
    private final ReductionDAO reductionDAO;

    public AdminController() {
        this.attractionDAO = new AttractionDAO();
        this.reductionDAO = new ReductionDAO();
    }

    // ------------------ Gestion Attractions ------------------ //

    public boolean ajouterAttraction(Attraction a) {
        return attractionDAO.insertAttraction(a);
    }

    public boolean modifierAttraction(Attraction a) {
        return attractionDAO.updateAttraction(a);
    }

    public boolean supprimerAttraction(int id) {
        return attractionDAO.deleteAttraction(id);
    }

    public List<Attraction> listerAttractions() {
        return attractionDAO.getAllAttractions();
    }

    public Attraction getAttractionById(int id) {
        return attractionDAO.getAttractionById(id);
    }

    // ------------------ Gestion Réductions ------------------ //

    public List<Reduction> listerReductions() {
        return reductionDAO.getAllReductions();
    }

    public boolean supprimerReduction(int idReduction) {
        return reductionDAO.deleteReduction(idReduction);
    }

    public boolean ajouterReduction(Reduction r) {
        return reductionDAO.insertReduction(r);
    }
}