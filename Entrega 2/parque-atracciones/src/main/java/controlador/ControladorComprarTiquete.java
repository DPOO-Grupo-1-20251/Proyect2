package controlador;

import vista.menu.ComprarTiqueteView;
import vista.menu.TicketViewer;

import java.awt.image.BufferedImage;

public class ControladorComprarTiquete {

    private final ComprarTiqueteView vista;

    public ControladorComprarTiquete() {
        vista = new ComprarTiqueteView();
        initListeners();
    }

    private void initListeners() {
        vista.addGenerarListener(e -> {
            String nombreUsuario = vista.getNombreUsuario();
            String atraccion = vista.getAtraccion();
            String tipoTiquete = vista.getTipoTiquete();

            // Aquí generarías el QR real. Por ahora es null.
            BufferedImage qr = null;

            // Mostrar el ticket con los datos recolectados
            TicketViewer.mostrarTiquete(nombreUsuario, atraccion, qr);
        });
    }
}
