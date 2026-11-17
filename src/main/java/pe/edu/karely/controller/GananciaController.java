package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.GananciaService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class GananciaController {
    private final GananciaService service;
    public GananciaController(GananciaService service) {
        this.service = service;
    }
    @PostMapping("/operaciones/reprogramada-cancelar")
    public Map<String, Object> cancelarReprogramada(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.cancelarReprogramada(new ApiInput(datos), (Long) auth.getPrincipal());
    }

}
