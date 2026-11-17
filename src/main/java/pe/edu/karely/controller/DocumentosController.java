package pe.edu.karely.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.karely.service.DocumentoDoctorService;

@RestController
@RequestMapping("/api/v1/documentos")
public class DocumentosController {
    private final DocumentoDoctorService service;

    public DocumentosController(DocumentoDoctorService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> upload(@RequestParam long idDoctor, @RequestParam String tipo,
                                      @RequestParam MultipartFile archivo, Authentication auth) throws IOException {
        return service.subir(idDoctor, tipo, archivo, (Long) auth.getPrincipal());
    }

    @GetMapping("/doctor/{idDoctor}")
    public List<Map<String, Object>> list(@PathVariable long idDoctor, Authentication auth) {
        return service.listar(idDoctor, (Long) auth.getPrincipal());
    }

    @GetMapping("/{idDocumento}")
    public ResponseEntity<byte[]> read(@PathVariable long idDocumento, Authentication auth) {
        var documento = service.descargar(idDocumento, (Long) auth.getPrincipal());
        if (documento == null) {
            return ResponseEntity.notFound().build();
        }
        String nombre = documento.nombre().replaceAll("[^A-Za-z0-9._-]", "_");
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombre)
                .contentType(MediaType.parseMediaType(documento.mime())).body(documento.bytes());
    }
}
