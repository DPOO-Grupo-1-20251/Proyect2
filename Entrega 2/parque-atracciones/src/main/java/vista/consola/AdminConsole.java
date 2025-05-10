// Manuel Villaveces (◣ ◢) KickAss
package vista.consola;

import dominio.usuario.Usuario;
import dominio.usuario.Cliente;
import dominio.tiquete.*;
import infraestructura.persistencia.TiqueteRepositoryJson;
import aplicacion.ServicioVentaTiquetes;
import dominio.elementoparque.*;
import dominio.excepciones.TiqueteInvalidoException;
import dominio.empleado.Capacitacion;
import dominio.util.CondicionClimatica;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

public class AdminConsole {

    private final ServicioVentaTiquetes servicio;
    private final Scanner scanner;

    // ✅ Constructor for integration
    public AdminConsole(ServicioVentaTiquetes servicio) {
        this.servicio = servicio;
        this.scanner = new Scanner(System.in);
    }

    // ✅ Standalone constructor
    public AdminConsole() {
        String archivoTiquetes = "tiquetes.json";
        TiqueteRepositoryJson repositorio = new TiqueteRepositoryJson(archivoTiquetes);
        this.servicio = new ServicioVentaTiquetes(repositorio, id -> buscarAtraccionPorId(id));
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        while (true) {
            System.out.println("\n----- MENÚ PRINCIPAL -----");
            System.out.println("1. Vender Tiquete General");
            System.out.println("2. Vender Tiquete Temporada");
            System.out.println("3. Vender Entrada Individual");
            System.out.println("4. Vender FastPass");
            System.out.println("5. Consultar Tiquetes por Usuario");
            System.out.println("6. Consultar Tiquetes por Fecha");
            System.out.println("7. Consultar Tiquetes por Tipo");
            System.out.println("8. Validar Acceso");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = leerOpcion();
            switch (opcion) {
                case 1: venderTiqueteGeneralDesdeConsole(); break;
                case 2: venderTiqueteTemporadaDesdeConsole(); break;
                case 3: venderEntradaIndividualDesdeConsole(); break;
                case 4: venderFastPassDesdeConsole(); break;
                case 5: consultarPorUsuarioDesdeConsole(); break;
                case 6: consultarPorFechaDesdeConsole(); break;
                case 7: consultarPorTipoDesdeConsole(); break;
                case 8: validarAccesoDesdeConsole(); break;
                case 9: System.out.println("¡Hasta luego!"); return;
                default: System.out.println("❌ Opción inválida.");
            }
        }
    }

    // === ✅ EXTRACTED PUBLIC METHODS ===

    public void venderTiqueteGeneralDesdeConsole() {
        Usuario usuario = solicitarUsuario();
        if (usuario == null) return;
        CategoriaTiquete categoria = solicitarCategoriaTiquete();
        if (categoria == null) return;
        Double precioBase = solicitarPrecioBase();
        if (precioBase == null) return;
        try {
            TiqueteGeneral tg = servicio.venderTiqueteGeneral(usuario, categoria, precioBase);
            System.out.println("✅ Tiquete general vendido: Código " + tg.getCodigo() + " | Precio $" + tg.getPrecio());
        } catch (Exception e) {
            System.out.println("❌ Error al vender el tiquete: " + e.getMessage());
        }
    }

    public void venderTiqueteTemporadaDesdeConsole() {
        Usuario usuario = solicitarUsuario();
        if (usuario == null) return;
        CategoriaTiquete categoria = solicitarCategoriaTiquete();
        if (categoria == null) return;
        System.out.print("Ingrese fecha de inicio (AAAA-MM-DD): ");
        LocalDate fechaInicio = leerFecha();
        if (fechaInicio == null) return;
        System.out.print("Ingrese fecha de fin (AAAA-MM-DD): ");
        LocalDate fechaFin = leerFecha();
        if (fechaFin == null) return;
        Double precioBase = solicitarPrecioBase();
        if (precioBase == null) return;
        try {
            TiqueteTemporada tt = servicio.venderTiqueteTemporada(usuario, categoria, fechaInicio.atStartOfDay(), fechaFin.atStartOfDay(), precioBase);
            System.out.println("✅ Tiquete temporada vendido: Código " + tt.getCodigo() + " | Precio $" + tt.getPrecio());
        } catch (Exception e) {
            System.out.println("❌ Error al vender el tiquete: " + e.getMessage());
        }
    }

    public void venderEntradaIndividualDesdeConsole() {
        Usuario usuario = solicitarUsuario();
        if (usuario == null) return;
        Atraccion atraccion = solicitarAtraccion();
        if (atraccion == null) return;
        Double precioBase = solicitarPrecioBase();
        if (precioBase == null) return;
        try {
            EntradaIndividual ei = servicio.venderEntradaIndividual(usuario, atraccion, precioBase);
            System.out.println("✅ Entrada individual vendida: Código " + ei.getCodigo() + " | Precio $" + ei.getPrecio());
        } catch (Exception e) {
            System.out.println("❌ Error al vender la entrada: " + e.getMessage());
        }
    }

