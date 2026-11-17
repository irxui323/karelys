package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface GananciaService {
    Map<String, Object> cancelarReprogramada(ApiInput i, long actor);
}
