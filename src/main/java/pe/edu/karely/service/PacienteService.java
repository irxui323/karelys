package pe.edu.karely.service;

import java.util.List;
import java.util.Map;
import pe.edu.karely.dto.ApiInput;

public interface PacienteService {
    List<Map<String, Object>> listarPacientes(long actor);
    Map<String, Object> registrarPaciente(ApiInput i, long actor);
    Map<String, Object> editarPaciente(ApiInput i, long actor);

    List<Map<String, Object>> buscarPaciente(String tipo, String documento, long actor);
}
