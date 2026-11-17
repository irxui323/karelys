package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.ConsultorioService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultorioServiceImpl implements ConsultorioService {
    private final ConsultorioRepository repository;
    private final PermisosUsuario permisos;
    private final AuditoriaRepository auditoria;
    public ConsultorioServiceImpl(ConsultorioRepository repository, PermisosUsuario permisos, AuditoriaRepository auditoria) {
        this.repository = repository;
        this.permisos = permisos;
        this.auditoria = auditoria;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarConsultorios(long actor) {
        permisos.rol(actor);
        return repository.listarConsultorios();
    }

    @Override
    @Transactional
    public Map<String, Object> registrarConsultorio(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        Long id=repository.registrarConsultorio(i.required("nombre"), i.text("ubicacion"));
        auditoria.registrar(actor,"CONSULTORIO",id,"CREAR","Alta de consultorio");
        return RespuestasOperacion.resultado("consultorio-crear",id);
    }

    @Override
    @Transactional
    public Map<String, Object> editarConsultorio(ApiInput i, long actor) {
        permisos.exigir(actor, "ADMINISTRADORA");
        long id = i.id("idConsultorio");
        if (repository.editarConsultorio(id, i.required("nombre"), i.text("ubicacion"), i.required("estado")) != 1) {
            throw new IllegalArgumentException("Registro no encontrado");
        }
        auditoria.registrar(actor, "CONSULTORIO", id, "ACTUALIZAR", "Edición del catálogo");
        return RespuestasOperacion.resultado("consultorio-editar", id);
    }
}
