package vista;

import aplicacion.*;
import infraestructura.persistencia.*;
import dominio.elementoparque.*;
import dominio.empleado.*;
import dominio.tiquete.*;
import dominio.usuario.Cliente;
import dominio.trabajo.Turno;
import dominio.trabajo.LugarTrabajo;
import java.util.Scanner;
import java.util.Optional;

/**
 * Consola principal para la interacción con el sistema del parque de diversiones.
 *
 * <b>Permite:</b>
 * <ul>
 *   <li>Login de usuarios (administrador, empleado, cliente).</li>
 *   <li>Gestión completa de atracciones, empleados y tiquetes.</li>
 *   <li>Consultas avanzadas, actualización y eliminación de datos.</li>
 *   <li>Venta y consulta de tiquetes, asignación de turnos.</li>
 * </ul>
 *
 * <b>Precondiciones:</b> Los datos de entrada deben ser válidos.
 * <b>Poscondiciones:</b> Permite demostrar todas las funcionalidades del sistema.
 * 
 * @author Sistema Parque
 */
public class ConsolaParque {
    /**
     * Punto de entrada principal de la consola.
     *
     * <b>Precondiciones:</b> Ninguna.
     * <b>Poscondiciones:</b> Permite la interacción con el usuario.
     *
     * @param args Argumentos de línea de comandos (no usados).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Define base names for directories/files within the 'data' folder
        // CORRECTED: Use the actual directory name "elementos.json" where the files reside
        String elementosDirName = "elementos.json"; // Directory containing mecanicas.json, culturales.json
        String espectaculosFileName = "atracciones.json"; // File for espectaculos (consider renaming?)
        String empleadosFileName = "empleados.json";
        String tiquetesFileName = "tiquetes.json";

        try {
            // Initialize repositories using base names - JsonUtil handles path resolution
            // Pass the directory name for elements and the file name for espectaculos
            ElementoParqueRepositoryJson repoElementos = new ElementoParqueRepositoryJson(elementosDirName, espectaculosFileName);
            EmpleadoRepositoryJson repoEmpleados = new EmpleadoRepositoryJson(empleadosFileName);
            TiqueteRepositoryJson repoTiquetes = new TiqueteRepositoryJson(tiquetesFileName);

            // Initialize services
            ServicioGestionElementosParque servicioElementos = new ServicioGestionElementosParque(repoElementos);
            ServicioGestionEmpleados servicioEmpleados = new ServicioGestionEmpleados(repoEmpleados);
            // Pass the attraction resolver lambda to the Tiquete service constructor
            ServicioVentaTiquetes servicioTiquetes = new ServicioVentaTiquetes(repoTiquetes, id -> servicioElementos.consultarAtraccionPorId(id).orElse(null));

            System.out.println("Bienvenido al sistema del Parque de Diversiones");

            // --- Main Loop ---
            while (true) {
                System.out.println("\nSeleccione una opción:");
                System.out.println("1. Login");
                System.out.println("2. Salir");
                String op = sc.nextLine();
                if (op.equals("2")) break;
                if (op.equals("1")) {
                    System.out.print("Ingrese su identificación: ");
                    String id = sc.nextLine();
                    System.out.print("Ingrese su contraseña: ");
                    String pass = sc.nextLine();
                    Optional<Empleado> emp = servicioEmpleados.consultarEmpleadoPorIdentificacion(id);
                    if (emp.isPresent() && emp.get().verificarPassword(pass)) {
                        menuEmpleado(emp.get(), sc, servicioElementos, servicioEmpleados, servicioTiquetes);
                    } else {
                        System.out.println("Login fallido. Intente de nuevo.");
                    }
                } else {
                     System.out.println("Opción no válida.");
                }
            }

            System.out.println("Gracias por usar el sistema.");

        } catch (Exception e) {
            // Catch potential initialization errors (e.g., root dir not found)
            System.err.println("Error fatal durante la inicialización o ejecución: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            System.out.println("El sistema no pudo iniciar correctamente. Saliendo.");
        } finally {
             sc.close(); // Close scanner in finally block
        }
    }

    /**
     * Menú principal para empleados y administradores.
     *
     * <b>Precondiciones:</b> El empleado debe estar autenticado.
     * <b>Poscondiciones:</b> Permite acceder a todas las funcionalidades según el rol.
     *
     * @param emp Empleado autenticado.
     * @param sc Scanner para entrada de usuario.
     * @param servicioElementos Servicio de gestión de elementos.
     * @param servicioEmpleados Servicio de gestión de empleados.
     * @param servicioTiquetes Servicio de venta de tiquetes.
     */
    private static void menuEmpleado(Empleado emp, Scanner sc, ServicioGestionElementosParque servicioElementos, ServicioGestionEmpleados servicioEmpleados, ServicioVentaTiquetes servicioTiquetes) {
        System.out.println("Bienvenido, " + emp.getNombre() + " (" + emp.getClass().getSimpleName() + ")");
        boolean esAdmin = emp instanceof Administrador;
        while (true) {
            System.out.println("\nOpciones:");
            System.out.println("1. Ver atracciones");
            System.out.println("2. Ver empleados");
            System.out.println("3. Vender tiquete");
            System.out.println("4. Consultar tiquetes por usuario");
            if (esAdmin) {
                System.out.println("5. Crear empleado");
                System.out.println("6. Crear atracción cultural");
                System.out.println("7. Crear atracción mecánica");
                System.out.println("8. Crear espectáculo");
                System.out.println("9. Actualizar atracción");
                System.out.println("10. Eliminar atracción");
                System.out.println("11. Actualizar empleado");
                System.out.println("12. Eliminar empleado");
                System.out.println("13. Asignar turno");
                System.out.println("14. Consultas avanzadas");
            }
            System.out.println((esAdmin ? "15" : "5") + ". Salir");
            String op = sc.nextLine();
            if (op.equals(esAdmin ? "15" : "5")) break;
            try {
                switch (op) {
                    case "1":
                        servicioElementos.consultarTodasLasAtracciones().forEach(a -> System.out.println(a.getId() + ": " + a.getNombre()));
                        break;
                    case "2":
                        servicioEmpleados.consultarTodosLosEmpleados().forEach(e -> System.out.println(e.getIdentificacion() + ": " + e.getNombre()));
                        break;
                    case "3":
                        venderTiquete(sc, servicioElementos, servicioTiquetes);
                        break;
                    case "4":
                        System.out.print("ID usuario: ");
                        String idU = sc.nextLine();
                        servicioTiquetes.consultarTiquetesPorUsuario(idU).forEach(t -> System.out.println(t.getCodigo() + ": " + t.getClass().getSimpleName()));
                        break;
                    case "5":
                        if (esAdmin) crearEmpleado(sc, servicioEmpleados);
                        break;
                    case "6":
                        if (esAdmin) crearAtraccionCultural(sc, servicioElementos);
                        break;
                    case "7":
                        if (esAdmin) crearAtraccionMecanica(sc, servicioElementos);
                        break;
                    case "8":
                        if (esAdmin) crearEspectaculo(sc, servicioElementos);
                        break;
                    case "9":
                        if (esAdmin) actualizarAtraccion(sc, servicioElementos);
                        break;
                    case "10":
                        if (esAdmin) eliminarAtraccion(sc, servicioElementos);
                        break;
                    case "11":
                        if (esAdmin) actualizarEmpleado(sc, servicioEmpleados);
                        break;
                    case "12":
                        if (esAdmin) eliminarEmpleado(sc, servicioEmpleados);
                        break;
                    case "13":
                        if (esAdmin) asignarTurno(sc, servicioElementos, servicioEmpleados);
                        break;
                    case "14":
                        if (esAdmin) consultasAvanzadas(sc, servicioElementos);
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    /**
     * Permite crear un nuevo empleado desde la consola.
     */
    private static void crearEmpleado(Scanner sc, ServicioGestionEmpleados servicioEmpleados) {
        System.out.print("Tipo (Cajero/Cocinero/OperarioAtraccion/ServicioGeneral/Administrador): ");
        String tipoEmp = sc.nextLine();
        System.out.print("ID: ");
        String idE = sc.nextLine();
        System.out.print("Nombre: ");
        String nomE = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Teléfono: ");
        String telefono = sc.nextLine();
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String passE = sc.nextLine();
        Empleado nuevo = null;
        switch (tipoEmp) {
            case "Cajero":
                System.out.print("Caja asignada: ");
                int caja = Integer.parseInt(sc.nextLine());
                System.out.print("Punto de venta: ");
                String puntoVenta = sc.nextLine();
                nuevo = new Cajero(idE, nomE, email, telefono, username, passE, caja, puntoVenta);
                break;
            case "Cocinero":
                System.out.print("Especialidad: ");
                String esp = sc.nextLine();
                nuevo = new Cocinero(idE, nomE, email, telefono, username, passE, esp);
                break;
            case "OperarioAtraccion":
                System.out.print("Certificado de seguridad (true/false): ");
                boolean certificado = Boolean.parseBoolean(sc.nextLine());
                System.out.print("IDs atracciones capacitadas (coma): ");
                java.util.List<String> atrs = java.util.Arrays.asList(sc.nextLine().split(","));
                nuevo = new OperarioAtraccion(idE, nomE, email, telefono, username, passE, certificado, atrs);
                break;
            case "ServicioGeneral":
                nuevo = new ServicioGeneral(idE, nomE, email, telefono, username, passE);
                break;
            case "Administrador":
                System.out.print("Áreas de responsabilidad (coma): ");
                String areas = sc.nextLine();
                nuevo = new Administrador(idE, nomE, email, telefono, username, passE, java.util.Arrays.asList(areas.split(",")));
                break;
        }
        if (nuevo != null) {
            servicioEmpleados.registrarEmpleado(nuevo);
            System.out.println("Empleado creado.");
        }
    }

    /**
     * Permite crear una nueva atracción cultural desde la consola.
     */
    private static void crearAtraccionCultural(Scanner sc, ServicioGestionElementosParque servicioElementos) {
        System.out.print("ID: ");
        String idA = sc.nextLine();
        System.out.print("Nombre: ");
        String nomA = sc.nextLine();
        System.out.print("Ubicación: ");
        String ubi = sc.nextLine();
        System.out.print("Cupo máximo: ");
        int cupo = Integer.parseInt(sc.nextLine());
        System.out.print("Empleados mínimos: ");
        int minEmp = Integer.parseInt(sc.nextLine());
        System.out.print("Edad mínima: ");
        int edadMin = Integer.parseInt(sc.nextLine());
        System.out.print("Nivel exclusividad (FAMILIAR/ORO/DIAMANTE): ");
        NivelExclusividad ne = NivelExclusividad.valueOf(sc.nextLine());
        AtraccionCultural ac = servicioElementos.crearAtraccionCultural(idA, nomA, ubi, cupo, minEmp, edadMin, java.util.Collections.emptyList(), ne);
        System.out.println("Atracción cultural creada: " + ac.getId());
    }

    /**
     * Permite crear una nueva atracción mecánica desde la consola.
     */
    private static void crearAtraccionMecanica(Scanner sc, ServicioGestionElementosParque servicioElementos) {
        System.out.print("ID: ");
        String idA = sc.nextLine();
        System.out.print("Nombre: ");
        String nomA = sc.nextLine();
        System.out.print("Ubicación: ");
        String ubi = sc.nextLine();
        System.out.print("Cupo máximo: ");
        int cupo = Integer.parseInt(sc.nextLine());
        System.out.print("Empleados mínimos: ");
        int minEmp = Integer.parseInt(sc.nextLine());
        System.out.print("Nivel exclusividad (FAMILIAR/ORO/DIAMANTE): ");
        NivelExclusividad ne = NivelExclusividad.valueOf(sc.nextLine());
        System.out.print("Nivel riesgo (MEDIO/ALTO): ");
        NivelRiesgo nr = NivelRiesgo.valueOf(sc.nextLine());
        System.out.print("Altura mínima: ");
        double altMin = Double.parseDouble(sc.nextLine());
        System.out.print("Altura máxima: ");
        double altMax = Double.parseDouble(sc.nextLine());
        System.out.print("Peso mínimo: ");
        double pesoMin = Double.parseDouble(sc.nextLine());
        System.out.print("Peso máximo: ");
        double pesoMax = Double.parseDouble(sc.nextLine());
        System.out.print("Contraindicaciones (coma): ");
        java.util.List<String> contra = java.util.Arrays.asList(sc.nextLine().split(","));
        System.out.print("Restricciones (coma): ");
        java.util.List<String> restr = java.util.Arrays.asList(sc.nextLine().split(","));
        Capacitacion cap = Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO;
        AtraccionMecanica am = servicioElementos.crearAtraccionMecanica(idA, nomA, ubi, cupo, minEmp, ne, nr, altMin, altMax, pesoMin, pesoMax, contra, restr, java.util.Collections.emptyList(), cap);
        System.out.println("Atracción mecánica creada: " + am.getId());
    }

    /**
     * Permite crear un nuevo espectáculo desde la consola.
     */
    private static void crearEspectaculo(Scanner sc, ServicioGestionElementosParque servicioElementos) {
        System.out.print("ID: ");
        String idE = sc.nextLine();
        System.out.print("Nombre: ");
        String nomE = sc.nextLine();
        System.out.print("Ubicación: ");
        String ubi = sc.nextLine();
        System.out.print("Cupo máximo: ");
        int cupo = Integer.parseInt(sc.nextLine());
        System.out.print("Descripción: ");
        String desc = sc.nextLine();
        System.out.print("Cantidad de horarios: ");
        int nHor = Integer.parseInt(sc.nextLine());
        java.util.List<dominio.util.RangoFechaHora> horarios = new java.util.ArrayList<>();
        for (int i = 0; i < nHor; i++) {
            System.out.print("Inicio (YYYY-MM-DDTHH:MM:SS): ");
            java.time.LocalDateTime ini = java.time.LocalDateTime.parse(sc.nextLine());
            System.out.print("Fin (YYYY-MM-DDTHH:MM:SS): ");
            java.time.LocalDateTime fin = java.time.LocalDateTime.parse(sc.nextLine());
            horarios.add(new dominio.util.RangoFechaHora(ini, fin));
        }
        Espectaculo esp = servicioElementos.crearEspectaculo(idE, nomE, ubi, cupo, desc, horarios, java.util.Collections.emptyList());
        System.out.println("Espectáculo creado: " + esp.getId());
    }

    /**
     * Permite actualizar una atracción existente desde la consola.
     */
    private static void actualizarAtraccion(Scanner sc, ServicioGestionElementosParque servicioElementos) {
        System.out.print("ID de la atracción a actualizar: ");
        String id = sc.nextLine();
        Optional<Atraccion> atrOpt = servicioElementos.consultarAtraccionPorId(id);
        if (atrOpt.isEmpty()) {
            System.out.println("Atracción no encontrada.");
            return;
        }
        Atraccion atr = atrOpt.get();
        System.out.print("Nuevo nombre (actual: " + atr.getNombre() + "): ");
        String nombre = sc.nextLine();
        if (!nombre.isEmpty()) atr.setNombre(nombre);
        servicioElementos.actualizarAtraccion(atr);
        System.out.println("Atracción actualizada.");
    }

    /**
     * Permite eliminar una atracción desde la consola.
     */
    private static void eliminarAtraccion(Scanner sc, ServicioGestionElementosParque servicioElementos) {
        System.out.print("ID de la atracción a eliminar: ");
        String id = sc.nextLine();
        servicioElementos.eliminarAtraccion(id);
        System.out.println("Atracción eliminada.");
    }

    /**
     * Permite actualizar un empleado existente desde la consola.
     */
    private static void actualizarEmpleado(Scanner sc, ServicioGestionEmpleados servicioEmpleados) {
        System.out.print("ID del empleado a actualizar: ");
        String id = sc.nextLine();
        Optional<Empleado> empOpt = servicioEmpleados.consultarEmpleadoPorIdentificacion(id);
        if (empOpt.isEmpty()) {
            System.out.println("Empleado no encontrado.");
            return;
        }
        Empleado emp = empOpt.get();
        System.out.print("Nuevo nombre (actual: " + emp.getNombre() + "): ");
        String nombre = sc.nextLine();
        if (!nombre.isEmpty()) emp.setNombre(nombre);
        servicioEmpleados.actualizarEmpleado(emp);
        System.out.println("Empleado actualizado.");
    }

    /**
     * Permite eliminar un empleado desde la consola.
     */
    private static void eliminarEmpleado(Scanner sc, ServicioGestionEmpleados servicioEmpleados) {
        System.out.print("ID del empleado a eliminar: ");
        String id = sc.nextLine();
        servicioEmpleados.eliminarEmpleado(id);
        System.out.println("Empleado eliminado.");
    }

    /**
     * Permite asignar un turno a un empleado desde la consola.
     */
    private static void asignarTurno(Scanner sc, ServicioGestionElementosParque servicioElementos, ServicioGestionEmpleados servicioEmpleados) {
        System.out.print("ID empleado: ");
        String idE = sc.nextLine();
        System.out.print("Fecha (YYYY-MM-DD): ");
        java.time.LocalDate fecha = java.time.LocalDate.parse(sc.nextLine());
        System.out.print("Turno (APERTURA/CIERRE): ");
        Turno turno = Turno.valueOf(sc.nextLine());
        System.out.print("Lugar trabajo (ID o vacío para ServicioGeneral): ");
        String idL = sc.nextLine();
        LugarTrabajo lugar = null;
        if (!idL.isEmpty()) {
            lugar = servicioElementos.consultarAtraccionPorId(idL).orElse(null);
        }
        servicioEmpleados.asignarTurno(idE, lugar, fecha, turno);
        System.out.println("Turno asignado.");
    }

    /**
     * Permite realizar consultas avanzadas desde la consola.
     */
    private static void consultasAvanzadas(Scanner sc, ServicioGestionElementosParque servicioElementos) {
        System.out.println("Consultas avanzadas:");
        System.out.println("1. Atracciones por exclusividad");
        System.out.println("2. Atracciones mecánicas por riesgo");
        System.out.println("3. Elementos restringidos por clima");
        String op = sc.nextLine();
        switch (op) {
            case "1":
                System.out.print("Nivel exclusividad (FAMILIAR/ORO/DIAMANTE): ");
                NivelExclusividad ne = NivelExclusividad.valueOf(sc.nextLine());
                servicioElementos.consultarAtraccionesPorExclusividad(ne).forEach(a -> System.out.println(a.getId() + ": " + a.getNombre()));
                break;
            case "2":
                System.out.print("Nivel riesgo (MEDIO/ALTO): ");
                NivelRiesgo nr = NivelRiesgo.valueOf(sc.nextLine());
                servicioElementos.consultarAtraccionesMecanicasPorRiesgo(nr).forEach(a -> System.out.println(a.getId() + ": " + a.getNombre()));
                break;
            case "3":
                System.out.print("Clima (LLUVIA/TORMENTA/FRIO/CALOR): ");
                dominio.util.CondicionClimatica clima = dominio.util.CondicionClimatica.valueOf(sc.nextLine());
                servicioElementos.consultarElementosPorClima(clima).forEach(e -> System.out.println(e.getId() + ": " + e.getNombre()));
                break;
        }
    }

    /**
     * Permite vender un tiquete desde la consola.
     */
    private static void venderTiquete(Scanner sc, ServicioGestionElementosParque servicioElementos, ServicioVentaTiquetes servicioTiquetes) {
        try {
            System.out.print("ID comprador: ");
            String idC = sc.nextLine();
            if (idC == null || idC.trim().isEmpty()) {
                System.out.println("[ERROR] El ID del comprador no puede estar vacío o ser nulo. Abortando venta de tiquete.");
                return;
            }
            System.out.print("Nombre comprador: ");
            String nomC = sc.nextLine();
            System.out.print("Email comprador: ");
            String emailC = sc.nextLine();
            if (emailC == null || emailC.trim().isEmpty()) {
                emailC = idC + "@test.com";
            }
            System.out.print("Tipo de tiquete (general/temporada/individual/fastpass): ");
            String tipo = sc.nextLine();
            Cliente comprador = new Cliente(idC, nomC, nomC, idC, emailC, "", java.time.LocalDate.now(), 1.7, 70);
            if (tipo.equals("general")) {
                System.out.print("Categoría (FAMILIAR/ORO/DIAMANTE/BASICO): ");
                String catStr = sc.nextLine();
                CategoriaTiquete cat = CategoriaTiquete.valueOf(catStr);
                System.out.print("Precio: ");
                String precioStr = sc.nextLine();
                double precio = Double.parseDouble(precioStr);
                TiqueteGeneral tg = servicioTiquetes.venderTiqueteGeneral(comprador, cat, precio);
                System.out.println("Tiquete vendido: " + tg.getCodigo());
            } else if (tipo.equals("temporada")) {
                System.out.print("Categoría (FAMILIAR/ORO/DIAMANTE): ");
                String catStr = sc.nextLine();
                CategoriaTiquete cat = CategoriaTiquete.valueOf(catStr);
                System.out.print("Fecha de inicio (YYYY-MM-DDTHH:MM:SS): ");
                String iniStr = sc.nextLine();
                java.time.LocalDateTime ini = java.time.LocalDateTime.parse(iniStr);
                System.out.print("Fecha de fin (YYYY-MM-DDTHH:MM:SS): ");
                String finStr = sc.nextLine();
                java.time.LocalDateTime fin = java.time.LocalDateTime.parse(finStr);
                System.out.print("Precio: ");
                String precioStr = sc.nextLine();
                double precio = Double.parseDouble(precioStr);
                TiqueteTemporada tt = servicioTiquetes.venderTiqueteTemporada(comprador, cat, ini, fin, precio);
                System.out.println("Tiquete vendido: " + tt.getCodigo());
            } else if (tipo.equals("individual")) {
                System.out.print("ID de la atracción: ");
                String idA = sc.nextLine();
                Atraccion atr = servicioElementos.consultarAtraccionPorId(idA).orElse(null);
                if (atr == null) {
                    System.out.println("Atracción no encontrada.");
                    return;
                }
                System.out.print("Precio: ");
                String precioStr = sc.nextLine();
                double precio = Double.parseDouble(precioStr);
                EntradaIndividual ei = servicioTiquetes.venderEntradaIndividual(comprador, atr, precio);
                System.out.println("Tiquete vendido: " + ei.getCodigo());
            } else if (tipo.equals("fastpass")) {
                System.out.print("Fecha válida (YYYY-MM-DDTHH:MM:SS): ");
                String fechaStr = sc.nextLine();
                java.time.LocalDateTime fecha = java.time.LocalDateTime.parse(fechaStr);
                System.out.print("Precio: ");
                String precioStr = sc.nextLine();
                double precio = Double.parseDouble(precioStr);
                FastPass fp = servicioTiquetes.venderFastPass(comprador, fecha, precio);
                System.out.println("Tiquete vendido: " + fp.getCodigo());
            }
        } catch (Exception ex) {
            System.out.println("[ERROR] " + ex.getClass().getName() + ": " + ex.getMessage());
            ex.printStackTrace(System.out);
            throw ex;
        }
    }
}
