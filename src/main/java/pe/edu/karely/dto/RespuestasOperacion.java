package pe.edu.karely.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RespuestasOperacion {
    private RespuestasOperacion() {}

    public static Map<String, Object> resultado(String operacion, Object id) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("ok", true);
        respuesta.put("operacion", operacion);
        if (id != null) {
            respuesta.put("id", id);
        }
        return respuesta;
    }

    public static Map<String, Object> procedimiento(String operacion, List<Object> salidas) {
        return resultado(operacion, salidas.isEmpty() ? null : salidas.getFirst());
    }
}
