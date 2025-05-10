// ✅ Manuel Villaveces (◣ ◢) KickAss Games
package vista.consola;

import dominio.usuario.Cliente;
import dominio.tiquete.Tiquete;
import infraestructura.persistencia.ClienteRepositoryJson;
import aplicacion.ServicioVentaTiquetes;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ClienteConsole {
    private final ClienteRepositoryJson clienteRepo;
    private final ServicioVentaTiquetes servicioTiquetes;
    private final Cliente clienteLogueado;
    private final Scanner scanner = new Scanner(System.in);

    // ✅ New constructor matching LoginConsole
    public ClienteConsole(ClienteRepositoryJson clienteRepo, ServicioVentaTiquetes servicioTiquetes, Cliente clienteLogueado) {
        this.clienteRepo = clienteRepo;
        this.servicioTiquetes = servicioTiquetes;
        this.clienteLogueado = clienteLogueado;
    }

    public void mostrarMenuCliente() {
        while (true) {
            System.out.println("\n=== MENÚ CLIENTE === Bienvenido " + clienteLogueado.getNombre());
            System.out.println("1. Ver mis tiquetes");
            System.out.println("2. Ver todos los clientes");
            System.out.println("3. Crear nuevo cliente");
            System.out.println("4. Salir");
            System.out.print("Seleccione opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    verMisTiquetes();
                    break;
                case "2":
                    mostrarTodosLosClientes();
                    break;
                case "3":
                    crearNuevoCliente();
                    break;
                case "4":
                    System.out.println("👋 Hasta luego.");
                    return;
                default:
                    System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void verMisTiquetes() {
        System.out.println("\n=== MIS TIQUETES ===");
        List<Tiquete> tiquetes = servicioTiquetes.consultarTiquetesPorUsuario(clienteLogueado.getIdentificacion());
        if (tiquetes.isEmpty()) {
            System.out.println("No tienes tiquetes registrados.");
        } else {
            for (Tiquete t : tiquetes) {
                System.out.println("- " + t.getCodigo() + " | $" + t.getPrecio() + " | " + t.getClass().getSimpleName());
            }
        }
    }

    private void mostrarTodosLosClientes() {
        System.out.println("\n=== LISTA DE CLIENTES ===");
        for (Cliente c : clienteRepo.cargarClientes()) {
            System.out.println("- " + c.getIdentificacion() + " | " + c.getNombre() + " | " + c.getEmail());
        }
    }

    private void crearNuevoCliente() {
        System.out.println("\n=== CREAR NUEVO CLIENTE ===");
        System.out.print("Identificación: ");
        String id = scanner.nextLine();

        Cliente existente = clienteRepo.buscarClientePorIdentificacion(id);
        if (existente != null) {
            System.out.println("❌ Ya existe un cliente con esa identificación.");
            return;
        }

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        LocalDate fechaNacimiento;
        try {
            fechaNacimiento = LocalDate.parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("❌ Fecha inválida.");
            return;
        }

        System.out.print("Altura (metros): ");
        double altura;
        try {
            altura = Double.parseDouble(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("❌ Altura inválida.");
            return;
        }

        System.out.print("Peso (kg): ");
        double peso;
        try {
            peso = Double.parseDouble(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("❌ Peso inválido.");
            return;
        }

        Cliente nuevo = new Cliente(username, "defaultpass", nombre, id, email, telefono, fechaNacimiento, altura, peso);
        List<Cliente> clientesActuales = clienteRepo.cargarClientes();
        clientesActuales.add(nuevo);
        clienteRepo.guardarClientes(clientesActuales);

        System.out.println("✅ Cliente creado exitosamente: " + nuevo.getNombre());
    }
}
