// ✅ Manuel Villaveces (◣ ◢) KickAss Games - MainMenuConsole FINAL

package vista.consola;

import aplicacion.ServicioGestionElementosParque;
import aplicacion.ServicioGestionEmpleados;
import aplicacion.ServicioVentaTiquetes;
import dominio.empleado.Empleado;
import dominio.empleado.OperarioAtraccion;
import dominio.usuario.Cliente;
import dominio.usuario.Usuario;
import infraestructura.persistencia.ClienteRepositoryJson;
import infraestructura.persistencia.ElementoParqueRepositoryJson;
import infraestructura.persistencia.EmpleadoRepositoryJson;
import infraestructura.persistencia.TiqueteRepositoryJson;
import vista.consola.AdminConsole;
import vista.consola.EmpleadoConsole;
import vista.consola.ClienteConsole;

import dominio.elementoparque.Atraccion;
import dominio.elementoparque.AtraccionMecanica;
import dominio.elementoparque.NivelExclusividad;
import dominio.elementoparque.NivelRiesgo;
import dominio.util.CondicionClimatica;
import dominio.empleado.Capacitacion;

import java.util.Scanner;

public class MainMenuConsole {

    private final ServicioVentaTiquetes servicio;
    private final ServicioGestionElementosParque servicioElementos;
    private final ServicioGestionEmpleados servicioEmpleados;
    private final ClienteRepositoryJson clienteRepo;
    private final TiqueteRepositoryJson repositorio;
    private final Scanner scanner = new Scanner(System.in);

    public MainMenuConsole() {
        this.repositorio = new TiqueteRepositoryJson("tiquetes.json");
        this.clienteRepo = new ClienteRepositoryJson("clientes.json");

        this.servicio = new ServicioVentaTiquetes(repositorio, this::buscarAtraccionPorId);

        ElementoParqueRepositoryJson repoElementos = new ElementoParqueRepositoryJson("elementos.json", "atracciones.json");
        EmpleadoRepositoryJson repoEmpleados = new EmpleadoRepositoryJson("empleados.json");

        this.servicioElementos = new ServicioGestionElementosParque(repoElementos);
        this.servicioEmpleados = new ServicioGestionEmpleados(repoEmpleados);
    }

    public void mostrarInicio() {
        System.out.println("\n=== BIENVENIDO AL PARQUE ===");
        System.out.println("1. Cliente");
        System.out.println("2. Empleado");
        System.out.println("3. Administrador");
        System.out.print("Seleccione su rol: ");

        int rol;
        try {
            rol = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
            return;
        }

        switch (rol) {
            case 1: clienteLogin(); break;
            case 2: empleadoLogin(); break;
            case 3:
                AdminConsole admin = new AdminConsole(servicio);
                admin.mostrarMenu();
                break;
            default:
                System.out.println("❌ Opción inválida.");
        }
    }

    private void clienteLogin() {
        System.out.print("Ingrese su ID de cliente: ");
        String idCliente = scanner.nextLine();
        Usuario cliente = buscarUsuarioPorId(idCliente);

        if (cliente instanceof Cliente) {
            new ClienteConsole(clienteRepo, servicio, (Cliente) cliente).mostrarMenuCliente();
        } else {
            System.out.println("❌ Cliente no encontrado.");
        }
    }

    private void empleadoLogin() {
        System.out.print("Ingrese su ID de empleado: ");
        String idEmpleado = scanner.nextLine();
        Empleado empleado = buscarEmpleadoPorId(idEmpleado);

        if (empleado != null) {
            new EmpleadoConsole(
                    empleado,
                    servicioElementos,
                    servicioEmpleados,
                    servicio
            ).mostrarMenuEmpleado();
        } else {
            System.out.println("❌ Empleado no encontrado.");
        }
    }

    private Usuario buscarUsuarioPorId(String id) {
        if (id.equals("123")) {
            return new Cliente(
                    "juan123", "pass123", "Juan Pérez", id,
                    "juan@example.com", "555-1234",
                    java.time.LocalDate.of(2000, 1, 1),
                    1.75, 70.0
            );
        }
        return null;
    }

    private Empleado buscarEmpleadoPorId(String id) {
        if (id.equals("999")) {
            return new OperarioAtraccion(
                    id,
                    "Carlos López",
                    "carlos@example.com",
                    "555-9999",
                    "emp999",
                    "pass999",
                    true,
                    java.util.List.of("Montaña Rusa")
            );
        }
        return null;
    }

    private Atraccion buscarAtraccionPorId(String idAtraccion) {
        if (idAtraccion.equals("A1")) {
            return new AtraccionMecanica(
                    idAtraccion,
                    "Montaña Rusa",
                    "Zona A",
                    20,
                    2,
                    NivelExclusividad.FAMILIAR,
                    NivelRiesgo.MEDIO,
                    1.2, 2.0,
                    50, 120,
                    java.util.List.of("Cardiopatía"),
                    java.util.List.of("Asma"),
                    java.util.List.of(CondicionClimatica.TORMENTA),
                    Capacitacion.MONTAÑA_RUSA_A1
            );
        }
        return null;
    }

    public static void main(String[] args) {
        new MainMenuConsole().mostrarInicio();
    }
}
