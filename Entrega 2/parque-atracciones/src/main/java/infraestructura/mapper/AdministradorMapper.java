package infraestructura.mapper;

import dominio.empleado.Administrador;
import infraestructura.dto.AdministradorDTO;

public class AdministradorMapper {
    public static AdministradorDTO toDTO(Administrador admin) {
        return new AdministradorDTO(
            admin.getIdentificacion(),
            admin.getNombre(),
            admin.getEmail(),
            admin.getTelefono(),
            admin.getUsername(),
            admin.getPassword(),
            admin.getAreasResponsabilidad()
        );
    }

    public static Administrador fromDTO(AdministradorDTO dto) {
        return new Administrador(
            dto.identificacion,
            dto.nombre,
            dto.email,
            dto.telefono,
            dto.username,
            dto.password,
            dto.areasResponsabilidad
        );
    }
}
