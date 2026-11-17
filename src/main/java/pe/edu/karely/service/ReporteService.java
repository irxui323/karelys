package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface ReporteService {
    List<Map<String, Object>> generarResumen(long actor);

    List<Map<String, Object>> generarReporte(java.time.LocalDate desde, java.time.LocalDate hasta, String estado, long actor);
}
