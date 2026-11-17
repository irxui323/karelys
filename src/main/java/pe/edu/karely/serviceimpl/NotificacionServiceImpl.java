package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.NotificacionService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificacionServiceImpl implements NotificacionService {
    private final NotificacionRepository repository;
    private final PermisosUsuario permisos;
    public NotificacionServiceImpl(NotificacionRepository repository, PermisosUsuario permisos) {
        this.repository = repository;
        this.permisos = permisos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarNotificaciones(long actor) {
        permisos.exigir(actor,"ADMINISTRADORA","TESORERA");
        return repository.listarNotificaciones();
    }

    @Override
    @Transactional
    public Map<String, Object> encolarNotificacion(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA","RECEPCIONISTA");
        return RespuestasOperacion.procedimiento("SP_ENCOLAR_NOTIFICACION_CITA", repository.encolarNotificacion(actor, actor, i.id("idCita"), i.required("tipo")));
    }

}
