package aplicacion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dominio.elementoparque.*;
import dominio.util.CondicionClimatica;
import dominio.empleado.Capacitacion;
import java.util.*;

class DummyElementoParqueRepository implements ElementoParqueRepository {
    private final Map<String, Atraccion> atracciones = new HashMap<>();
    private final Map<String, Espectaculo> espectaculos = new HashMap<>();
    @Override public Atraccion save(Atraccion atraccion) { atracciones.put(atraccion.getId(), atraccion); return atraccion; }
    @Override public Espectaculo save(Espectaculo espectaculo) { espectaculos.put(espectaculo.getId(), espectaculo); return espectaculo; }
    @Override public Optional<Atraccion> findAtraccionById(String id) { return Optional.ofNullable(atracciones.get(id)); }
    @Override public Optional<Espectaculo> findEspectaculoById(String id) { return Optional.ofNullable(espectaculos.get(id)); }
    @Override public Optional<ElementoParque> findById(String id) { return Optional.ofNullable(atracciones.containsKey(id) ? atracciones.get(id) : espectaculos.get(id)); }
    @Override public Optional<Atraccion> findAtraccionByNombre(String nombre) { return atracciones.values().stream().filter(a -> a.getNombre().equals(nombre)).findFirst(); }
    @Override public Optional<Espectaculo> findEspectaculoByNombre(String nombre) { return espectaculos.values().stream().filter(e -> e.getNombre().equals(nombre)).findFirst(); }
    @Override public Optional<ElementoParque> findByNombre(String nombre) { return findAtraccionByNombre(nombre).map(a -> (ElementoParque)a).or(() -> findEspectaculoByNombre(nombre).map(e -> (ElementoParque)e)); }
    @Override public List<Atraccion> findAllAtracciones() { return new ArrayList<>(atracciones.values()); }
    @Override public List<ElementoParque> findAll() { List<ElementoParque> l = new ArrayList<>(); l.addAll(atracciones.values()); l.addAll(espectaculos.values()); return l; }
    @Override public void deleteById(String id) { atracciones.remove(id); espectaculos.remove(id); }
    @Override public List<AtraccionMecanica> findAllMecanicas() { return atracciones.values().stream().filter(a -> a instanceof AtraccionMecanica).map(a -> (AtraccionMecanica)a).toList(); }
    @Override public List<AtraccionCultural> findAllCulturales() { return atracciones.values().stream().filter(a -> a instanceof AtraccionCultural).map(a -> (AtraccionCultural)a).toList(); }
    @Override public List<Espectaculo> findAllEspectaculos() { return new ArrayList<>(espectaculos.values()); }
}

class ServicioGestionElementosParqueTest {
    @Test
    void testGestionElementos() {
        ElementoParqueRepository repo = new DummyElementoParqueRepository();
        ServicioGestionElementosParque servicio = new ServicioGestionElementosParque(repo);

        // Datos de prueba para atracción mecánica
        String id = "A1";
        String nombre = "Montaña Rusa";
        String ubicacion = "Zona A";
        int cupoMaximo = 20;
        int empleadosMinimos = 2;
        NivelExclusividad exclusividad = NivelExclusividad.ORO;
        NivelRiesgo riesgo = NivelRiesgo.ALTO;
        double alturaMin = 1.2, alturaMax = 2.0, pesoMin = 30, pesoMax = 120;
        List<String> contraindicaciones = List.of("Cardiopatía");
        List<String> restricciones = List.of("Vértigo");
        List<CondicionClimatica> clima = List.of();
        Capacitacion cap = Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO;

        AtraccionMecanica atraccion = new AtraccionMecanica(id, nombre, ubicacion, cupoMaximo, empleadosMinimos,
                exclusividad, riesgo, alturaMin, alturaMax, pesoMin, pesoMax, contraindicaciones, restricciones, clima, cap);
        repo.save(atraccion);

        // Crear atracción mecánica
        AtraccionMecanica creada = servicio.crearAtraccionMecanica(id, nombre, ubicacion, cupoMaximo, empleadosMinimos,
                exclusividad, riesgo, alturaMin, alturaMax, pesoMin, pesoMax, contraindicaciones, restricciones, clima, cap);
        assertNotNull(creada);
        assertEquals(nombre, creada.getNombre());

        // Consultar por nombre
        Optional<Atraccion> encontrada = servicio.consultarAtraccionPorNombre(nombre);
        assertTrue(encontrada.isPresent());
        assertEquals(nombre, encontrada.get().getNombre());

        // Datos de prueba para espectáculo
        String idEsp = "E1";
        String nombreEsp = "Show de Magia";
        String ubicacionEsp = "Plaza Central";
        int cupoEsp = 100;
        String descripcion = "Magia para toda la familia";
        // Proveer al menos un horario válido
        List<dominio.util.RangoFechaHora> horarios = List.of(
            new dominio.util.RangoFechaHora(
                java.time.LocalDateTime.of(2025, 4, 14, 10, 0),
                java.time.LocalDateTime.of(2025, 4, 14, 12, 0)
            )
        );
        Espectaculo espectaculo = new Espectaculo(idEsp, nombreEsp, ubicacionEsp, cupoEsp, descripcion, horarios, clima);
        repo.save(espectaculo);

        // Crear espectáculo
        Espectaculo creadoEsp = servicio.crearEspectaculo(idEsp, nombreEsp, ubicacionEsp, cupoEsp, descripcion, horarios, clima);
        assertNotNull(creadoEsp);
        assertEquals(nombreEsp, creadoEsp.getNombre());

        // Consultar espectáculo por nombre
        Optional<Espectaculo> encontradoEsp = servicio.consultarEspectaculoPorNombre(nombreEsp);
        assertTrue(encontradoEsp.isPresent());
        assertEquals(nombreEsp, encontradoEsp.get().getNombre());
    }
}
