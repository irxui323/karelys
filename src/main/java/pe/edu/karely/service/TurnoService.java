package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface TurnoService {
    List<Map<String, Object>> listarTurnos(long actor);
    Map<String, Object> registrarTurno(ApiInput i, long actor);

    Map<String, Object> editarTurno(ApiInput i, long actor);
}
