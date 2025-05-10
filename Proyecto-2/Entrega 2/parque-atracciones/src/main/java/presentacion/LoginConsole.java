// ✅ Manuel Villaveces (◣ ◢) KickAss Games - LoginConsole FINAL

package vista.consola;

import aplicacion.ServicioVentaTiquetes;
import dominio.empleado.Administrador;
import dominio.empleado.OperarioAtraccion;
import dominio.empleado.Empleado;
import dominio.usuario.Cliente;
import infraestructura.persistencia.AdministradorRepositoryJson;
import infraestructura.persistencia.ClienteRepositoryJson;
import infraestructura.persistencia.EmpleadoRepositoryJson;
import infraestructura.persistencia.TiqueteRepositoryJson;

import java.time.LocalDate;
import java.util.Scanner;

public class LoginConsole {
    private final AdministradorRepositoryJson adminRepo;
    private final EmpleadoRepositoryJson empleadoRepo;
    private final ClienteRepositoryJson clienteRepo;
    private final ServicioVentaTiquetes servicioTiquetes;
    private final Scanner scanner = new Scanner(System.in);

    public LoginConsole(
            AdministradorRepositoryJson adminRepo,
            EmpleadoRepositoryJson empleadoRepo,
            ClienteRepositoryJson clienteRepo,
            ServicioVentaTiquetes servicioTiquetes
    ) {
        this.adminRepo = adminRepo;
        this.empleadoRepo = empleadoRepo;
        this.clienteRepo = clienteRepo;
        this.servicioTiquetes = servicioTiquetes;
    }

    public void mostrarLogin() {
        while (true) {
            System.out.println("\n=== BIENVENIDO AL PARQUE ===");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear nuevo usuario");
            System.out.println("3. Salir");
            System.out.print("Seleccione opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1": iniciarSesion(); break;
                case "2": crearNuevoUsuario(); break;
                case "3": System.out.println("👋 Hasta luego."); return;
                default: System.out.println("❌ Opción inválida.");
            }
        }
    }

    private void iniciarSesion() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Buscar en administradores
        Administrador admin = adminRepo.buscarAdministradorPorUsername(username);
        if (admin != null && admin.authenticate(username, password)) {
            System.out.println("✅ Bienvenido Administrador " + admin.getNombre());
            new AdminConsole(servicioTiquetes).mostrarMenu();
            return;
        }

        // Buscar en empleados
        Empleado empleado = empleadoRepo.buscarEmpleadoPorUsername(username);
        if (empleado != null) {
            System.out.println("✅ Bienvenido Empleado " + empleado.getNombre());
            new EmpleadoConsole(
                    empleado,
                    null, // servicioElementos si necesitas
                    null, // servicioEmpleados si necesitas
                    servicioTiquetes
            ).mostrarMenuEmpleado();
            return;
        }

        // Buscar en clientes
        Cliente cliente = clienteRepo.buscarClientePorUsername(username);
        if (cliente != null) {
            System.out.println("✅ Bienvenido Cliente " + cliente.getNombre());
            new ClienteConsole(clienteRepo, servicioTiquetes, cliente).mostrarMenuCliente();
            return;
        }

        System.out.println("❌ Usuario no encontrado o credenciales incorrectas.");
    }

    private void crearNuevoUsuario() {
        System.out.println("\n=== CREAR NUEVO USUARIO ===");
        System.out.println("1. Administrador");
        System.out.println("2. Empleado");
        System.out.println("3. Cliente");
        System.out.print("Seleccione tipo de usuario: ");
        String tipo = scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        // Verificar si el username ya existe
        if (adminRepo.buscarAdministradorPorUsername(username) != null ||
            empleadoRepo.buscarEmpleadoPorUsername(username) != null ||
            clienteRepo.buscarClientePorUsername(username) != null) {
            System.out.println("❌ Ya existe un usuario con ese username.");
            return;
        }

        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        System.out.print("Contraseña (⚠️ cualquier texto): ");
        String password = scanner.nextLine();

        System.out.print("Identificación: ");
        String identificacion = scanner.nextLine();

        switch (tipo) {
            case "1":
                Administrador nuevoAdmin = new Administrador(
                        identificacion, nombre, email, telefono,
                        username, password,
                        java.util.List.of("General")
                );
                adminRepo.agregarAdministrador(nuevoAdmin);
                System.out.println("✅ Administrador creado.");
                break;
            case "2":
                OperarioAtraccion nuevoEmp = new OperarioAtraccion(
                        identificacion, nombre, email, telefono,
                        username, password,
                        false,
                        java.util.List.of()
                );
                empleadoRepo.agregarEmpleado(nuevoEmp);
                System.out.println("✅ Empleado creado.");
                break;
            case "3":
                System.out.print("Fecha nacimiento (YYYY-MM-DD): ");
                LocalDate fechaNac;
                try { fechaNac = LocalDate.parse(scanner.nextLine()); }
                catch (Exception e) { System.out.println("❌ Fecha inválida."); return; }

                System.out.print("Altura (m): ");
                double altura = Double.parseDouble(scanner.nextLine());

                System.out.print("Peso (kg): ");
                double peso = Double.parseDouble(scanner.nextLine());

                Cliente nuevoCli = new Cliente(
                        username, password, nombre, identificacion,
                        email, telefono, fechaNac, altura, peso
                );
                clienteRepo.agregarCliente(nuevoCli);
                System.out.println("✅ Cliente creado.");
                break;
            default:
                System.out.println("❌ Tipo de usuario inválido.");
        }
    }

    // ✅ MAIN METHOD
    public static void main(String[] args) {
        AdministradorRepositoryJson adminRepo = new AdministradorRepositoryJson("administradores.json");
        EmpleadoRepositoryJson empleadoRepo = new EmpleadoRepositoryJson("empleados.json");
        ClienteRepositoryJson clienteRepo = new ClienteRepositoryJson("clientes.json");
        TiqueteRepositoryJson tiqueteRepo = new TiqueteRepositoryJson("tiquetes.json");

        ServicioVentaTiquetes servicioTiquetes = new ServicioVentaTiquetes(tiqueteRepo, id -> null); // replace null for atracción lookup if needed

        LoginConsole console = new LoginConsole(adminRepo, empleadoRepo, clienteRepo, servicioTiquetes);
        console.mostrarLogin();
    }
}
