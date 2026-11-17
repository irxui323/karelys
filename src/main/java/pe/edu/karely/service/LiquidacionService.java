package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface LiquidacionService {
    List<Map<String, Object>> listarLiquidaciones(long actor);
    List<Map<String, Object>> listarPagos(long actor);
    Map<String, Object> generarLiquidacion(ApiInput i, long actor);
    Map<String, Object> pagarDoctor(ApiInput i, long actor);
}
