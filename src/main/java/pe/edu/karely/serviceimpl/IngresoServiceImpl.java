package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.IngresoService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IngresoServiceImpl implements IngresoService {
    private final IngresoRepository repository;
    private final PermisosUsuario permisos;
    public IngresoServiceImpl(IngresoRepository repository, PermisosUsuario permisos) {
        this.repository = repository;
        this.permisos = permisos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarIngresos(long actor) {
        permisos.exigir(actor,"TESORERA","ADMINISTRADORA");
        return repository.listarIngresos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> sumarIngresos(long doctor, long actor) {
        permisos.exigir(actor, "ADMINISTRADORA", "TESORERA");
        return repository.sumarIngresos(doctor);
    }
}
