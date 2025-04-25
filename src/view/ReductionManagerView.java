package view;

import controller.AdminController;
import model.Reduction;
import model.Reduction.CritereReduction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReductionManagerView extends JFrame {

    private final AdminController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    public ReductionManagerView() {
        this.controller = new AdminController();
        initUI();
        loadReductions();
    }

    private void initUI() {
        setTitle("Gestion des réductions");
        setSize(600, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Nom", "Pourcentage", "Critère"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton ajouterButton = new JButton("Ajouter");
        JButton supprimerButton = new JButton("Supprimer");
        JButton rafraichirButton = new JButton("Rafraîchir");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(ajouterButton);
        buttonPanel.add(supprimerButton);
        buttonPanel.add(rafraichirButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        ajouterButton.addActionListener(e -> showReductionDialog());
        supprimerButton.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                int id = (int) tableModel.getValueAt(selected, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cette réduction ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.supprimerReduction(id);
                    loadReductions();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez une réduction.");
            }
        });

        rafraichirButton.addActionListener(e -> loadReductions());

        setVisible(true);
    }

    private void loadReductions() {
        tableModel.setRowCount(0);
        List<Reduction> reductions = controller.listerReductions();
        for (Reduction r : reductions) {
            tableModel.addRow(new Object[]{
                    r.getId(),
                    r.getNom(),
                    r.getPourcentage() + "%",
                    r.getCritere()
            });
        }
    }

    private void showReductionDialog() {
        JTextField nomField = new JTextField();
        JTextField pourcentageField = new JTextField();
        JComboBox<CritereReduction> critereBox = new JComboBox<>(CritereReduction.values());

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Nom de la réduction :"));
        panel.add(nomField);
        panel.add(new JLabel("Pourcentage :"));
        panel.add(pourcentageField);
        panel.add(new JLabel("Critère :"));
        panel.add(critereBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter une réduction", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nom = nomField.getText().trim();
                int pourcentage = Integer.parseInt(pourcentageField.getText().trim());
                CritereReduction critere = (CritereReduction) critereBox.getSelectedItem();

                Reduction r = new Reduction(nom, pourcentage, critere);
                controller.ajouterReduction(r);
                loadReductions();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Pourcentage invalide.");
            }
        }
    }
}