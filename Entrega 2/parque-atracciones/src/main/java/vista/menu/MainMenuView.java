package vista.menu;

import javax.swing.*;
import java.awt.*;
import controlador.ControladorMenuPrincipal;

public class MainMenuView extends JFrame {

    private JButton btnComprar;
    private JButton btnVerAtracciones;
    private JButton btnSalir;

    public MainMenuView(ControladorMenuPrincipal controlador) {
        setTitle("Parque de Atracciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // center on screen

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titleLabel = new JLabel("Bienvenido al parque");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnComprar = new JButton("Comprar tiquete");
        btnVerAtracciones = new JButton("Ver atracciones");
        btnSalir = new JButton("Salir");

        btnComprar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerAtracciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Connect buttons to controller
        btnComprar.addActionListener(e -> controlador.abrirComprarTiquete());
        btnVerAtracciones.addActionListener(e -> controlador.verAtracciones());
        btnSalir.addActionListener(e -> controlador.salir());

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(btnComprar);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(btnVerAtracciones);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(btnSalir);

        add(mainPanel);
        setVisible(true);
    }

    public JButton getBtnComprar() {
        return btnComprar;
    }

    public JButton getBtnVerAtracciones() {
        return btnVerAtracciones;
    }

    public JButton getBtnSalir() {
        return btnSalir;
    }
}
