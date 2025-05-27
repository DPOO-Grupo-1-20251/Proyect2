package vista.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class TicketPanel extends JPanel {
    private final String nombreUsuario;
    private final String nombreAtraccion;
    private final LocalDateTime fecha;
    private final BufferedImage qrImage;

    public TicketPanel(String nombreUsuario, String nombreAtraccion, LocalDateTime fecha, BufferedImage qrImage) {
        this.nombreUsuario = nombreUsuario;
        this.nombreAtraccion = nombreAtraccion;
        this.fecha = fecha;
        this.qrImage = qrImage;
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.LIGHT_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Ticket background
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(30, 30, 340, 240, 30, 30);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2d.drawString("Tiquete de entrada", 130, 60);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2d.drawString("Nombre: " + nombreUsuario, 50, 100);
        g2d.drawString("Atracción: " + nombreAtraccion, 50, 130);
        g2d.drawString("Fecha: " + fecha.toString(), 50, 160);

        // Draw QR
        if (qrImage != null) {
            g2d.drawImage(qrImage, 250, 170, 100, 100, this);
        }
    }
}
