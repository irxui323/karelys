package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface ConsultorioService {
    List<Map<String, Object>> listarConsultorios(long actor);
    Map<String, Object> registrarConsultorio(ApiInput i, long actor);

    Map<String, Object> editarConsultorio(ApiInput i, long actor);
}
