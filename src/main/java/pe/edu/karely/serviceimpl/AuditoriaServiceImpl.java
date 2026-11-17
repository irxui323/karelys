package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.AuditoriaService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {
    private final AuditoriaRepository repository;
    private final PermisosUsuario permisos;
    public AuditoriaServiceImpl(AuditoriaRepository repository, PermisosUsuario permisos) {
        this.repository = repository;
        this.permisos = permisos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarAuditoria(long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        return repository.listarAuditoria();
    }

}
