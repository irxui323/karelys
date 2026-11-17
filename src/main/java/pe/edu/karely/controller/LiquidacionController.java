package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.LiquidacionService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class LiquidacionController {
    private final LiquidacionService service;
    public LiquidacionController(LiquidacionService service) {
        this.service = service;
    }
    @GetMapping("/datos/liquidaciones")
    public List<Map<String, Object>> listarLiquidaciones(Authentication auth) {
        return service.listarLiquidaciones((Long) auth.getPrincipal());
    }

    @GetMapping("/datos/pagos-doctor")
    public List<Map<String, Object>> listarPagos(Authentication auth) {
        return service.listarPagos((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/liquidacion-generar")
    public Map<String, Object> generarLiquidacion(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.generarLiquidacion(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/doctor-pagar")
    public Map<String, Object> pagarDoctor(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.pagarDoctor(new ApiInput(datos), (Long) auth.getPrincipal());
    }

}
