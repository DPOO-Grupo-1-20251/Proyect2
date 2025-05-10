package aplicacion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dominio.tiquete.*;
import dominio.usuario.*;
import dominio.elementoparque.*;
import dominio.excepciones.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import dominio.empleado.Empleado;
import dominio.empleado.Cajero;
import dominio.empleado.Capacitacion;
import infraestructura.persistencia.TiqueteRepositoryJson;

class ServicioVentaTiquetesTest {

    @Test
    void testVentaTiquetes() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");

        AtraccionMecanica dummyAtraccion = new AtraccionMecanica(
            "A1", "Montaña Rusa", "Zona A", 10, 2,
            NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO,
            1.0, 2.0, 10, 60, List.of(), List.of(), List.of(),
            Capacitacion.OPERACION_ATRACCION_RIESGO_MEDIO
        );

        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> dummyAtraccion);

        Cliente cliente = new Cliente("cliuser", "pass", "Carlos", "C1", "carlos@mail.com", "555-0000",
                LocalDate.of(2000, 1, 1), 1.75, 70);

        Empleado empleado = new Cajero("E1", "Ana", "ana@mail.com", "555-1234", "anauser", "pass", 1, "Tienda1");

        AtraccionMecanica atraccion = new AtraccionMecanica(
            "A1", "Montaña Rusa", "Zona A", 20, 2,
            NivelExclusividad.ORO, NivelRiesgo.ALTO,
            1.2, 2.0, 30, 120, List.of(), List.of(), List.of(),
            Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO
        );

        // Venta de tiquete general
        TiqueteGeneral tg = servicio.venderTiqueteGeneral(cliente, CategoriaTiquete.ORO, 100.0);
        assertNotNull(tg);
        assertEquals(CategoriaTiquete.ORO, tg.getCategoria());

        // Venta de entrada individual
        EntradaIndividual ei = servicio.venderEntradaIndividual(cliente, dummyAtraccion, 50.0);
        assertNotNull(ei);
        assertEquals(dummyAtraccion.getId(), ei.getAtraccion().getId()); //  FIXED

        // Venta de fastpass
        FastPass fp = servicio.venderFastPass(empleado, LocalDateTime.now().plusDays(1), 30.0);
        assertNotNull(fp);
        assertTrue(fp.tieneDescuentoEmpleado());

        // Validación de tiquete
        assertTrue(servicio.validarTiquete(tg, LocalDateTime.now()));

        // Validación de acceso a atracción
        try {
            servicio.validarAccesoAtraccion(tg, atraccion);
        } catch (TiqueteInvalidoException e) {
            // Esperado si la categoría no es suficiente
        }

        // Registrar uso de tiquete
        servicio.registrarUsoTiquete(tg);
        assertTrue(tg.estaUtilizado()); //  FIXED
    }
}
