package vista.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ComprarTiqueteView extends JFrame {
    private JTextField nombreUsuarioField;
    private JComboBox<String> atraccionCombo;
    private JButton btnComprar;

    public ComprarTiqueteView() {
        setTitle("Comprar Tiquete");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nombre del usuario:"));
        nombreUsuarioField = new JTextField();
        panel.add(nombreUsuarioField);

        panel.add(new JLabel("Atracción:"));
        atraccionCombo = new JComboBox<>(new String[]{"Montaña Rusa", "Casa del Terror", "Carrusel"});
        panel.add(atraccionCombo);

        btnComprar = new JButton("Comprar");
        panel.add(new JLabel()); // Empty cell for alignment
        panel.add(btnComprar);

        add(panel);
    }

    public String getNombreUsuario() {
        return nombreUsuarioField.getText();
    }

    public String getAtraccionSeleccionada() {
        return (String) atraccionCombo.getSelectedItem();
    }

    public void setComprarAction(ActionListener listener) {
        btnComprar.addActionListener(listener);
    }
}
