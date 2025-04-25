package view;


import controller.ReservationController;
import dao.AttractionDAO;
import model.Attraction;
import model.Reservation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportingView extends JFrame {

    private final ReservationController reservationController;
    private final AttractionDAO attractionDAO;

    public ReportingView() {
        this.reservationController = new ReservationController();
        this.attractionDAO = new AttractionDAO();
        initUI();
    }

    private void initUI() {
        setTitle("Statistiques des réservations");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.add(buildReservationsChart(), BorderLayout.CENTER);
        add(chartPanel);

        setVisible(true);
    }

    private ChartPanel buildReservationsChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<Reservation> reservations = reservationController.getToutesReservations();
        Map<Integer, Integer> countMap = new HashMap<>();

        for (Reservation r : reservations) {
            countMap.put(r.getIdAttraction(), countMap.getOrDefault(r.getIdAttraction(), 0) + 1);
        }

        for (Attraction a : attractionDAO.getAllAttractions()) {
            int nb = countMap.getOrDefault(a.getId(), 0);
            dataset.addValue(nb, "Réservations", a.getNom());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Réservations par attraction",
                "Attraction",
                "Nombre de réservations",
                dataset
        );

        return new ChartPanel(barChart);
    }
}