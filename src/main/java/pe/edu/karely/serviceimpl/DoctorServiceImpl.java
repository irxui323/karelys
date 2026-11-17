package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.DoctorService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository repository;
    private final PermisosUsuario permisos;
    private final AuditoriaRepository auditoria;
    private final UsuarioRepository usuarios;
    public DoctorServiceImpl(DoctorRepository repository, PermisosUsuario permisos, AuditoriaRepository auditoria, UsuarioRepository usuarios) {
        this.repository = repository;
        this.permisos = permisos;
        this.auditoria = auditoria;
        this.usuarios = usuarios;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarDoctores(long actor) {
        String rol = permisos.rol(actor);
        permisos.exigir(actor,"ADMINISTRADORA","RECEPCIONISTA","DOCTOR","TESORERA");
        return rol.equals("DOCTOR") ? repository.listarDoctoresDelMedico(actor)
        : rol.equals("TESORERA") ? repository.listarDoctoresParaTesoreria()
        : repository.listarDoctoresGeneral();
    }

    @Override
    @Transactional
    public Map<String, Object> registrarDoctor(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        Long id=repository.registrarDoctor(i.required("tipoDocumento"), i.required("numeroDocumento"), i.required("nombres"), i.required("apellidos"), i.text("telefono"), i.text("correo"), i.optionalId("idUsuario"));
        auditoria.registrar(actor,"DOCTOR",id,"CREAR","Alta de doctor via API");
        return RespuestasOperacion.resultado("doctor-crear",id);
    }

    @Override
    @Transactional
    public Map<String, Object> editarDoctor(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        int n=repository.editarDoctor(i.required("nombres"), i.required("apellidos"), i.text("telefono"), i.text("correo"), i.required("estado"), i.id("idDoctor"));
        if(n!=1)throw new IllegalArgumentException("Doctor no encontrado");
        auditoria.registrar(actor,"DOCTOR",i.id("idDoctor"),"ACTUALIZAR","Actualizacion de doctor via API");
        return RespuestasOperacion.resultado("doctor-editar",i.id("idDoctor"));
    }

    @Override
    @Transactional
    public Map<String, Object> vincularUsuario(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        Long usr=i.id("idUsuario");
        Map<String,Object> usrOk=usuarios.buscarDoctorActivo(usr);
        if(usrOk==null)throw new IllegalArgumentException("Se requiere un usuario activo con rol DOCTOR");
        int n=repository.vincularUsuarioEnTabla(usr, i.id("idDoctor"));
        if(n!=1)throw new IllegalArgumentException("Doctor no encontrado");
        auditoria.registrar(actor,"DOCTOR",i.id("idDoctor"),"VINCULAR_USUARIO","Asignacion de cuenta a doctor");
        return RespuestasOperacion.resultado("doctor-vincular-usuario",i.id("idDoctor"));
    }

    @Override
    @Transactional
    public Map<String, Object> asignarEspecialidad(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");repository.asignarEspecialidad(i.id("idDoctor"), i.id("idEspecialidad"));
        auditoria.registrar(actor,"DOCTOR_ESPECIALIDAD",i.id("idDoctor"),"ASIGNAR","Asignacion de especialidad");
        return RespuestasOperacion.resultado("doctor-especialidad",null);
    }

    @Override
    @Transactional
    public Map<String, Object> validarDocumentacion(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        return RespuestasOperacion.procedimiento("SP_VALIDAR_DOCUMENTACION", repository.validarDocumentacion(actor, actor, i.id("idDoctor")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> buscarPorDocumento(String documento, long actor) {
        permisos.exigir(actor, "ADMINISTRADORA", "RECEPCIONISTA");
        return repository.buscarPorDocumento(documento.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarPendientes(long actor) {
        permisos.exigir(actor, "ADMINISTRADORA");
        return repository.listarPendientes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarConDocumentacionCompleta(long actor) {
        permisos.exigir(actor, "ADMINISTRADORA");
        return repository.listarConDocumentacionCompleta();
    }

    @Override
    @Transactional
    public Map<String, Object> rechazarDocumentacion(ApiInput entrada, long actor) {
        permisos.exigir(actor, "ADMINISTRADORA");
        long id = entrada.id("idDoctor");
        if (repository.rechazarDocumentacion(id) != 1) {
            throw new IllegalArgumentException("Doctor activo no encontrado");
        }
        auditoria.registrar(actor, "DOCTOR", id, "RECHAZAR", "Documentación rechazada");
        return RespuestasOperacion.resultado("doctor-rechazar-documentacion", id);
    }
}
