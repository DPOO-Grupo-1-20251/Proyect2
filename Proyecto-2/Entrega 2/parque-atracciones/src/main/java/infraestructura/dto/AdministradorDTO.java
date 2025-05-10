package infraestructura.dto;

import java.util.List;

/**
 * DTO para Administrador.
 */
public class AdministradorDTO {
    public String identificacion;
    public String nombre;
    public String email;
    public String telefono;
    public String username;
    public String password;
    public List<String> areasResponsabilidad;

    public AdministradorDTO() {} // Constructor vacío requerido por Gson

    public AdministradorDTO(String identificacion, String nombre, String email, String telefono,
                            String username, String password, List<String> areasResponsabilidad) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.username = username;
        this.password = password;
        this.areasResponsabilidad = areasResponsabilidad;
    }
}
