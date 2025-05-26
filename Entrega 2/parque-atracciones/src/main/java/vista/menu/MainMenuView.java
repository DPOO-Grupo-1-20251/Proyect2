package vista.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuView extends JFrame {

    public MainMenuView() {
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

        JButton btnComprar = new JButton("Comprar tiquete");
        JButton btnVerAtracciones = new JButton("Ver atracciones");
        JButton btnSalir = new JButton("Salir");

        btnComprar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerAtracciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Listeners
        btnComprar.addActionListener(e -> System.out.println("Comprar tiquete"));
        btnVerAtracciones.addActionListener(e -> System.out.println("Ver atracciones"));
        btnSalir.addActionListener(e -> System.exit(0));

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

    // Optional main method for test running
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenuView::new);
    }
}
