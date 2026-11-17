package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.IngresoService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class IngresoController {
    private final IngresoService service;
    public IngresoController(IngresoService service) {
        this.service = service;
    }
    @GetMapping("/datos/ingresos")
    public List<Map<String, Object>> listarIngresos(Authentication auth) {
        return service.listarIngresos((Long) auth.getPrincipal());
    }

    @GetMapping("/ingresos/doctores/{doctor}/total")
    public List<Map<String, Object>> sumarIngresos(@PathVariable long doctor, Authentication auth) {
        return service.sumarIngresos(doctor, (Long) auth.getPrincipal());
    }
}
