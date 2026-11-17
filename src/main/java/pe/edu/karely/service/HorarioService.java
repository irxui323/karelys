package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface HorarioService {
    List<Map<String, Object>> listarHorarios(long actor);
    Map<String, Object> registrarHorario(ApiInput i, long actor);
}
