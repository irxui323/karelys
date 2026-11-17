package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface UsuarioService {
    List<Map<String, Object>> listarRoles(long actor);
    List<Map<String, Object>> listarUsuarios(long actor);
    Map<String, Object> asignarRol(ApiInput i, long actor);
    Map<String, Object> cambiarEstado(ApiInput i, long actor);

    Map<String, Object> registrarUsuario(ApiInput entrada, long actor);
}
