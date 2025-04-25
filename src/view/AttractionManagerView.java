package view;

import controller.AdminController;
import model.Attraction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AttractionManagerView extends JFrame {

    private final AdminController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    public AttractionManagerView() {
        this.controller = new AdminController();
        initUI();
        loadAttractions();
    }

    private void initUI() {
        setTitle("Gestion des attractions");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Nom", "Description", "Prix", "Capacité", "Disponible"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton ajouterButton = new JButton("Ajouter");
        JButton modifierButton = new JButton("Modifier");
        JButton supprimerButton = new JButton("Supprimer");
        JButton rafraichirButton = new JButton("Rafraîchir");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(ajouterButton);
        buttonPanel.add(modifierButton);
        buttonPanel.add(supprimerButton);
        buttonPanel.add(rafraichirButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        ajouterButton.addActionListener(e -> showAttractionDialog(null));
        modifierButton.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                int id = (int) tableModel.getValueAt(selected, 0);
                Attraction a = controller.getAttractionById(id);
                showAttractionDialog(a);
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez une attraction.");
            }
        });

        supprimerButton.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                int id = (int) tableModel.getValueAt(selected, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cette attraction ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.supprimerAttraction(id);
                    loadAttractions();
                }
            }
        });

        rafraichirButton.addActionListener(e -> loadAttractions());

        setVisible(true);
    }

    private void loadAttractions() {
        tableModel.setRowCount(0); // reset
        List<Attraction> attractions = controller.listerAttractions();
        for (Attraction a : attractions) {
            tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getNom(),
                    a.getDescription(),
                    a.getPrix(),
                    a.getCapacite(),
                    a.isDisponible() ? "Oui" : "Non"
            });
        }
    }

    private void showAttractionDialog(Attraction existing) {
        JTextField nomField = new JTextField();
        JTextField descField = new JTextField();
        JTextField prixField = new JTextField();
        JTextField capaciteField = new JTextField();
        JCheckBox dispoCheck = new JCheckBox("Disponible");

        if (existing != null) {
            nomField.setText(existing.getNom());
            descField.setText(existing.getDescription());
            prixField.setText(String.valueOf(existing.getPrix()));
            capaciteField.setText(String.valueOf(existing.getCapacite()));
            dispoCheck.setSelected(existing.isDisponible());
        }

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("Nom :"));
        panel.add(nomField);
        panel.add(new JLabel("Description :"));
        panel.add(descField);
        panel.add(new JLabel("Prix (€) :"));
        panel.add(prixField);
        panel.add(new JLabel("Capacité :"));
        panel.add(capaciteField);
        panel.add(new JLabel(""));
        panel.add(dispoCheck);

        int result = JOptionPane.showConfirmDialog(this, panel, existing == null ? "Ajouter une attraction" : "Modifier l'attraction", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nom = nomField.getText().trim();
                String desc = descField.getText().trim();
                double prix = Double.parseDouble(prixField.getText().trim());
                int capacite = Integer.parseInt(capaciteField.getText().trim());
                boolean dispo = dispoCheck.isSelected();

                Attraction a = new Attraction(nom, desc, prix, capacite, dispo);
                if (existing != null) {
                    a.setId(existing.getId());
                    controller.modifierAttraction(a);
                } else {
                    controller.ajouterAttraction(a);
                }

                loadAttractions();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Prix ou capacité invalide.");
            }
        }
    }
}