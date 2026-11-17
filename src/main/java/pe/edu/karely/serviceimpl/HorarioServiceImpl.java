package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.HorarioService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HorarioServiceImpl implements HorarioService {
    private final HorarioRepository repository;
    private final PermisosUsuario permisos;
    public HorarioServiceImpl(HorarioRepository repository, PermisosUsuario permisos) {
        this.repository = repository;
        this.permisos = permisos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarHorarios(long actor) {
        String rol = permisos.rol(actor);
        permisos.exigir(actor,"ADMINISTRADORA","RECEPCIONISTA","DOCTOR");
        return rol.equals("DOCTOR") ? repository.listarHorariosDelMedico(actor)
        : repository.listarHorariosGeneral();
    }

    @Override
    @Transactional
    public Map<String, Object> registrarHorario(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA","DOCTOR");
        return RespuestasOperacion.procedimiento("SP_REGISTRAR_DISPONIBILIDAD", repository.registrarHorario(actor, actor, i.id("idDoctor"), i.date("fecha"), i.id("idFranja")));
    }

}
