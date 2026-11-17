package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.UsuarioService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository repository;
    private final org.springframework.security.crypto.password.PasswordEncoder encoder;
    private final PermisosUsuario permisos;
    public UsuarioServiceImpl(UsuarioRepository repository, PermisosUsuario permisos, org.springframework.security.crypto.password.PasswordEncoder encoder) {
        this.encoder = encoder;
        this.repository = repository;
        this.permisos = permisos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarRoles(long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        return repository.listarRoles();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarUsuarios(long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        return repository.listarUsuarios();
    }

    @Override
    @Transactional
    public Map<String, Object> asignarRol(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        return RespuestasOperacion.procedimiento("SP_CAMBIAR_ROL_USUARIO", repository.asignarRol(actor, actor, i.id("idUsuario"), i.id("idRol")));
    }

    @Override
    @Transactional
    public Map<String, Object> cambiarEstado(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        return RespuestasOperacion.procedimiento("SP_CAMBIAR_ESTADO_USUARIO", repository.cambiarEstado(actor, actor, i.id("idUsuario"), i.required("estado")));
    }


    @Override
    @Transactional
    public Map<String, Object> registrarUsuario(ApiInput entrada, long actor) {
        permisos.exigir(actor, "ADMINISTRADORA");
        String password = entrada.required("password");
        if (password.length() < 12) {
            throw new IllegalArgumentException("Contraseña mínima: 12 caracteres");
        }
        Object id = repository.crearUsuario(actor, entrada.id("idRol"), entrada.required("username"),
                entrada.required("correo"), encoder.encode(password));
        return Map.of("ok", true, "id", id);
    }
}
