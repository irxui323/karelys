package pe.edu.karely.serviceimpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.karely.repository.DoctorRepository;
import pe.edu.karely.repository.DocumentoDoctorRepository;
import pe.edu.karely.repository.DocumentoDoctorRepository.Documento;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.service.DocumentoDoctorService;

@Service
public class DocumentoDoctorServiceImpl implements DocumentoDoctorService {
    private final DocumentoDoctorRepository documentos;
    private final DoctorRepository doctores;
    private final PermisosUsuario permisos;

    public DocumentoDoctorServiceImpl(DocumentoDoctorRepository documentos, DoctorRepository doctores,
                                     PermisosUsuario permisos) {
        this.documentos = documentos;
        this.doctores = doctores;
        this.permisos = permisos;
    }

    @Override
    @Transactional
    public Map<String, Object> subir(long idDoctor, String tipo, MultipartFile archivo, long actor) throws IOException {
        permisos.exigir(actor, "ADMINISTRADORA");
        if (!Set.of("DNI", "TITULO", "OTRO").contains(tipo)) {
            throw new IllegalArgumentException("Tipo no permitido");
        }
        if (archivo.isEmpty() || archivo.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("Archivo obligatorio, máximo 10 MB");
        }
        String mime = Objects.toString(archivo.getContentType(), "");
        if (!Set.of("application/pdf", "image/png", "image/jpeg").contains(mime)) {
            throw new IllegalArgumentException("Archivo PDF, PNG o JPEG únicamente");
        }
        String nombre = Objects.toString(archivo.getOriginalFilename(), "documento")
                .replaceAll("[^A-Za-z0-9._-]", "_");
        Long id = documentos.insertar(idDoctor, tipo, nombre, mime, archivo.getBytes());
        doctores.marcarDocumentacionIncompleta(idDoctor);
        return Map.of("ok", true, "idDocumento", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listar(long idDoctor, long actor) {
        permisos.exigir(actor, "ADMINISTRADORA");
        return documentos.listarDocumentos(idDoctor);
    }

    @Override
    @Transactional(readOnly = true)
    public Documento descargar(long idDocumento, long actor) {
        permisos.exigir(actor, "ADMINISTRADORA");
        var filas = documentos.obtener(idDocumento);
        return filas.isEmpty() ? null : filas.getFirst();
    }
}
