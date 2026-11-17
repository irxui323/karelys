package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.CitaService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CitaController {
    private final CitaService service;
    public CitaController(CitaService service) {
        this.service = service;
    }
    @GetMapping("/datos/citas")
    public List<Map<String, Object>> listarCitas(Authentication auth) {
        return service.listarCitas((Long) auth.getPrincipal());
    }

    @GetMapping("/datos/reservas")
    public List<Map<String, Object>> listarReservas(Authentication auth) {
        return service.listarReservas((Long) auth.getPrincipal());
    }

    @GetMapping("/datos/atenciones")
    public List<Map<String, Object>> listarAtenciones(Authentication auth) {
        return service.listarAtenciones((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/prereserva-crear")
    public Map<String, Object> bloquearTurno(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.bloquearTurno(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/cita-confirmar")
    public Map<String, Object> registrarCita(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.registrarCita(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/cita-reprogramar")
    public Map<String, Object> reprogramarCita(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.reprogramarCita(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/asistencia-registrar")
    public Map<String, Object> registrarAsistencia(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.registrarAsistencia(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/atencion-iniciar")
    public Map<String, Object> iniciarAtencion(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.iniciarAtencion(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/diagnostico-registrar")
    public Map<String, Object> registrarDiagnostico(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.registrarDiagnostico(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/receta-crear")
    public Map<String, Object> registrarReceta(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.registrarReceta(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/medicamento-agregar")
    public Map<String, Object> agregarMedicamento(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.agregarMedicamento(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/recibo-emitir")
    public Map<String, Object> emitirRecibo(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.emitirRecibo(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/recibo-entregar")
    public Map<String, Object> entregarRecibo(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.entregarRecibo(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/atencion-cerrar")
    public Map<String, Object> cerrarAtencion(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.cerrarAtencion(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @GetMapping("/agenda-medica")
    public List<Map<String, Object>> filtrarAgendaMedica(Authentication auth, @RequestParam java.time.LocalDate fecha) {
        return service.filtrarAgendaMedica((Long) auth.getPrincipal(), fecha);
    }

    @GetMapping("/agenda-medica/{idCita}")
    public Map<String, Object> detalleCitaMedica(Authentication auth, @PathVariable long idCita) {
        return service.detalleCitaMedica((Long) auth.getPrincipal(), idCita);
    }
}
