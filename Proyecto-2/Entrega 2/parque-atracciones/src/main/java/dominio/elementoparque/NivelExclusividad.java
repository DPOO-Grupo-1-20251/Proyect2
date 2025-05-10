package dominio.elementoparque;

import dominio.tiquete.CategoriaTiquete;

/**
 * Enum que representa los niveles de exclusividad de las atracciones del parque.
 */
public enum NivelExclusividad {
    FAMILIAR("Acceso con tiquete Familiar o superior"),
    ORO("Acceso con tiquete Oro o superior"),
    DIAMANTE("Acceso exclusivo con tiquete Diamante");

    private final String descripcion;

    NivelExclusividad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean permiteAccesoConCategoria(CategoriaTiquete categoria) {
        switch (this) {
            case FAMILIAR:
                return categoria == CategoriaTiquete.FAMILIAR || categoria == CategoriaTiquete.ORO || categoria == CategoriaTiquete.DIAMANTE;
            case ORO:
                return categoria == CategoriaTiquete.ORO || categoria == CategoriaTiquete.DIAMANTE;
            case DIAMANTE:
                return categoria == CategoriaTiquete.DIAMANTE;
            default:
                return false;
        }
    }
}
