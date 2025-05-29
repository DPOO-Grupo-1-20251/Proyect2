package vista.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class TicketViewer {

    public static void mostrarTiquete(String nombreUsuario, String atraccion, String tipoTiquete, BufferedImage qr) {
        JFrame frame = new JFrame("Tiquete generado");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
        ticketPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ticketPanel.add(new JLabel("Nombre: " + nombreUsuario));
        ticketPanel.add(new JLabel("Atracción: " + atraccion));
        ticketPanel.add(new JLabel("Tipo: " + tipoTiquete));
        ticketPanel.add(new JLabel("Fecha: " + LocalDateTime.now().toString()));

        if (qr != null) {
            JLabel qrLabel = new JLabel(new ImageIcon(qr));
            qrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ticketPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            ticketPanel.add(qrLabel);
        } else {
            ticketPanel.add(new JLabel("QR no disponible"));
        }

        frame.add(ticketPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
