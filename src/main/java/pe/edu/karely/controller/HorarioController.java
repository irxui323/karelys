package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.HorarioService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class HorarioController {
    private final HorarioService service;
    public HorarioController(HorarioService service) {
        this.service = service;
    }
    @GetMapping("/datos/disponibilidades")
    public List<Map<String, Object>> listarHorarios(Authentication auth) {
        return service.listarHorarios((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/disponibilidad-registrar")
    public Map<String, Object> registrarHorario(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.registrarHorario(new ApiInput(datos), (Long) auth.getPrincipal());
    }

}
