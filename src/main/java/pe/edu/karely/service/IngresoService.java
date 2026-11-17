package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface IngresoService {
    List<Map<String, Object>> listarIngresos(long actor);

    List<Map<String, Object>> sumarIngresos(long doctor, long actor);
}
