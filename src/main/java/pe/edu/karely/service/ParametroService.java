package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface ParametroService {
    List<Map<String, Object>> listarParametros(long actor);
    Map<String, Object> configurarParametro(ApiInput i, long actor);
}
