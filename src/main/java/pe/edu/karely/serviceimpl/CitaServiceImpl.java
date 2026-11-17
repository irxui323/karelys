package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.CitaService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaServiceImpl implements CitaService {
    private final CitaRepository repository;
    private final PermisosUsuario permisos;
    public CitaServiceImpl(CitaRepository repository, PermisosUsuario permisos) {
        this.repository = repository;
        this.permisos = permisos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarCitas(long actor) {
        String rol = permisos.rol(actor);
        permisos.exigir(actor,"ADMINISTRADORA","RECEPCIONISTA","CAJERA","DOCTOR");return rol.equals("DOCTOR") ? repository.listarCitasDelMedico(actor):repository.listarCitasGeneral();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarReservas(long actor) {
        permisos.exigir(actor,"ADMINISTRADORA","RECEPCIONISTA","CAJERA");
        return repository.listarReservas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarAtenciones(long actor) {
        String rol = permisos.rol(actor);
        permisos.exigir(actor,"DOCTOR","ADMINISTRADORA");return rol.equals("DOCTOR") ? repository.listarAtencionesDelMedico(actor):repository.listarAtencionesGeneral();
    }

    @Override
    @Transactional
    public Map<String, Object> bloquearTurno(ApiInput i, long actor) {
        permisos.exigir(actor,"RECEPCIONISTA","ADMINISTRADORA");
        return RespuestasOperacion.procedimiento("SP_PRE_RESERVAR_CITA", repository.bloquearTurno(actor, actor, i.id("idPaciente"), i.id("idDoctor"), i.id("idEspecialidad"), i.id("idConsultorio"), i.id("idFranja"), i.date("fecha")));
    }

    @Override
    @Transactional
    public Map<String, Object> registrarCita(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA","RECEPCIONISTA","CAJERA");
        return RespuestasOperacion.procedimiento("SP_CONFIRMAR_CITA_PAGADA", repository.registrarCita(actor, actor, i.id("idPreReserva"), i.required("metodoPago"), i.required("referenciaPago")));
    }

    @Override
    @Transactional
    public Map<String, Object> reprogramarCita(ApiInput i, long actor) {
        permisos.exigir(actor,"RECEPCIONISTA","ADMINISTRADORA");
        return RespuestasOperacion.procedimiento("SP_REPROGRAMAR_CITA", repository.reprogramarCita(actor, actor, i.id("idCita"), i.date("fechaNueva"), i.id("idFranjaNueva"), i.id("idConsultorioNuevo"), i.required("motivo"), i.required("aceptacionPaciente")));
    }

    @Override
    @Transactional
    public Map<String, Object> registrarAsistencia(ApiInput i, long actor) {
        permisos.exigir(actor,"RECEPCIONISTA");
        return RespuestasOperacion.procedimiento("SP_REGISTRAR_ASISTENCIA", repository.registrarAsistencia(actor, actor, i.id("idCita")));
    }

    @Override
    @Transactional
    public Map<String, Object> iniciarAtencion(ApiInput i, long actor) {
        permisos.exigir(actor,"DOCTOR");
        return RespuestasOperacion.procedimiento("SP_INICIAR_ATENCION", repository.iniciarAtencion(actor, actor, i.id("idCita")));
    }

    @Override
    @Transactional
    public Map<String, Object> registrarDiagnostico(ApiInput i, long actor) {
        permisos.exigir(actor,"DOCTOR");
        return RespuestasOperacion.procedimiento("SP_REGISTRAR_DIAGNOSTICO", repository.registrarDiagnostico(actor, actor, i.id("idCita"), i.required("diagnostico"), i.text("observaciones")));
    }

    @Override
    @Transactional
    public Map<String, Object> registrarReceta(ApiInput i, long actor) {
        permisos.exigir(actor,"DOCTOR");
        return RespuestasOperacion.procedimiento("SP_CREAR_RECETA", repository.registrarReceta(actor, actor, i.id("idCita"), i.required("indicaciones")));
    }

    @Override
    @Transactional
    public Map<String, Object> agregarMedicamento(ApiInput i, long actor) {
        permisos.exigir(actor,"DOCTOR");
        return RespuestasOperacion.procedimiento("SP_AGREGAR_MEDICAMENTO", repository.agregarMedicamento(actor, actor, i.id("idReceta"), i.required("medicamento"), i.required("dosis"), i.required("frecuencia"), i.required("duracion"), i.text("observaciones")));
    }

    @Override
    @Transactional
    public Map<String, Object> emitirRecibo(ApiInput i, long actor) {
        permisos.exigir(actor,"DOCTOR");
        return RespuestasOperacion.procedimiento("SP_EMITIR_RECIBO", repository.emitirRecibo(actor, actor, i.id("idCita"), i.required("numeroRecibo")));
    }

    @Override
    @Transactional
    public Map<String, Object> entregarRecibo(ApiInput i, long actor) {
        permisos.exigir(actor,"DOCTOR");
        return RespuestasOperacion.procedimiento("SP_ENTREGAR_RECIBO", repository.entregarRecibo(actor, actor, i.id("idCita"), i.required("medioEntrega")));
    }

    @Override
    @Transactional
    public Map<String, Object> cerrarAtencion(ApiInput i, long actor) {
        permisos.exigir(actor,"DOCTOR");
        return RespuestasOperacion.procedimiento("SP_CERRAR_ATENCION", repository.cerrarAtencion(actor, actor, i.id("idCita")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> filtrarAgendaMedica(long actor, java.time.LocalDate fecha) {
        permisos.exigir(actor, "DOCTOR");
        return repository.filtrarAgendaMedica(actor, fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> detalleCitaMedica(long actor, long idCita) {
        permisos.exigir(actor, "DOCTOR");
        var cita = repository.detalleCitaMedica(actor, idCita);
        if (cita == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Cita no encontrada");
        }
        return cita;
    }
}
