package vista.menu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controlador.ControladorMenuPrincipal;

public class MainMenuView extends JFrame {

    private ControladorMenuPrincipal controlador;

    public MainMenuView(ControladorMenuPrincipal controlador) {
        this.controlador = controlador;

        setTitle("Parque de Atracciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center on screen

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titleLabel = new JLabel("Bienvenido al parque");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnComprar = new JButton("Comprar tiquete");
        JButton btnVerAtracciones = new JButton("Ver atracciones");
        JButton btnSalir = new JButton("Salir");

        btnComprar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerAtracciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action Listeners
        btnComprar.addActionListener(e -> controlador.comprarTiquete());
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
}