    public void venderFastPassDesdeConsole() {
        Usuario usuario = solicitarUsuario();
        if (usuario == null) return;
        System.out.print("Ingrese fecha válida (AAAA-MM-DD): ");
        LocalDate fechaValida = leerFecha();
        if (fechaValida == null) return;
        Double precioBase = solicitarPrecioBase();
        if (precioBase == null) return;
        try {
            FastPass fp = servicio.venderFastPass(usuario, fechaValida.atStartOfDay(), precioBase);
            System.out.println("✅ FastPass vendido: Código " + fp.getCodigo() + " | Precio $" + fp.getPrecio());
        } catch (Exception e) {
            System.out.println("❌ Error al vender el FastPass: " + e.getMessage());
        }
    }

    public void consultarPorUsuarioDesdeConsole() {
        System.out.print("ID usuario: ");
        List<Tiquete> tiquetes = servicio.consultarTiquetesPorUsuario(scanner.nextLine());
        if (tiquetes.isEmpty()) {
            System.out.println("❌ No hay tiquetes.");
        } else {
            tiquetes.forEach(t -> System.out.println("- " + t.getCodigo() + " | $" + t.getPrecio() + " | " + t.getClass().getSimpleName()));
        }
    }

    public void consultarPorFechaDesdeConsole() {
        System.out.print("Fecha (AAAA-MM-DD): ");
        LocalDate fecha = leerFecha();
        if (fecha == null) return;
        List<Tiquete> tiquetes = servicio.consultarTiquetesPorFecha(fecha);
        if (tiquetes.isEmpty()) {
            System.out.println("❌ No hay tiquetes.");
        } else {
            tiquetes.forEach(t -> System.out.println("- " + t.getCodigo() + " | $" + t.getPrecio() + " | " + t.getClass().getSimpleName()));
        }
    }

    public void consultarPorTipoDesdeConsole() {
        System.out.println("1. General | 2. Temporada | 3. Individual | 4. FastPass");
        System.out.print("Opción: ");
        Class<? extends Tiquete> tipo = null;
        int op = leerOpcion();
        switch (op) {
            case 1: tipo = TiqueteGeneral.class; break;
            case 2: tipo = TiqueteTemporada.class; break;
            case 3: tipo = EntradaIndividual.class; break;
            case 4: tipo = FastPass.class; break;
            default:
                System.out.println("❌ Opción inválida.");
                return;
        }
        List<? extends Tiquete> tiquetes = servicio.consultarTiquetesPorTipo(tipo);
        if (tiquetes.isEmpty()) {
            System.out.println("❌ No se encontraron tiquetes.");
        } else {
            tiquetes.forEach(t -> System.out.println("- " + t.getCodigo() + " | $" + t.getPrecio() + " | " + t.getClass().getSimpleName()));
        }
    }

    public void validarAccesoDesdeConsole() {
        System.out.print("Código de tiquete: ");
        String codigo = scanner.nextLine();
        Tiquete tiquete = servicio.obtenerTiquetePorCodigo(codigo);
        if (tiquete == null) {
            System.out.println("❌ Tiquete no encontrado.");
            return;
        }
        Atraccion atraccion = solicitarAtraccion();
        if (atraccion == null) return;
        try {
            if (servicio.validarAccesoAtraccion(tiquete, atraccion)) {
                System.out.println("✅ Acceso PERMITIDO a " + atraccion.getNombre());
            }
        } catch (TiqueteInvalidoException e) {
            System.out.println("❌ Acceso denegado: " + e.getMessage());
        }
    }

    // === HELPERS ===
    private Usuario solicitarUsuario() {
        System.out.print("ID usuario: ");
        String id = scanner.nextLine();
        if (id.equals("123")) {
            return new Cliente("juan123", "pass123", "Juan Pérez", id, "juan@example.com", "555-1234", LocalDate.of(2000,1,1),1.75,70);
        }
        System.out.println("❌ Usuario no encontrado.");
        return null;
    }

    private CategoriaTiquete solicitarCategoriaTiquete() {
        System.out.println("Seleccione la categoría:");
        CategoriaTiquete[] values = CategoriaTiquete.values();
        for (int i = 0; i < values.length; i++) {
            System.out.println((i+1) + ". " + values[i].name());
        }
        int op = leerOpcion();
        if (op >= 1 && op <= values.length) {
            return values[op-1];
        }
        System.out.println("❌ Opción inválida.");
        return null;
    }

    private Double solicitarPrecioBase() {
        System.out.print("Precio base: ");
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Precio inválido.");
            return null;
        }
    }

    private LocalDate leerFecha() {
        try {
            return LocalDate.parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("❌ Fecha inválida.");
            return null;
        }
    }

    private Atraccion solicitarAtraccion() {
        System.out.print("ID atracción: ");
        String id = scanner.nextLine();
        Atraccion atr = buscarAtraccionPorId(id);
        if (atr == null) {
            System.out.println("❌ Atracción no encontrada.");
        }
        return atr;
    }

    private Atraccion buscarAtraccionPorId(String id) {
        if (id.equals("A1")) {
            return new AtraccionMecanica(id, "Montaña Rusa", "Zona A", 20, 2,
                    NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO, 1.2, 2.0,
                    50, 120, List.of("Cardiopatía"), List.of("Asma"),
                    List.of(CondicionClimatica.TORMENTA), Capacitacion.MONTAÑA_RUSA_A1);
        }
        return null;
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        new AdminConsole().mostrarMenu();
    }
}
