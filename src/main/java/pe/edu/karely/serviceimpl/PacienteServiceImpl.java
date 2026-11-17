package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.PacienteService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteServiceImpl implements PacienteService {
    private final PacienteRepository repository;
    private final PermisosUsuario permisos;
    private final AuditoriaRepository auditoria;
    public PacienteServiceImpl(PacienteRepository repository, PermisosUsuario permisos, AuditoriaRepository auditoria) {
        this.repository = repository;
        this.permisos = permisos;
        this.auditoria = auditoria;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarPacientes(long actor) {
        permisos.exigir(actor,"ADMINISTRADORA","RECEPCIONISTA");
        return repository.listarPacientes();
    }

    @Override
    @Transactional
    public Map<String, Object> registrarPaciente(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA","RECEPCIONISTA");
        Long id=repository.registrarPaciente(i.required("tipoDocumento"), i.required("numeroDocumento"), i.required("nombres"), i.required("apellidos"), i.text("fechaNacimiento")==null||i.text("fechaNacimiento").isBlank()?null:java.sql.Date.valueOf(i.date("fechaNacimiento")), i.text("telefono"), i.text("correo"), i.text("direccion"));
        auditoria.registrar(actor,"PACIENTE",id,"CREAR","Alta de paciente via API");
        return RespuestasOperacion.resultado("paciente-crear",id);
    }

    @Override
    @Transactional
    public Map<String, Object> editarPaciente(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA","RECEPCIONISTA");
        int n=repository.editarPaciente(i.required("nombres"), i.required("apellidos"), i.text("telefono"), i.text("correo"), i.text("direccion"), i.required("estado"), i.id("idPaciente"));
        if(n!=1)throw new IllegalArgumentException("Paciente no encontrado");
        auditoria.registrar(actor,"PACIENTE",i.id("idPaciente"),"ACTUALIZAR","Actualizacion de paciente via API");
        return RespuestasOperacion.resultado("paciente-editar",i.id("idPaciente"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> buscarPaciente(String tipo, String documento, long actor) {
        permisos.exigir(actor, "ADMINISTRADORA", "RECEPCIONISTA");
        return repository.buscarPaciente(tipo.trim().toUpperCase(java.util.Locale.ROOT), documento.trim());
    }
}
