// Manuel Villaveces (◣ ◢) KickAss
<<<<<<< HEAD:Entrega 2/parque-atracciones/src/main/java/vista/consola/AdminConsole.java
<<<<<<< HEAD:Entrega 2/parque-atracciones/src/main/java/vista/consola/AdminConsole.java
package vista.consola;
=======

package presentacion;
>>>>>>> parent of 80522bc (Console Flawless):Entrega 2/parque-atracciones/src/main/java/presentacion/AdminConsole.java
=======
package presentacion;
>>>>>>> parent of df82c3e (MVC vista consola):Entrega 2/parque-atracciones/src/main/java/presentacion/AdminConsole.java

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
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class AdminConsole {

    private final ServicioVentaTiquetes servicio;
    private final Scanner scanner = new Scanner(System.in);

    public AdminConsole() {
        String archivoTiquetes = "tiquetes.json";
        TiqueteRepositoryJson repositorio = new TiqueteRepositoryJson(archivoTiquetes);

        this.servicio = new ServicioVentaTiquetes(repositorio, id -> {
            System.out.println("Buscando atracción con ID: " + id);
            return buscarAtraccionPorId(id);
        });
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

            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1: venderTiqueteGeneral(); break;
                case 2: venderTiqueteTemporada(); break;
                case 3: venderEntradaIndividual(); break;
                case 4: venderFastPass(); break;
                case 5: consultarPorUsuario(); break;
                case 6: consultarPorFecha(); break;
                case 7: consultarPorTipo(); break;
                case 8: validarAcceso(); break;
                case 9:
                    System.out.println("¡Hasta luego!");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void venderTiqueteGeneral() {
        System.out.println("\n=== Venta de Tiquete General ===");
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

    private void venderTiqueteTemporada() {
        System.out.println("\n=== Venta de Tiquete Temporada ===");
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

    private void venderEntradaIndividual() {
        System.out.println("\n=== Venta de Entrada Individual ===");
        Usuario usuario = solicitarUsuario();
        if (usuario == null) return;

        Atraccion atraccion = solicitarAtraccion();
        if (atraccion == null) return;

        Double precioBase = solicitarPrecioBase();
        if (precioBase == null) return;

        try {
            EntradaIndividual entrada = servicio.venderEntradaIndividual(usuario, atraccion, precioBase);
            System.out.println("✅ Entrada individual vendida: Código " + entrada.getCodigo() + " | Precio $" + entrada.getPrecio());
        } catch (Exception e) {
            System.out.println("❌ Error al vender la entrada: " + e.getMessage());
        }
    }

    private void venderFastPass() {
        System.out.println("\n=== Venta de FastPass ===");
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

    private Usuario solicitarUsuario() {
        System.out.print("Ingrese el ID del usuario: ");
        String idUsuario = scanner.nextLine();
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            System.out.println("❌ Usuario no encontrado.");
        }
        return usuario;
    }

    private CategoriaTiquete solicitarCategoriaTiquete() {
        System.out.println("Seleccione la categoría:");
        for (CategoriaTiquete cat : CategoriaTiquete.values()) {
            System.out.println((cat.ordinal() + 1) + ". " + cat.name());
        }
        System.out.print("Opción: ");
        try {
            int op = Integer.parseInt(scanner.nextLine());
            if (op < 1 || op > CategoriaTiquete.values().length) throw new Exception();
            return CategoriaTiquete.values()[op - 1];
        } catch (Exception e) {
            System.out.println("❌ Opción inválida.");
            return null;
        }
    }

    private Double solicitarPrecioBase() {
        System.out.print("Ingrese el precio base: ");
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

    private Usuario buscarUsuarioPorId(String idUsuario) {
        if (idUsuario.equals("123")) {
            return new Cliente("juan123", "pass123", "Juan Pérez", idUsuario, "juan@example.com", "555-1234", LocalDate.of(2000, 1, 1), 1.75, 70.0);
        }
        return null;
    }

    private Atraccion solicitarAtraccion() {
        System.out.print("Ingrese el ID de la atracción: ");
        String idAtraccion = scanner.nextLine();
        Atraccion atraccion = buscarAtraccionPorId(idAtraccion);
        if (atraccion == null) {
            System.out.println("❌ Atracción no encontrada.");
        }
        return atraccion;
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
                List.of("Cardiopatía"),
                List.of("Asma"),
                List.of(CondicionClimatica.TORMENTA),
                Capacitacion.MONTAÑA_RUSA_A1
            );
        }
        return null;
    }

    private void consultarPorUsuario() {
        System.out.println("\n=== Consultar Tiquetes por Usuario ===");
        System.out.print("ID usuario: ");
        List<Tiquete> tiquetes = servicio.consultarTiquetesPorUsuario(scanner.nextLine());
        if (tiquetes.isEmpty()) {
            System.out.println("❌ No hay tiquetes.");
        } else {
            tiquetes.forEach(t -> System.out.println("- " + t.getCodigo() + " | $" + t.getPrecio() + " | " + t.getClass().getSimpleName()));
        }
    }

    private void consultarPorFecha() {
        System.out.println("\n=== Consultar Tiquetes por Fecha ===");
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

    private void consultarPorTipo() {
        System.out.println("\n=== Consultar Tiquetes por Tipo ===");
        System.out.println("1. General | 2. Temporada | 3. Individual | 4. FastPass");
        System.out.print("Opción: ");
        Class<? extends Tiquete> tipo = null;
        try {
            int op = Integer.parseInt(scanner.nextLine());
            switch (op) {
                case 1: tipo = TiqueteGeneral.class; break;
                case 2: tipo = TiqueteTemporada.class; break;
                case 3: tipo = EntradaIndividual.class; break;
                case 4: tipo = FastPass.class; break;
                default:
                    System.out.println("❌ Opción fuera de rango.");
                    return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida.");
            return;
        }

        List<? extends Tiquete> tiquetes = servicio.consultarTiquetesPorTipo(tipo);
        if (tiquetes.isEmpty()) {
            System.out.println("❌ No se encontraron tiquetes de este tipo.");
        } else {
            tiquetes.forEach(t -> System.out.println("- " + t.getCodigo() + " | $" + t.getPrecio() + " | " + t.getClass().getSimpleName()));
        }
    }

    private void validarAcceso() {
        System.out.println("\n=== Validar Acceso a Atracción ===");
        System.out.print("Ingrese el código del tiquete: ");
        String codigoTiquete = scanner.nextLine();

        Tiquete tiquete = servicio.obtenerTiquetePorCodigo(codigoTiquete);
        if (tiquete == null) {
            System.out.println("❌ No se encontró un tiquete con ese código.");
            return;
        }

        Atraccion atraccion = solicitarAtraccion();
        if (atraccion == null) return;

        try {
            boolean acceso = servicio.validarAccesoAtraccion(tiquete, atraccion);
            if (acceso) {
                System.out.println("✅ Acceso PERMITIDO a " + atraccion.getNombre());
            }
        } catch (TiqueteInvalidoException e) {
            System.out.println("❌ Acceso denegado: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AdminConsole().mostrarMenu();
    }
}
