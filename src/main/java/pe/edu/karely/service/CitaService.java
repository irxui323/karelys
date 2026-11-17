package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface CitaService {
    List<Map<String, Object>> listarCitas(long actor);
    List<Map<String, Object>> listarReservas(long actor);
    List<Map<String, Object>> listarAtenciones(long actor);
    Map<String, Object> bloquearTurno(ApiInput i, long actor);
    Map<String, Object> registrarCita(ApiInput i, long actor);
    Map<String, Object> reprogramarCita(ApiInput i, long actor);
    Map<String, Object> registrarAsistencia(ApiInput i, long actor);
    Map<String, Object> iniciarAtencion(ApiInput i, long actor);
    Map<String, Object> registrarDiagnostico(ApiInput i, long actor);
    Map<String, Object> registrarReceta(ApiInput i, long actor);
    Map<String, Object> agregarMedicamento(ApiInput i, long actor);
    Map<String, Object> emitirRecibo(ApiInput i, long actor);
    Map<String, Object> entregarRecibo(ApiInput i, long actor);
    Map<String, Object> cerrarAtencion(ApiInput i, long actor);

    List<Map<String, Object>> filtrarAgendaMedica(long actor, java.time.LocalDate fecha);

    Map<String, Object> detalleCitaMedica(long actor, long idCita);
}
