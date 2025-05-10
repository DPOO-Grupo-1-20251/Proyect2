// ✅ Manuel Villaveces (◣ ◢) KickAss Games - AdministradorRepositoryJson

package infraestructura.persistencia;

import infraestructura.dto.AdministradorDTO;
import infraestructura.mapper.AdministradorMapper;
import dominio.empleado.Administrador;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class AdministradorRepositoryJson {
    private final String archivoAbsoluto;
    private final List<Administrador> administradoresCache;

    public AdministradorRepositoryJson(String nombreArchivo) {
        this.archivoAbsoluto = JsonUtil.getDataFilePath(nombreArchivo);
        File file = new File(archivoAbsoluto);
        if (!file.exists()) {
            JsonUtil.writeToFileAbsolute(archivoAbsoluto, new ArrayList<AdministradorDTO>());
        }
        this.administradoresCache = cargarAdministradores();
    }

    public void guardarAdministradores(List<Administrador> administradores) {
        List<AdministradorDTO> listaDTO = new ArrayList<>();
        for (Administrador admin : administradores) {
            listaDTO.add(AdministradorMapper.toDTO(admin));
        }
        JsonUtil.writeToFileAbsolute(archivoAbsoluto, listaDTO);
    }

    public List<Administrador> cargarAdministradores() {
        TypeToken<List<AdministradorDTO>> typeToken = new TypeToken<List<AdministradorDTO>>() {};
        List<AdministradorDTO> listaDTO = JsonUtil.readFromFileAbsolute(archivoAbsoluto, typeToken);
        List<Administrador> administradores = new ArrayList<>();
        for (AdministradorDTO dto : listaDTO) {
            administradores.add(AdministradorMapper.fromDTO(dto));
        }
        return administradores;
    }

    public Administrador buscarAdministradorPorUsername(String username) {
        for (Administrador admin : administradoresCache) {
            if (admin.getUsername().equalsIgnoreCase(username)) {
                return admin;
            }
        }
        return null;
    }

    public void agregarAdministrador(Administrador admin) {
        administradoresCache.add(admin);
        guardarAdministradores(administradoresCache);
    }

    public Administrador buscarAdministradorPorIdentificacion(String id) {
        for (Administrador admin : administradoresCache) {
            if (admin.getIdentificacion().equals(id)) {
                return admin;
            }
        }
        return null;
    }

    public List<Administrador> getAdministradores() {
        return administradoresCache;
    }
}
