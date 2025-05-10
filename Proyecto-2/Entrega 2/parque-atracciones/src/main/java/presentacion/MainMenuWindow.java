// Manuel Villaveces (◣ ◢) KickAss
package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuWindow extends JFrame {
    private JButton[] menuButtons;
    private int selectedIndex = 0;

    public MainMenuWindow() {
        setTitle("Menú Principal - Parque de Atracciones");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        JLabel titulo = new JLabel("Menú Principal", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titulo);

        // Initialize buttons
        menuButtons = new JButton[4];
        menuButtons[0] = new JButton("🎟️ Vender Tiquete");
        menuButtons[1] = new JButton("🔍 Consultar Tiquetes");
        menuButtons[2] = new JButton("✅ Validar Acceso");
        menuButtons[3] = new JButton("🚪 Salir");

        // Add actions
        menuButtons[0].addActionListener(this::abrirVenderTiquete);
        menuButtons[1].addActionListener(this::abrirConsultarTiquetes);
        menuButtons[2].addActionListener(this::abrirValidarAcceso);
        menuButtons[3].addActionListener(e -> System.exit(0));

        for (JButton btn : menuButtons) {
            btn.setFocusable(false); // we handle focus ourselves
            panel.add(btn);
        }

        add(panel);

        // Highlight the first button
        highlightButton(0);

        // Enable keyboard input
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        moveSelection(-1);
                        break;
                    case KeyEvent.VK_DOWN:
                        moveSelection(1);
                        break;
                    case KeyEvent.VK_ENTER:
                        menuButtons[selectedIndex].doClick();
                        break;
                }
            }
        });
    }

    private void highlightButton(int index) {
        for (int i = 0; i < menuButtons.length; i++) {
            if (i == index) {
                menuButtons[i].setBackground(Color.LIGHT_GRAY);
            } else {
                menuButtons[i].setBackground(UIManager.getColor("Button.background"));
            }
        }
    }

    private void moveSelection(int delta) {
        selectedIndex += delta;
        if (selectedIndex < 0) selectedIndex = menuButtons.length - 1;
        if (selectedIndex >= menuButtons.length) selectedIndex = 0;
        highlightButton(selectedIndex);
    }

    private void abrirVenderTiquete(ActionEvent e) {
        new VenderTiqueteWindow().setVisible(true);
    }

    private void abrirConsultarTiquetes(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidad de consulta aún no implementada.");
    }

    private void abrirValidarAcceso(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidad de validación aún no implementada.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuWindow().setVisible(true));
    }
}
