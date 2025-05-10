<<<<<<< HEAD:Entrega 2/parque-atracciones/src/main/java/vista/consola/EmpleadoConsole.java
// Manuel Villaveces (◣ ◢) KickAss
<<<<<<< HEAD:Entrega 2/parque-atracciones/src/main/java/vista/consola/EmpleadoConsole.java
package vista.consola;
=======
package presentacion;
>>>>>>> parent of 80522bc (Console Flawless):Entrega 2/parque-atracciones/src/main/java/presentacion/EmpleadoConsole.java
=======
package presentacion;
>>>>>>> parent of df82c3e (MVC vista consola):Entrega 2/parque-atracciones/src/main/java/presentacion/EmpleadoConsole.java

import aplicacion.ServicioVentaTiquetes;
import dominio.usuario.Usuario;

public class EmpleadoConsole {
    private final ServicioVentaTiquetes servicio;
    private final Usuario empleado;

    public EmpleadoConsole(ServicioVentaTiquetes servicio, Usuario empleado) {
        this.servicio = servicio;
        this.empleado = empleado;
    }

    public void mostrarMenuEmpleado() {
        System.out.println("\n Bienvenido, " + empleado.getNombre());
        System.out.println("Este es el menú del empleado (¡a implementar más adelante!)");
    }
}
