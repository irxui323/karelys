package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.EspecialidadService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EspecialidadServiceImpl implements EspecialidadService {
    private final EspecialidadRepository repository;
    private final PermisosUsuario permisos;
    private final AuditoriaRepository auditoria;
    public EspecialidadServiceImpl(EspecialidadRepository repository, PermisosUsuario permisos, AuditoriaRepository auditoria) {
        this.repository = repository;
        this.permisos = permisos;
        this.auditoria = auditoria;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarEspecialidades(long actor) {
        permisos.rol(actor);
        return repository.listarEspecialidades();
    }

    @Override
    @Transactional
    public Map<String, Object> registrarEspecialidad(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        Long id=repository.registrarEspecialidad(i.required("nombre"), i.money("precioConsulta"));
        auditoria.registrar(actor,"ESPECIALIDAD",id,"CREAR","Alta de especialidad");
        return RespuestasOperacion.resultado("especialidad-crear",id);
    }

    @Override
    @Transactional
    public Map<String, Object> editarEspecialidad(ApiInput i, long actor) {
        permisos.exigir(actor, "ADMINISTRADORA");
        long id = i.id("idEspecialidad");
        if (repository.editarEspecialidad(id, i.required("nombre"), i.money("precioConsulta"), i.required("estado")) != 1) {
            throw new IllegalArgumentException("Registro no encontrado");
        }
        auditoria.registrar(actor, "ESPECIALIDAD", id, "ACTUALIZAR", "Edición del catálogo");
        return RespuestasOperacion.resultado("especialidad-editar", id);
    }
}
