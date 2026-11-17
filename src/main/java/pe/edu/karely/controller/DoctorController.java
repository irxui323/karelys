package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.DoctorService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class DoctorController {
    private final DoctorService service;
    public DoctorController(DoctorService service) {
        this.service = service;
    }
    @GetMapping("/datos/doctores")
    public List<Map<String, Object>> listarDoctores(Authentication auth) {
        return service.listarDoctores((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/doctor-crear")
    public Map<String, Object> registrarDoctor(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.registrarDoctor(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/doctor-editar")
    public Map<String, Object> editarDoctor(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.editarDoctor(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/doctor-vincular-usuario")
    public Map<String, Object> vincularUsuario(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.vincularUsuario(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/doctor-especialidad")
    public Map<String, Object> asignarEspecialidad(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.asignarEspecialidad(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/doctor-validar")
    public Map<String, Object> validarDocumentacion(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.validarDocumentacion(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @GetMapping("/doctores/buscar")
    public List<Map<String, Object>> buscarPorDocumento(@RequestParam String documento, Authentication auth) {
        return service.buscarPorDocumento(documento, (Long) auth.getPrincipal());
    }

    @GetMapping("/doctores/pendientes")
    public List<Map<String, Object>> listarPendientes(Authentication auth) {
        return service.listarPendientes((Long) auth.getPrincipal());
    }

    @GetMapping("/doctores/documentacion-completa")
    public List<Map<String, Object>> listarConDocumentacionCompleta(Authentication auth) {
        return service.listarConDocumentacionCompleta((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/doctor-rechazar-documentacion")
    public Map<String, Object> rechazarDocumentacion(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.rechazarDocumentacion(new ApiInput(datos), (Long) auth.getPrincipal());
    }
}
