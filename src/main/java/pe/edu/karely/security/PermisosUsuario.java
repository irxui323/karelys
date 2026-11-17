package pe.edu.karely.security;

import java.util.Arrays;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import pe.edu.karely.repository.UsuarioRepository;

@Component
public class PermisosUsuario {
    private final UsuarioRepository usuarios;

    public PermisosUsuario(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    public String rol(long actor) {
        var fila = usuarios.rolActivo(actor);
        if (fila == null) {
            throw new AccessDeniedException("Usuario inactivo");
        }
        return fila.get("ROL").toString();
    }

    public void exigir(long actor, String... roles) {
        if (!Arrays.asList(roles).contains(rol(actor))) {
            throw new AccessDeniedException("Rol sin permiso para esta operación");
        }
    }
}
