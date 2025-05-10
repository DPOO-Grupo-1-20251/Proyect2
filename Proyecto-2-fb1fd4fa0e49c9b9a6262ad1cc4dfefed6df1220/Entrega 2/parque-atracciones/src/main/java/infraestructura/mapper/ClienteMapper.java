package infraestructura.mapper;

import infraestructura.dto.ClienteDTO;
import dominio.usuario.Cliente;

public class ClienteMapper {
    public static ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.id = cliente.getIdentificacion();
        dto.username = cliente.getUsername();
        dto.password = cliente.getPassword();
        dto.nombre = cliente.getNombre();
        dto.email = cliente.getEmail();
        dto.telefono = cliente.getTelefono();
        dto.fechaNacimiento = cliente.getFechaNacimiento();
        dto.altura = cliente.getAltura();
        dto.peso = cliente.getPeso();
        return dto;
    }

    public static Cliente fromDTO(ClienteDTO dto) {
        return new Cliente(
            dto.username,
            dto.password,
            dto.nombre,
            dto.id,
            dto.email,
            dto.telefono,
            dto.fechaNacimiento,
            dto.altura,
            dto.peso
        );
    }
}
