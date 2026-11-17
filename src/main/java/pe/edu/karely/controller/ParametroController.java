package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.ParametroService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ParametroController {
    private final ParametroService service;
    public ParametroController(ParametroService service) {
        this.service = service;
    }
    @GetMapping("/datos/parametros")
    public List<Map<String, Object>> listarParametros(Authentication auth) {
        return service.listarParametros((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/parametro-cambiar")
    public Map<String, Object> configurarParametro(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.configurarParametro(new ApiInput(datos), (Long) auth.getPrincipal());
    }

}
