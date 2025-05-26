package vista.menu;

import controlador.ControladorMenuPrincipal;

public class launcher {
    public static void main(String[] args) {
        ControladorMenuPrincipal controlador = new ControladorMenuPrincipal();
        new MainMenuView(controlador);
    }
}
