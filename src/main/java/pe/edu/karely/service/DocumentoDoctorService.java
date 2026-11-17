package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.repository.DocumentoDoctorRepository.Documento;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface DocumentoDoctorService {
    Map<String, Object> subir(long idDoctor, String tipo, MultipartFile archivo, long actor) throws IOException;
    List<Map<String, Object>> listar(long idDoctor, long actor);
    Documento descargar(long idDocumento, long actor);
}
