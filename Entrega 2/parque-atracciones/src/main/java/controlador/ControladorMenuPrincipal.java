package controlador;

import vista.menu.MainMenuView;
import vista.menu.ComprarTiqueteView;

import javax.swing.*;

public class ControladorMenuPrincipal {

    private MainMenuView vista;

    public ControladorMenuPrincipal() {
        vista = new MainMenuView(this); // <- make sure your MainMenuView constructor takes this!
        initListeners();
    }

    private void initListeners() {
        vista.getBtnComprar().addActionListener(e -> abrirComprarTiquete());
        vista.getBtnVerAtracciones().addActionListener(e -> verAtracciones());
        vista.getBtnSalir().addActionListener(e -> salir());
    }

    // ✅ This opens the ComprarTiqueteView GUI
    public void abrirComprarTiquete() {
        new ComprarTiqueteView();
    }

    public void verAtracciones() {
        JOptionPane.showMessageDialog(vista, "Aquí irá la vista de atracciones");
    }

    public void salir() {
        int confirm = JOptionPane.showConfirmDialog(vista, "¿Estás seguro que quieres salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
