package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class CitaRepository {
    private final OracleJdbc jdbc;
    public CitaRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarCitasDelMedico(long usuario) {
        return jdbc.list("SELECT A.* FROM VW_AGENDA_CITAS A JOIN DOCTOR D ON A.ID_DOCTOR=D.ID_DOCTOR WHERE D.ID_USUARIO=? ORDER BY A.FECHA_CITA DESC,A.HORA_INICIO FETCH FIRST 200 ROWS ONLY", usuario);
    }
    public List<Map<String, Object>> listarCitasGeneral() {
        return jdbc.list("SELECT * FROM VW_AGENDA_CITAS ORDER BY FECHA_CITA DESC, HORA_INICIO FETCH FIRST 400 ROWS ONLY");
    }
    public List<Map<String, Object>> listarReservas() {
        return jdbc.list("SELECT ID_PRE_RESERVA,ID_PACIENTE,ID_DOCTOR,ID_CONSULTORIO,ID_FRANJA,FECHA_CITA,MONTO,FECHA_EXPIRACION,ESTADO FROM PRE_RESERVA_CITA WHERE ESTADO='BLOQUEADA' AND FECHA_EXPIRACION>SYSTIMESTAMP ORDER BY ID_PRE_RESERVA DESC FETCH FIRST 100 ROWS ONLY");
    }
    public List<Map<String, Object>> listarAtencionesDelMedico(long usuario) {
        return jdbc.list("SELECT A.ID_ATENCION,A.ID_CITA,A.ID_DOCTOR,A.FECHA_INICIO,A.FECHA_CIERRE,A.ESTADO FROM ATENCION_MEDICA A JOIN DOCTOR D ON D.ID_DOCTOR=A.ID_DOCTOR WHERE D.ID_USUARIO=? ORDER BY A.FECHA_INICIO DESC FETCH FIRST 150 ROWS ONLY", usuario);
    }
    public List<Map<String, Object>> listarAtencionesGeneral() {
        return jdbc.list("SELECT ID_ATENCION,ID_CITA,ID_DOCTOR,FECHA_INICIO,FECHA_CIERRE,ESTADO FROM ATENCION_MEDICA ORDER BY FECHA_INICIO DESC FETCH FIRST 150 ROWS ONLY");
    }
    public List<Object> bloquearTurno(long actor, long usuario, Long idPaciente, Long idDoctor, Long idEspecialidad, Long idConsultorio, Long idFranja, java.time.LocalDate fecha) {
        return jdbc.call("SP_PRE_RESERVAR_CITA", actor, usuario, idPaciente, idDoctor, idEspecialidad, idConsultorio, idFranja, fecha, OracleJdbc.outNumber());
    }
    public List<Object> registrarCita(long actor, long usuario, Long idPreReserva, String metodoPago, String referenciaPago) {
        return jdbc.call("SP_CONFIRMAR_CITA_PAGADA", actor, usuario, idPreReserva, metodoPago, referenciaPago, OracleJdbc.outNumber());
    }
    public List<Object> reprogramarCita(long actor, long usuario, Long idCita, java.time.LocalDate fechaNueva, Long idFranjaNueva, Long idConsultorioNuevo, String motivo, String aceptacionPaciente) {
        return jdbc.call("SP_REPROGRAMAR_CITA", actor, usuario, idCita, fechaNueva, idFranjaNueva, idConsultorioNuevo, motivo, aceptacionPaciente);
    }
    public List<Object> registrarAsistencia(long actor, long usuario, Long idCita) {
        return jdbc.call("SP_REGISTRAR_ASISTENCIA", actor, usuario, idCita, OracleJdbc.outNumber());
    }
    public List<Object> iniciarAtencion(long actor, long usuario, Long idCita) {
        return jdbc.call("SP_INICIAR_ATENCION", actor, usuario, idCita, OracleJdbc.outNumber());
    }
    public List<Object> registrarDiagnostico(long actor, long usuario, Long idCita, String diagnostico, String observaciones) {
        return jdbc.call("SP_REGISTRAR_DIAGNOSTICO", actor, usuario, idCita, diagnostico, observaciones);
    }
    public List<Object> registrarReceta(long actor, long usuario, Long idCita, String indicaciones) {
        return jdbc.call("SP_CREAR_RECETA", actor, usuario, idCita, indicaciones, OracleJdbc.outNumber());
    }
    public List<Object> agregarMedicamento(long actor, long usuario, Long idReceta, String medicamento, String dosis, String frecuencia, String duracion, String observaciones) {
        return jdbc.call("SP_AGREGAR_MEDICAMENTO", actor, usuario, idReceta, medicamento, dosis, frecuencia, duracion, OracleJdbc.outNumber(), observaciones);
    }
    public List<Object> emitirRecibo(long actor, long usuario, Long idCita, String numeroRecibo) {
        return jdbc.call("SP_EMITIR_RECIBO", actor, usuario, idCita, numeroRecibo, OracleJdbc.outNumber());
    }
    public List<Object> entregarRecibo(long actor, long usuario, Long idCita, String medioEntrega) {
        return jdbc.call("SP_ENTREGAR_RECIBO", actor, usuario, idCita, medioEntrega);
    }
    public List<Object> cerrarAtencion(long actor, long usuario, Long idCita) {
        return jdbc.call("SP_CERRAR_ATENCION", actor, usuario, idCita);
    }

    public List<Map<String, Object>> filtrarAgendaMedica(long actor, java.time.LocalDate fecha) {
        return jdbc.list("""
                SELECT A.* FROM VW_AGENDA_CITAS A JOIN DOCTOR D ON D.ID_DOCTOR = A.ID_DOCTOR
                WHERE D.ID_USUARIO = ? AND A.FECHA_CITA = ?
                ORDER BY A.HORA_INICIO, A.ID_CITA
                """, actor, fecha);
    }

    public Map<String, Object> detalleCitaMedica(long actor, long idCita) {
        return jdbc.one("""
                SELECT A.* FROM VW_AGENDA_CITAS A JOIN DOCTOR D ON D.ID_DOCTOR = A.ID_DOCTOR
                WHERE D.ID_USUARIO = ? AND A.ID_CITA = ?
                """, actor, idCita);
    }
}
