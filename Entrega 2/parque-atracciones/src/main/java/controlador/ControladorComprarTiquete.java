package controlador;

import vista.menu.ComprarTiqueteView;
import vista.menu.TicketViewer;
import utils.QRGenerator;

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

            String contenidoQR = nombreUsuario + " - " + atraccion + " - " + tipoTiquete;
            BufferedImage qr = QRGenerator.generateFakeQR(contenidoQR, 150);

            TicketViewer.mostrarTiquete(nombreUsuario, atraccion, tipoTiquete, qr);
        });
    }
}
