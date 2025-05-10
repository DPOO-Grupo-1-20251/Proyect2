// Manuel Villaveces (◣ ◢) KickAss

package aplicacion;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import dominio.tiquete.*;
import dominio.usuario.Usuario;
import dominio.usuario.Cliente;
import dominio.elementoparque.Atraccion;
import dominio.elementoparque.Espectaculo;
import dominio.elementoparque.NivelExclusividad;
import dominio.empleado.Empleado;
import dominio.excepciones.DatosInvalidosException;
import dominio.excepciones.TiqueteInvalidoException;
import dominio.elementoparque.ElementoParque;
import infraestructura.persistencia.TiqueteRepositoryJson;

public class ServicioVentaTiquetes {

    private static final double DESCUENTO_EMPLEADO = 0.50;
    private final TiqueteRepositoryJson tiqueteRepository;
    private final Map<String, Tiquete> tiquetesVendidos = new HashMap<>();

    public ServicioVentaTiquetes(TiqueteRepositoryJson tiqueteRepository, java.util.function.Function<String, Atraccion> atraccionResolver) {
        this.tiqueteRepository = tiqueteRepository;
        for (Tiquete t : tiqueteRepository.cargarTiquetes(atraccionResolver)) {
            tiquetesVendidos.put(t.getCodigo(), t);
        }
    }

    private double calcularPrecioFinal(Usuario comprador, double precioBase) {
        if (comprador instanceof Empleado) {
            return precioBase * (1 - DESCUENTO_EMPLEADO);
        }
        return precioBase;
    }

    public TiqueteGeneral venderTiqueteGeneral(Usuario comprador, CategoriaTiquete categoria, double precioBase) {
        validarNotNull(comprador, "Comprador");
        validarNotNull(categoria, "Categoría");
        String codigo = generarCodigoUnico();
        boolean esEmpleado = comprador instanceof Empleado;
        double precioFinal = calcularPrecioFinal(comprador, precioBase);
        TiqueteGeneral tg = new TiqueteGeneral(codigo, LocalDateTime.now(), precioFinal,
                comprador.getIdentificacion(), comprador.getNombre(), esEmpleado, categoria, comprador);
        tiquetesVendidos.put(codigo, tg);
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
        return tg;
    }

    public TiqueteTemporada venderTiqueteTemporada(Usuario comprador, CategoriaTiquete categoria,
                                                   LocalDateTime fechaInicio, LocalDateTime fechaFin, double precioBase) {
        validarNotNull(comprador, "Comprador");
        validarNotNull(categoria, "Categoría");
        validarNotNull(fechaInicio, "Fecha de inicio");
        validarNotNull(fechaFin, "Fecha de fin");
        String codigo = generarCodigoUnico();
        boolean esEmpleado = comprador instanceof Empleado;
        double precioFinal = calcularPrecioFinal(comprador, precioBase);
        TiqueteTemporada tt = new TiqueteTemporada(codigo, LocalDateTime.now(), precioFinal,
                comprador.getIdentificacion(), comprador.getNombre(), esEmpleado, categoria, fechaInicio, fechaFin, comprador);
        tiquetesVendidos.put(codigo, tt);
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
        return tt;
    }

    public EntradaIndividual venderEntradaIndividual(Usuario comprador, Atraccion atraccion, double precioBase) {
        validarNotNull(comprador, "Comprador");
        validarNotNull(atraccion, "Atracción");
        String codigo = generarCodigoUnico();
        boolean esEmpleado = comprador instanceof Empleado;
        double precioFinal = calcularPrecioFinal(comprador, precioBase);
        EntradaIndividual ei = new EntradaIndividual(codigo, LocalDateTime.now(), precioFinal,
                comprador.getIdentificacion(), comprador.getNombre(), esEmpleado, atraccion, comprador);
        tiquetesVendidos.put(codigo, ei);
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
        return ei;
    }

    public FastPass venderFastPass(Usuario comprador, LocalDateTime fechaValida, double precioBase) {
        validarNotNull(comprador, "Comprador");
        validarNotNull(fechaValida, "Fecha válida");
        String codigo = generarCodigoUnico();
        boolean esEmpleado = comprador instanceof Empleado;
        double precioFinal = calcularPrecioFinal(comprador, precioBase);
        FastPass fp = new FastPass(codigo, LocalDateTime.now(), fechaValida, precioFinal,
                comprador.getIdentificacion(), comprador.getNombre(), esEmpleado, comprador);
        tiquetesVendidos.put(codigo, fp);
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
        return fp;
    }

    private void validarNotNull(Object obj, String mensaje) {
        if (obj == null) {
            throw new DatosInvalidosException(mensaje + " no puede ser nulo.");
        }
    }

    private String generarCodigoUnico() {
        return UUID.randomUUID().toString();
    }

    public boolean validarTiquete(Tiquete tiquete, LocalDateTime fechaUso) {
        validarNotNull(tiquete, "Tiquete");
        validarNotNull(fechaUso, "Fecha de uso");
        return tiquete.esValidoParaFecha(fechaUso);
    }

