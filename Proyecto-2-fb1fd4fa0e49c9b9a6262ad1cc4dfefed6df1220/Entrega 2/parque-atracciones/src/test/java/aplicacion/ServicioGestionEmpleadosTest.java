package aplicacion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import infraestructura.persistencia.EmpleadoRepositoryJson;
import java.util.List;
import dominio.empleado.Empleado;
import dominio.empleado.Cajero;
import dominio.trabajo.LugarTrabajo;
import dominio.trabajo.Tienda;
import dominio.trabajo.Turno;
import dominio.trabajo.AsignacionTurno;
import dominio.empleado.Capacitacion;
import java.util.UUID;

/**
 * Prueba de integración para la persistencia de empleados.
 */
public class ServicioGestionEmpleadosTest {
    @Test
    void testGestionEmpleados() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        
        // Registro de empleado con identificacion única para evitar conflictos en persistencia
        String uniqueId = "T" + UUID.randomUUID().toString().replace("-", "");
        Empleado cajero = new Cajero(uniqueId, "Ana", "ana@mail.com", "555-1234", "anauser", "pass", 1, "Tienda1");
        Empleado registrado = servicio.registrarEmpleado(cajero);
        assertEquals("Ana", registrado.getNombre());
        
        // Consulta por identificación
        assertTrue(servicio.consultarEmpleadoPorIdentificacion(uniqueId).isPresent());
        
        // Asignación de turno
        LugarTrabajo tienda = new Tienda("Tienda1", 2);
        LocalDate fecha = LocalDate.now();
        Turno turno = Turno.APERTURA;
        AsignacionTurno asignacion = servicio.asignarTurno(uniqueId, tienda, fecha, turno);
        assertNotNull(asignacion);
        assertEquals(cajero, asignacion.getEmpleado());
        
        // Autorizar capacitación
        servicio.autorizarCapacitacion(uniqueId, Capacitacion.MANEJO_CAJA);
        assertTrue(cajero.tieneCapacitacion(Capacitacion.MANEJO_CAJA));
        
        // Verificar requisitos de personal
        boolean cumple = servicio.verificarRequisitosPersonalLugar(tienda, fecha, turno);
        assertTrue(cumple);
    }

    @Test
    public void testPersistenciaEmpleados() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String randomId = "E" + UUID.randomUUID().toString().replace("-", "");
        Empleado nuevo = new Cajero(randomId, "Test Cajero", "test@parque.com", "555-0000", "testuser", "clave", 2, "Taquilla Norte");
        servicio.registrarEmpleado(nuevo);
        ServicioGestionEmpleados servicio2 = new ServicioGestionEmpleados(repo);
        List<Empleado> empleados = servicio2.consultarTodosLosEmpleados();
        assertTrue(empleados.stream().anyMatch(e -> e.getIdentificacion().equals(randomId)));
        // Limpieza manual si es necesario
    }
}
