package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface EspecialidadService {
    List<Map<String, Object>> listarEspecialidades(long actor);
    Map<String, Object> registrarEspecialidad(ApiInput i, long actor);

    Map<String, Object> editarEspecialidad(ApiInput i, long actor);
}
