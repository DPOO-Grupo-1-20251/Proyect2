package controlador;

import vista.menu.MainMenuView;

import javax.swing.*;

public class ControladorMenuPrincipal {

    private MainMenuView vista;

    public ControladorMenuPrincipal() {
        // 👇 Pass controller instance to the view
        vista = new MainMenuView(this);
        initListeners();
    }

    private void initListeners() {
        vista.getBtnComprar().addActionListener(e -> abrirComprarTiquete());
        vista.getBtnVerAtracciones().addActionListener(e -> verAtracciones());
        vista.getBtnSalir().addActionListener(e -> salir());
    }

    // 👇 Make these public so the view can call them
    public void abrirComprarTiquete() {
        JOptionPane.showMessageDialog(vista, "Aquí irá el menú de compra de tiquetes");
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
