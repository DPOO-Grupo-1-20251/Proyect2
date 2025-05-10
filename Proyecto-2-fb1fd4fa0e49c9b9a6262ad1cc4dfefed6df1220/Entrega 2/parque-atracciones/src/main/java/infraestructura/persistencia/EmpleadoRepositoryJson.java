// ✅ Manuel Villaveces (◣ ◢) KickAss Games - EmpleadoRepositoryJson FULL VERSION

package infraestructura.persistencia;

import infraestructura.dto.EmpleadoDTO;
import infraestructura.mapper.EmpleadoMapper;
import dominio.empleado.Empleado;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class EmpleadoRepositoryJson {
    private final String archivoAbsoluto;
    private final List<Empleado> empleadosCache;

    public EmpleadoRepositoryJson(String nombreArchivo) {
        this.archivoAbsoluto = JsonUtil.getDataFilePath(nombreArchivo);
        File file = new File(archivoAbsoluto);
        if (!file.exists()) {
            JsonUtil.writeToFileAbsolute(archivoAbsoluto, new ArrayList<EmpleadoDTO>());
        }
        this.empleadosCache = cargarEmpleados();
    }

    public void guardarEmpleados(List<Empleado> empleados) {
        List<EmpleadoDTO> listaDTO = new ArrayList<>();
        for (Empleado empleado : empleados) {
            listaDTO.add(EmpleadoMapper.toDTO(empleado));
        }
        JsonUtil.writeToFileAbsolute(archivoAbsoluto, listaDTO);
    }

    public List<Empleado> cargarEmpleados() {
        TypeToken<List<EmpleadoDTO>> typeToken = new TypeToken<List<EmpleadoDTO>>() {};
        List<EmpleadoDTO> listaDTO = JsonUtil.readFromFileAbsolute(archivoAbsoluto, typeToken);
        List<Empleado> empleados = new ArrayList<>();
        for (EmpleadoDTO dto : listaDTO) {
            empleados.add(EmpleadoMapper.fromDTO(dto));
        }
        return empleados;
    }

    public Empleado buscarEmpleadoPorUsername(String username) {
        for (Empleado e : empleadosCache) {
            if (e.getUsername().equalsIgnoreCase(username)) {
                return e;
            }
        }
        return null;
    }

    public void agregarEmpleado(Empleado empleado) {
        empleadosCache.add(empleado);
        guardarEmpleados(empleadosCache);
    }

    public Empleado buscarEmpleadoPorIdentificacion(String id) {
        for (Empleado e : empleadosCache) {
            if (e.getIdentificacion().equals(id)) {
                return e;
            }
        }
        return null;
    }

    public List<Empleado> getEmpleados() {
        return empleadosCache;
    }
}
