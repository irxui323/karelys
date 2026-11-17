package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.TurnoService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TurnoServiceImpl implements TurnoService {
    private final TurnoRepository repository;
    private final PermisosUsuario permisos;
    private final AuditoriaRepository auditoria;
    public TurnoServiceImpl(TurnoRepository repository, PermisosUsuario permisos, AuditoriaRepository auditoria) {
        this.repository = repository;
        this.permisos = permisos;
        this.auditoria = auditoria;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarTurnos(long actor) {
        permisos.rol(actor);
        return repository.listarTurnos();
    }

    @Override
    @Transactional
    public Map<String, Object> registrarTurno(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        Long id=repository.registrarTurno(i.required("horaInicio"), i.required("horaFin"));
        auditoria.registrar(actor,"FRANJA_HORARIA",id,"CREAR","Alta de franja horaria");
        return RespuestasOperacion.resultado("franja-crear",id);
    }

    @Override
    @Transactional
    public Map<String, Object> editarTurno(ApiInput i, long actor) {
        permisos.exigir(actor, "ADMINISTRADORA");
        long id = i.id("idFranja");
        if (repository.editarTurno(id, i.required("horaInicio"), i.required("horaFin"), i.required("estado")) != 1) {
            throw new IllegalArgumentException("Registro no encontrado");
        }
        auditoria.registrar(actor, "FRANJA_HORARIA", id, "ACTUALIZAR", "Edición del catálogo");
        return RespuestasOperacion.resultado("franja-editar", id);
    }
}
