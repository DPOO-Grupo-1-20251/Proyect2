// Manuel Villaveces (◣ ◢) KickAss

package dominio.elementoparque;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dominio.empleado.Capacitacion;
import dominio.empleado.Empleado;
import dominio.empleado.OperarioAtraccion;
import dominio.excepciones.CapacitacionInsuficienteException;
import dominio.excepciones.DatosInvalidosException;
import dominio.util.CondicionClimatica;

public class AtraccionMecanica extends Atraccion {
    private final NivelRiesgo nivelRiesgo;
    private double restriccionAlturaMinima;
    private double restriccionAlturaMaxima;
    private double restriccionPesoMinimo;
    private double restriccionPesoMaximo;
    private List<String> contraindicacionesSalud;
    private List<String> restriccionesSalud;
    private final Capacitacion capacitacionEspecifica;

    public AtraccionMecanica(String id, String nombre, String ubicacion,
                             int cupoMaximo, int empleadosMinimos,
                             NivelExclusividad nivelExclusividad, NivelRiesgo nivelRiesgo,
                             double restriccionAlturaMinima, double restriccionAlturaMaxima,
                             double restriccionPesoMinimo, double restriccionPesoMaximo,
                             List<String> contraindicacionesSalud,
                             List<String> restriccionesSalud,
                             List<CondicionClimatica> climaNoPermitido,
                             Capacitacion capacitacionEspecifica) {
        super(id, nombre, ubicacion, cupoMaximo, empleadosMinimos);

        if (nivelExclusividad == null) throw new DatosInvalidosException("El nivel de exclusividad no puede ser null.");
        if (nivelRiesgo == null) throw new DatosInvalidosException("El nivel de riesgo no puede ser null.");
        if (restriccionAlturaMinima < 0 || restriccionAlturaMinima >= restriccionAlturaMaxima) throw new DatosInvalidosException("Restricción de altura inválida.");
        if (restriccionPesoMinimo < 0 || restriccionPesoMinimo >= restriccionPesoMaximo) throw new DatosInvalidosException("Restricción de peso inválida.");
        if (contraindicacionesSalud == null) throw new DatosInvalidosException("La lista de contraindicaciones no puede ser null.");
        if (restriccionesSalud == null) throw new DatosInvalidosException("La lista de restricciones de salud no puede ser null.");
        if (nivelRiesgo == NivelRiesgo.ALTO && capacitacionEspecifica == null) throw new DatosInvalidosException("Se requiere una capacitación específica para atracciones de riesgo ALTO.");
        if (climaNoPermitido == null) throw new DatosInvalidosException("La lista de clima no permitido no puede ser null.");

        super.setNivelExclusividad(nivelExclusividad);
        this.nivelRiesgo = nivelRiesgo;
        this.restriccionAlturaMinima = restriccionAlturaMinima;
        this.restriccionAlturaMaxima = restriccionAlturaMaxima;
        this.restriccionPesoMinimo = restriccionPesoMinimo;
        this.restriccionPesoMaximo = restriccionPesoMaximo;
        this.contraindicacionesSalud = new ArrayList<>(contraindicacionesSalud);
        this.restriccionesSalud = new ArrayList<>(restriccionesSalud);
        this.capacitacionEspecifica = capacitacionEspecifica;
        super.setClimaNoPermitido(climaNoPermitido);

        Set<Capacitacion> capacitaciones = new HashSet<>();
        capacitaciones.add(Capacitacion.CERTIFICADO_SEGURIDAD_ATRACCIONES);
        capacitaciones.add(Capacitacion.PRIMEROS_AUXILIOS);
        if (nivelRiesgo == NivelRiesgo.ALTO) {
            capacitaciones.add(Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO);
            if (this.capacitacionEspecifica != null) capacitaciones.add(this.capacitacionEspecifica);
        } else if (nivelRiesgo == NivelRiesgo.MEDIO) {
            capacitaciones.add(Capacitacion.OPERACION_ATRACCION_RIESGO_MEDIO);
        }
        super.setCapacitacionesRequeridas(capacitaciones);
    }

