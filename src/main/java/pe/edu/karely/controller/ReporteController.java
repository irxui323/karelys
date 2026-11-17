package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.ReporteService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ReporteController {
    private final ReporteService service;
    public ReporteController(ReporteService service) {
        this.service = service;
    }
    @GetMapping("/datos/resumen")
    public List<Map<String, Object>> generarResumen(Authentication auth) {
        return service.generarResumen((Long) auth.getPrincipal());
    }

    @GetMapping("/reportes/citas")
    public List<Map<String, Object>> generarReporte(@RequestParam java.time.LocalDate desde, @RequestParam java.time.LocalDate hasta, @RequestParam(required = false) String estado, Authentication auth) {
        return service.generarReporte(desde, hasta, estado, (Long) auth.getPrincipal());
    }
}
