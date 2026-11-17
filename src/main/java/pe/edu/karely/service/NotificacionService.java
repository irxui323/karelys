package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface NotificacionService {
    List<Map<String, Object>> listarNotificaciones(long actor);
    Map<String, Object> encolarNotificacion(ApiInput i, long actor);
}
