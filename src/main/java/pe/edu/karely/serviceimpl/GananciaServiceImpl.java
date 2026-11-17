package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.GananciaService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GananciaServiceImpl implements GananciaService {
    private final GananciaRepository repository;
    private final PermisosUsuario permisos;
    public GananciaServiceImpl(GananciaRepository repository, PermisosUsuario permisos) {
        this.repository = repository;
        this.permisos = permisos;
    }
    @Override
    @Transactional
    public Map<String, Object> cancelarReprogramada(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        return RespuestasOperacion.procedimiento("SP_CANCELAR_REPROGRAMADA", repository.cancelarReprogramada(actor, actor, i.id("idCita"), i.required("motivo")));
    }

}
