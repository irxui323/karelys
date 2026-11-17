package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface DoctorService {
    List<Map<String, Object>> listarDoctores(long actor);
    Map<String, Object> registrarDoctor(ApiInput i, long actor);
    Map<String, Object> editarDoctor(ApiInput i, long actor);
    Map<String, Object> vincularUsuario(ApiInput i, long actor);
    Map<String, Object> asignarEspecialidad(ApiInput i, long actor);
    Map<String, Object> validarDocumentacion(ApiInput i, long actor);

    List<Map<String, Object>> buscarPorDocumento(String documento, long actor);

    List<Map<String, Object>> listarPendientes(long actor);

    List<Map<String, Object>> listarConDocumentacionCompleta(long actor);

    Map<String, Object> rechazarDocumentacion(ApiInput entrada, long actor);
}