    public boolean validarAccesoAtraccion(Tiquete tiquete, ElementoParque elemento) {
        validarNotNull(tiquete, "Tiquete");
        validarNotNull(elemento, "Elemento del parque");

        if (elemento instanceof Espectaculo) return true;

        if (!(elemento instanceof Atraccion)) {
            throw new TiqueteInvalidoException("El elemento '" + elemento.getNombre() + "' no es una atracción válida.");
        }

        Atraccion atraccion = (Atraccion) elemento;
        NivelExclusividad nivelReq = atraccion.getNivelExclusividad();
        if (nivelReq == null) {
            throw new TiqueteInvalidoException("La atracción '" + atraccion.getNombre() + "' no tiene nivel de exclusividad definido.");
        }

        if (tiquete instanceof TiqueteGeneral || tiquete instanceof TiqueteTemporada || tiquete instanceof EntradaIndividual) {
            if (tiquete.getIdentificacionComprador() != null) {
                Usuario comprador = tiquete.getComprador();
                if (comprador instanceof Cliente) {
                    Cliente cliente = (Cliente) comprador;
                    if (!cliente.cumpleRestriccionesAtraccion(
                            atraccion.getAlturaMinima(), atraccion.getAlturaMaxima(),
                            atraccion.getPesoMinimo(), atraccion.getPesoMaximo(),
                            atraccion.getRestriccionesMedicas())) {
                        throw new TiqueteInvalidoException("El cliente no cumple las restricciones médicas o físicas para esta atracción.");
                    }
                }
            }
        }

        boolean accesoPermitido = false;
        CategoriaTiquete categoriaTiquete = null;

        if (tiquete instanceof TiqueteGeneral) {
            categoriaTiquete = ((TiqueteGeneral) tiquete).getCategoria();
            accesoPermitido = nivelReq.permiteAccesoConCategoria(categoriaTiquete);
        } else if (tiquete instanceof TiqueteTemporada) {
            categoriaTiquete = ((TiqueteTemporada) tiquete).getCategoria();
            accesoPermitido = nivelReq.permiteAccesoConCategoria(categoriaTiquete);
        } else if (tiquete instanceof EntradaIndividual) {
            accesoPermitido = ((EntradaIndividual) tiquete).esValidoParaAtraccion(atraccion);
            if (!accesoPermitido) {
                throw new TiqueteInvalidoException("Esta Entrada Individual no es válida para la atracción '" + atraccion.getNombre() + "'.");
            }
        } else if (tiquete instanceof FastPass) {
            throw new TiqueteInvalidoException("Un FastPass por sí solo no otorga acceso a la atracción. Se requiere un tiquete base válido.");
        }

        if (!accesoPermitido && categoriaTiquete != null) {
            throw new TiqueteInvalidoException("El tiquete categoría '" + categoriaTiquete + "' no permite acceso a '" + atraccion.getNombre() + "' (Nivel: " + nivelReq + ").");
        }

        return true;
    }

    public void registrarUsoTiquete(Tiquete tiquete) {
        validarNotNull(tiquete, "Tiquete a registrar");
        tiquete.marcarComoUtilizado();
        tiquetesVendidos.put(tiquete.getCodigo(), tiquete);
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
        System.out.println("Uso registrado para tiquete: " + tiquete.getCodigo());
    }

    public List<Tiquete> consultarTiquetesPorUsuario(String identificacion) {
        validarNotNull(identificacion, "Identificación");
        return tiquetesVendidos.values().stream()
                .filter(t -> identificacion.equals(t.getIdentificacionComprador()))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Tiquete> consultarTiquetesPorFecha(java.time.LocalDate fecha) {
        validarNotNull(fecha, "Fecha");
        return tiquetesVendidos.values().stream()
                .filter(t -> t.getFechaHoraEmision().toLocalDate().equals(fecha))
                .collect(java.util.stream.Collectors.toList());
    }

    public <T extends Tiquete> List<T> consultarTiquetesPorTipo(Class<T> tipo) {
        validarNotNull(tipo, "Tipo de tiquete");
        return tiquetesVendidos.values().stream()
                .filter(tipo::isInstance)
                .map(tipo::cast)
                .collect(java.util.stream.Collectors.toList());
    }

    public void eliminarTiquetesPorUsuario(String identificacion) {
        validarNotNull(identificacion, "Identificación");
        List<String> codigosAEliminar = new ArrayList<>();
        for (Tiquete t : tiquetesVendidos.values()) {
            if (identificacion.equals(t.getIdentificacionComprador())) {
                codigosAEliminar.add(t.getCodigo());
            }
        }
        for (String codigo : codigosAEliminar) {
            tiquetesVendidos.remove(codigo);
        }
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
    }

    public <T extends Tiquete> void eliminarTiquetesPorTipo(Class<T> tipo) {
        validarNotNull(tipo, "Tipo de tiquete");
        List<String> codigosAEliminar = new ArrayList<>();
        for (Tiquete t : tiquetesVendidos.values()) {
            if (tipo.isInstance(t)) {
                codigosAEliminar.add(t.getCodigo());
            }
        }
        for (String codigo : codigosAEliminar) {
            tiquetesVendidos.remove(codigo);
        }
        tiqueteRepository.guardarTiquetes(new ArrayList<>(tiquetesVendidos.values()));
    }

    // ✅ ADDED METHOD
    public Tiquete obtenerTiquetePorCodigo(String codigo) {
        return tiquetesVendidos.get(codigo);
    }
}
