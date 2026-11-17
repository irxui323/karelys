package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.UsuarioService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }
    @GetMapping("/datos/roles")
    public List<Map<String, Object>> listarRoles(Authentication auth) {
        return service.listarRoles((Long) auth.getPrincipal());
    }

    @GetMapping("/datos/usuarios")
    public List<Map<String, Object>> listarUsuarios(Authentication auth) {
        return service.listarUsuarios((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/usuario-rol")
    public Map<String, Object> asignarRol(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.asignarRol(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/usuario-estado")
    public Map<String, Object> cambiarEstado(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.cambiarEstado(new ApiInput(datos), (Long) auth.getPrincipal());
    }


    @PostMapping("/admin/usuarios")
    public Map<String, Object> registrarUsuario(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.registrarUsuario(new ApiInput(datos), (Long) auth.getPrincipal());
    }
}
