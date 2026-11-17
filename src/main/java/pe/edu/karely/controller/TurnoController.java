package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.TurnoService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TurnoController {
    private final TurnoService service;
    public TurnoController(TurnoService service) {
        this.service = service;
    }
    @GetMapping("/datos/franjas")
    public List<Map<String, Object>> listarTurnos(Authentication auth) {
        return service.listarTurnos((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/franja-crear")
    public Map<String, Object> registrarTurno(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.registrarTurno(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/franja-editar")
    public Map<String, Object> editarTurno(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.editarTurno(new ApiInput(datos), (Long) auth.getPrincipal());
    }
}
