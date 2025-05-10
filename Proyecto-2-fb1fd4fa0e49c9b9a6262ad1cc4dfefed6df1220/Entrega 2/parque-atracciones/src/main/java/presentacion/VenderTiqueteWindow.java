// Manuel Villaveces (◣ ◢) KickAss
package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VenderTiqueteWindow extends JFrame {
    private JTextField nombreField;
    private JTextField idField;
    private JComboBox<String> tipoTiqueteCombo;
    private JTextField precioField;

    public VenderTiqueteWindow() {
        setTitle("Venta de Tiquetes");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Nombre Comprador:"));
        nombreField = new JTextField();
        panel.add(nombreField);

        panel.add(new JLabel("Identificación:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Tipo de Tiquete:"));
        tipoTiqueteCombo = new JComboBox<>(new String[]{
            "General", "Temporada", "FastPass", "Entrada Individual"
        });
        panel.add(tipoTiqueteCombo);

        panel.add(new JLabel("Precio Base:"));
        precioField = new JTextField();
        panel.add(precioField);

        JButton confirmarBtn = new JButton("Confirmar Venta");
        confirmarBtn.addActionListener(this::confirmarVenta);
        panel.add(confirmarBtn);

        JButton cancelarBtn = new JButton("Cancelar");
        cancelarBtn.addActionListener(e -> dispose());
        panel.add(cancelarBtn);

        add(panel);
    }

    private void confirmarVenta(ActionEvent e) {
        String nombre = nombreField.getText();
        String id = idField.getText();
        String tipo = (String) tipoTiqueteCombo.getSelectedItem();
        String precioStr = precioField.getText();

        if (nombre.isEmpty() || id.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            JOptionPane.showMessageDialog(this, "✅ Venta confirmada:\n" +
                    "Nombre: " + nombre + "\n" +
                    "ID: " + id + "\n" +
                    "Tipo: " + tipo + "\n" +
                    "Precio: $" + precio);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
