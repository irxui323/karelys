package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.EspecialidadService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class EspecialidadController {
    private final EspecialidadService service;
    public EspecialidadController(EspecialidadService service) {
        this.service = service;
    }
    @GetMapping("/datos/especialidades")
    public List<Map<String, Object>> listarEspecialidades(Authentication auth) {
        return service.listarEspecialidades((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/especialidad-crear")
    public Map<String, Object> registrarEspecialidad(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.registrarEspecialidad(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/especialidad-editar")
    public Map<String, Object> editarEspecialidad(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.editarEspecialidad(new ApiInput(datos), (Long) auth.getPrincipal());
    }
}
