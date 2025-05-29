package vista.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ComprarTiqueteView extends JFrame {

    private JTextField txtNombreUsuario;
    private JTextField txtAtraccion;
    private JComboBox<String> comboTipoTiquete;
    private JButton btnGenerar;

    public ComprarTiqueteView() {
        setTitle("Comprar Tiquete");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nombre del Usuario:"));
        txtNombreUsuario = new JTextField();
        panel.add(txtNombreUsuario);

        panel.add(new JLabel("Atracción:"));
        txtAtraccion = new JTextField();
        panel.add(txtAtraccion);

        panel.add(new JLabel("Tipo de Tiquete:"));
        comboTipoTiquete = new JComboBox<>(new String[]{"Regular", "VIP", "Descuento"});
        panel.add(comboTipoTiquete);

        btnGenerar = new JButton("Generar Tiquete");
        panel.add(new JLabel()); // empty space
        panel.add(btnGenerar);

        add(panel);
        //setVisible(true);
    }

    public String getNombreUsuario() {
        return txtNombreUsuario.getText();
    }

    public String getAtraccion() {
        return txtAtraccion.getText();
    }

    public String getTipoTiquete() {
        return (String) comboTipoTiquete.getSelectedItem();
    }

    // ✅ THIS IS THE CRUCIAL METHOD YOUR CONTROLLER CALLS
    public void addGenerarListener(ActionListener listener) {
        btnGenerar.addActionListener(listener);
    }
}
