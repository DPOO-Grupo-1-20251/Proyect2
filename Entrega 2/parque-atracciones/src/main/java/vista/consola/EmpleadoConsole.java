// Manuel Villaveces (◣ ◢) KickAss
package vista.consola;

import aplicacion.ServicioGestionElementosParque;
import aplicacion.ServicioGestionEmpleados;
import aplicacion.ServicioVentaTiquetes;
import dominio.empleado.Empleado;
import dominio.tiquete.Tiquete;
import dominio.tiquete.CategoriaTiquete;
import dominio.usuario.Cliente;

import java.util.List;
import java.util.Scanner;

public class EmpleadoConsole {

    private final Empleado empleado;
    private final ServicioGestionElementosParque servicioElementos;
    private final ServicioGestionEmpleados servicioEmpleados;
    private final ServicioVentaTiquetes servicioTiquetes;
    private final AdminConsole adminConsole;
    private final Scanner scanner = new Scanner(System.in);

    public EmpleadoConsole(Empleado empleado,
                           ServicioGestionElementosParque servicioElementos,
                           ServicioGestionEmpleados servicioEmpleados,
                           ServicioVentaTiquetes servicioTiquetes) {
        this.empleado = empleado;
        this.servicioElementos = servicioElementos;
        this.servicioEmpleados = servicioEmpleados;
        this.servicioTiquetes = servicioTiquetes;
        this.adminConsole = new AdminConsole(servicioTiquetes); // ✅ allow delegation
    }

    public void mostrarMenuEmpleado() {
        System.out.println("\nBienvenido, " + empleado.getNombre() + " (" + empleado.getClass().getSimpleName() + ")");
        boolean esAdmin = empleado instanceof dominio.empleado.Administrador;

        while (true) {
            System.out.println("\n=== MENÚ EMPLEADO ===");
            System.out.println("1. Ver atracciones");
            System.out.println("2. Ver empleados");
            System.out.println("3. Vender tiquete general");
            System.out.println("4. Consultar tiquetes por usuario");
            if (esAdmin) {
                System.out.println("5. Vender tiquete temporada");
                System.out.println("6. Vender entrada individual");
                System.out.println("7. Vender fastpass");
                System.out.println("8. Consultar tiquetes por fecha");
                System.out.println("9. Consultar tiquetes por tipo");
                System.out.println("10. Validar acceso a atracción");
                System.out.println("11. Salir");
            } else {
                System.out.println("5. Salir");
            }

            System.out.print("Seleccione opción: ");
            String op = scanner.nextLine();

            if (op.equals(esAdmin ? "11" : "5")) break;

            try {
                switch (op) {
                    case "1":
                        if (servicioElementos != null) {
                            servicioElementos.consultarTodasLasAtracciones()
                                    .forEach(a -> System.out.println(a.getId() + ": " + a.getNombre()));
                        } else {
                            System.out.println("❌ Funcionalidad no disponible.");
                        }
                        break;
                    case "2":
                        if (servicioEmpleados != null) {
                            servicioEmpleados.consultarTodosLosEmpleados()
                                    .forEach(e -> System.out.println(e.getIdentificacion() + ": " + e.getNombre()));
                        } else {
                            System.out.println("❌ Funcionalidad no disponible.");
                        }
                        break;
                    case "3":
                        venderTiqueteGeneral();
                        break;
                    case "4":
                        consultarTiquetesUsuario();
                        break;
                    case "5":
                        if (esAdmin) adminConsole.venderTiqueteTemporadaDesdeConsole();
                        else System.out.println("❌ Opción inválida.");
                        break;
                    case "6":
                        if (esAdmin) adminConsole.venderEntradaIndividualDesdeConsole();
                        else System.out.println("❌ Opción inválida.");
                        break;
                    case "7":
                        if (esAdmin) adminConsole.venderFastPassDesdeConsole();
                        else System.out.println("❌ Opción inválida.");
                        break;
                    case "8":
                        if (esAdmin) adminConsole.consultarPorFechaDesdeConsole();
                        else System.out.println("❌ Opción inválida.");
                        break;
                    case "9":
                        if (esAdmin) adminConsole.consultarPorTipoDesdeConsole();
                        else System.out.println("❌ Opción inválida.");
                        break;
                    case "10":
                        if (esAdmin) adminConsole.validarAccesoDesdeConsole();
                        else System.out.println("❌ Opción inválida.");
                        break;
                    default:
                        System.out.println("❌ Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }

    private void venderTiqueteGeneral() {
        System.out.print("ID usuario: ");
        String idU = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        Cliente cliente = new Cliente(idU, nombre, nombre, idU, idU + "@mail.com", "000",
                java.time.LocalDate.now(), 1.7, 70);
        System.out.println("Seleccione categoría:");
        CategoriaTiquete[] cats = CategoriaTiquete.values();
        for (int i = 0; i < cats.length; i++) {
            System.out.println((i + 1) + ". " + cats[i].name());
        }
        System.out.print("Opción: ");
        int catIndex;
        try {
            catIndex = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Categoría inválida.");
            return;
        }
        if (catIndex < 1 || catIndex > cats.length) {
            System.out.println("❌ Categoría fuera de rango.");
            return;
        }
        CategoriaTiquete categoria = cats[catIndex - 1];

        System.out.print("Precio: ");
        double precio;
        try {
            precio = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Precio inválido.");
            return;
        }

        Tiquete t = servicioTiquetes.venderTiqueteGeneral(cliente, categoria, precio);
        System.out.println("✅ Tiquete vendido: " + t.getCodigo());
    }

    private void consultarTiquetesUsuario() {
        System.out.print("ID usuario: ");
        String idU = scanner.nextLine();
        List<Tiquete> lista = servicioTiquetes.consultarTiquetesPorUsuario(idU);
        if (lista.isEmpty()) {
            System.out.println("❌ No hay tiquetes.");
        } else {
            lista.forEach(t -> System.out.println(t.getCodigo() + ": " + t.getClass().getSimpleName()));
        }
    }
}
