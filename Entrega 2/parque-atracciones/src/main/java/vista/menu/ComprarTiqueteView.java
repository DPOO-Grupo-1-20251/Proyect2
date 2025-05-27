package vista.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controlador.ControladorMenuPrincipal;

public class ComprarTiqueteView extends JFrame {

    private ControladorMenuPrincipal controlador;

    public ComprarTiqueteView(ControladorMenuPrincipal controlador) {
        this.controlador = controlador;

        setTitle("Comprar Tiquete");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Tipo de usuario:"));
        JComboBox<String> tipoUsuario = new JComboBox<>(new String[] {"Cliente", "Empleado"});

        panel.add(tipoUsuario);
        panel.add(new JLabel("Tipo de tiquete:"));
        JComboBox<String> tipoTiquete = new JComboBox<>(new String[] {"Individual", "Grupal", "VIP"});

        panel.add(tipoTiquete);
        panel.add(new JLabel("Nombre:"));
        JTextField campoNombre = new JTextField();

        panel.add(campoNombre);
        panel.add(new JLabel("Cédula:"));
        JTextField campoCedula = new JTextField();

        panel.add(campoCedula);

        JButton comprarBtn = new JButton("Comprar");
        comprarBtn.addActionListener(e -> {
            // Aquí va la llamada al controlador para comprar
            JOptionPane.showMessageDialog(this, "Compra realizada (mock)");
        });

        panel.add(comprarBtn);

        add(panel);
    }
}
