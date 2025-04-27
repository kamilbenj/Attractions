package view;

import javax.swing.*;
import java.awt.*;

public class PaymentView extends JFrame {

    public PaymentView(Runnable onPaymentConfirmed) {
        initUI(onPaymentConfirmed);
    }

    private void initUI(Runnable onPaymentConfirmed) {
        setTitle("Paiement");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Cliquez sur 'Payer' pour finaliser la réservation.", SwingConstants.CENTER);
        JButton payerButton = new JButton("Payer");

        payerButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "✅ Paiement réussi !");
            onPaymentConfirmed.run();
            dispose();
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        panel.add(payerButton, BorderLayout.SOUTH);

        add(panel);

        setVisible(true);
    }
}