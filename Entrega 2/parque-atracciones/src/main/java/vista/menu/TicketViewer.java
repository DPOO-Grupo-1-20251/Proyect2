package vista.menu;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class TicketViewer {

    public static void mostrarTiquete(String nombreUsuario, String atraccion, BufferedImage qr) {
        JFrame frame = new JFrame("Tiquete generado");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        TicketPanel ticketPanel = new TicketPanel(nombreUsuario, atraccion, LocalDateTime.now(), qr);
        frame.add(ticketPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
