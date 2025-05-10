// ✅ Manuel Villaveces (◣ ◢) KickAss Games - ClienteRepositoryJson FULL VERSION

package infraestructura.persistencia;

import infraestructura.dto.ClienteDTO;
import infraestructura.mapper.ClienteMapper;
import dominio.usuario.Cliente;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class ClienteRepositoryJson {
    private final String archivoAbsoluto;
    private final List<Cliente> clientesCache;

    public ClienteRepositoryJson(String nombreArchivo) {
        this.archivoAbsoluto = JsonUtil.getDataFilePath(nombreArchivo);
        File file = new File(archivoAbsoluto);
        if (!file.exists()) {
            JsonUtil.writeToFileAbsolute(archivoAbsoluto, new ArrayList<ClienteDTO>());
        }
        this.clientesCache = cargarClientes();
    }

    public void guardarClientes(List<Cliente> clientes) {
        List<ClienteDTO> listaDTO = new ArrayList<>();
        for (Cliente cliente : clientes) {
            listaDTO.add(ClienteMapper.toDTO(cliente));
        }
        JsonUtil.writeToFileAbsolute(archivoAbsoluto, listaDTO);
    }

    public List<Cliente> cargarClientes() {
        TypeToken<List<ClienteDTO>> typeToken = new TypeToken<List<ClienteDTO>>() {};
        List<ClienteDTO> listaDTO = JsonUtil.readFromFileAbsolute(archivoAbsoluto, typeToken);
        List<Cliente> clientes = new ArrayList<>();
        for (ClienteDTO dto : listaDTO) {
            clientes.add(ClienteMapper.fromDTO(dto));
        }
        return clientes;
    }

    public Cliente buscarClientePorUsername(String username) {
        for (Cliente c : clientesCache) {
            if (c.getUsername().equalsIgnoreCase(username)) {
                return c;
            }
        }
        return null;
    }

    public void agregarCliente(Cliente cliente) {
        clientesCache.add(cliente);
        guardarClientes(clientesCache);
    }

    public Cliente buscarClientePorIdentificacion(String id) {
        for (Cliente c : clientesCache) {
            if (c.getIdentificacion().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public List<Cliente> getClientes() {
        return clientesCache;
    }
}
