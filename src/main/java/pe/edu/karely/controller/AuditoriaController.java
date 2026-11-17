package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.AuditoriaService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuditoriaController {
    private final AuditoriaService service;
    public AuditoriaController(AuditoriaService service) {
        this.service = service;
    }
    @GetMapping("/datos/auditoria")
    public List<Map<String, Object>> listarAuditoria(Authentication auth) {
        return service.listarAuditoria((Long) auth.getPrincipal());
    }

}
