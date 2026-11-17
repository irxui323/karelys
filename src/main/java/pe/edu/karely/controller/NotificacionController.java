package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.NotificacionService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class NotificacionController {
    private final NotificacionService service;
    public NotificacionController(NotificacionService service) {
        this.service = service;
    }
    @GetMapping("/datos/notificaciones")
    public List<Map<String, Object>> listarNotificaciones(Authentication auth) {
        return service.listarNotificaciones((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/notificacion-encolar")
    public Map<String, Object> encolarNotificacion(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.encolarNotificacion(new ApiInput(datos), (Long) auth.getPrincipal());
    }

}
