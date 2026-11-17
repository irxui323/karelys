package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface AuditoriaService {
    List<Map<String, Object>> listarAuditoria(long actor);
}
