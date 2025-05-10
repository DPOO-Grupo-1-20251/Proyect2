// Manuel Villaveces (◣ ◢) KickAss.

package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dominio.empleado.Administrador;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Administrador administrador;

    public LoginWindow(Administrador administrador) {
        this.administrador = administrador;
        setTitle("Login Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Usuario:");
        usernameField = new JTextField();
        JLabel passLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.addActionListener(e -> login());

        // ✅ Make pressing ENTER in username field trigger login
        usernameField.addActionListener(e -> login());
        // ✅ Make pressing ENTER in password field trigger login
        passwordField.addActionListener(e -> login());

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // empty placeholder
        panel.add(loginButton);

        add(panel);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (administrador.getUsername().equals(username) && administrador.getPassword().equals(password)) {
            JOptionPane.showMessageDialog(this, "Acceso concedido.");
            new MainMenuWindow().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales inválidas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        Administrador admin = new Administrador("1001", "Admin", "admin@parque.com", "555-0000", "admin", "adminpass", null);
        LoginWindow window = new LoginWindow(admin);
        window.setVisible(true);
    }
}
