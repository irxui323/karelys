package pe.edu.karely.controller;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.PacienteService;
import pe.edu.karely.dto.ApiInput;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PacienteController {
    private final PacienteService service;
    public PacienteController(PacienteService service) {
        this.service = service;
    }
    @GetMapping("/datos/pacientes")
    public List<Map<String, Object>> listarPacientes(Authentication auth) {
        return service.listarPacientes((Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/paciente-crear")
    public Map<String, Object> registrarPaciente(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.registrarPaciente(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @PostMapping("/operaciones/paciente-editar")
    public Map<String, Object> editarPaciente(@RequestBody Map<String, Object> datos, Authentication auth) {
        return service.editarPaciente(new ApiInput(datos), (Long) auth.getPrincipal());
    }

    @GetMapping("/pacientes/buscar")
    public List<Map<String, Object>> buscarPaciente(@RequestParam String tipo, @RequestParam String documento, Authentication auth) {
        return service.buscarPaciente(tipo, documento, (Long) auth.getPrincipal());
    }
}