    // ✅ ACCESSORS for validation logic
    @Override
    public double getAlturaMinima() { return restriccionAlturaMinima; }

    @Override
    public double getAlturaMaxima() { return restriccionAlturaMaxima; }

    @Override
    public double getPesoMinimo() { return restriccionPesoMinimo; }

    @Override
    public double getPesoMaximo() { return restriccionPesoMaximo; }

    // ✅ ADD literal getters for Mapper compatibility
    public double getRestriccionAlturaMinima() { return restriccionAlturaMinima; }

    public double getRestriccionAlturaMaxima() { return restriccionAlturaMaxima; }

    public double getRestriccionPesoMinimo() { return restriccionPesoMinimo; }

    public double getRestriccionPesoMaximo() { return restriccionPesoMaximo; }

    public NivelRiesgo getNivelRiesgo() { return nivelRiesgo; }

    public List<String> getContraindicacionesSalud() { return List.copyOf(contraindicacionesSalud); }

    public List<String> getRestriccionesSalud() { return List.copyOf(restriccionesSalud); }

    public Capacitacion getCapacitacionEspecifica() { return capacitacionEspecifica; }

    public void setRestriccionAlturaMinima(double alturaMinima) {
        if (alturaMinima < 0) throw new DatosInvalidosException("La altura mínima no puede ser negativa.");
        if (alturaMinima >= getRestriccionAlturaMaxima()) throw new DatosInvalidosException("La altura mínima debe ser menor que la máxima.");
        this.restriccionAlturaMinima = alturaMinima;
    }

    public void setRestriccionAlturaMaxima(double alturaMaxima) {
        if (alturaMaxima <= getRestriccionAlturaMinima()) throw new DatosInvalidosException("La altura máxima debe ser mayor que la mínima.");
        this.restriccionAlturaMaxima = alturaMaxima;
    }

    public void setRestriccionPesoMinimo(double pesoMinimo) {
        if (pesoMinimo < 0) throw new DatosInvalidosException("El peso mínimo no puede ser negativo.");
        if (pesoMinimo >= getRestriccionPesoMaximo()) throw new DatosInvalidosException("El peso mínimo debe ser menor que el máximo.");
        this.restriccionPesoMinimo = pesoMinimo;
    }

    public void setRestriccionPesoMaximo(double pesoMaximo) {
        if (pesoMaximo <= getRestriccionPesoMinimo()) throw new DatosInvalidosException("El peso máximo debe ser mayor que el mínimo.");
        this.restriccionPesoMaximo = pesoMaximo;
    }

    public void setContraindicacionesSalud(List<String> contraindicaciones) {
        if (contraindicaciones == null) throw new DatosInvalidosException("La lista de contraindicaciones no puede ser null.");
        this.contraindicacionesSalud = new ArrayList<>(contraindicaciones);
    }

    public void setRestriccionesSalud(List<String> restricciones) {
        if (restricciones == null) throw new DatosInvalidosException("La lista de restricciones no puede ser null.");
        this.restriccionesSalud = new ArrayList<>(restricciones);
    }

    @Override
    public boolean puedeAsignarEmpleado(Empleado empleado) {
        if (!(empleado instanceof OperarioAtraccion)) return false;
        return empleado.cumpleCapacitaciones(super.getCapacitacionesRequeridas());
    }

    @Override
    public void asignarEmpleado(Empleado empleado) {
        if (empleado == null) throw new DatosInvalidosException("El empleado a asignar no puede ser nulo.");
        if (!(empleado instanceof OperarioAtraccion) || !empleado.cumpleCapacitaciones(super.getCapacitacionesRequeridas())) {
            throw new CapacitacionInsuficienteException(empleado, this);
        }
        super.asignarEmpleado(empleado);
    }
}
