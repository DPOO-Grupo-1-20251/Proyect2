package presentacion;

import dominio.elementoparque.*;
import dominio.empleado.Capacitacion;
import dominio.util.CondicionClimatica;
import java.util.List;

public class ElementoParqueUtil {
    public static Atraccion buscarAtraccionPorId(String idAtraccion) {
        if (idAtraccion.equals("A1")) {
            return new AtraccionMecanica(
                idAtraccion, "Montaña Rusa", "Zona A", 20, 2,
                NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO,
                1.2, 2.0, 50, 120,
                List.of("Cardiopatía"),
                List.of("Asma"),
                List.of(CondicionClimatica.TORMENTA),
                Capacitacion.MONTAÑA_RUSA_A1
            );
        }
        return null;
    }
}
