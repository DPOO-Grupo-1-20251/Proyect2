package controlador;

import vista.menu.MainMenuView;

public class ControladorMenuPrincipal {

    private MainMenuView mainMenu;

    public ControladorMenuPrincipal() {
        mainMenu = new MainMenuView(this); // Pass the controller to the view
        mainMenu.setVisible(true);
    }

    public void comprarTiquete() {
        System.out.println("Lógica para comprar tiquete");
        // Aquí puedes abrir una nueva vista o llamar a un servicio
    }

    public void verAtracciones() {
        System.out.println("Lógica para ver atracciones");
        // Aquí puedes abrir una nueva vista o cargar datos
    }

    public void salir() {
        System.out.println("Saliendo...");
        System.exit(0);
    }
}
