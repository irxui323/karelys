package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.ConsultorioService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ConsultorioController {
    private final ConsultorioService service;
    public ConsultorioController(ConsultorioService service) {
        this.service = service;
    }
    @GetMapping("/datos/consultorios")
    public List<Map<String, Object>> listarConsultorios(Authentication auth) {
        return service.listarConsultorios((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/consultorio-crear")
    public Map<String, Object> registrarConsultorio(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.registrarConsultorio(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/consultorio-editar")
    public Map<String, Object> editarConsultorio(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.editarConsultorio(new ApiInput(datos), (Long) auth.getPrincipal());
    }
}
