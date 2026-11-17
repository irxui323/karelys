package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class PacienteRepository {
    private final OracleJdbc jdbc;
    public PacienteRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarPacientes() {
        return jdbc.list("SELECT ID_PACIENTE,TIPO_DOCUMENTO,NUMERO_DOCUMENTO,NOMBRES,APELLIDOS,FECHA_NACIMIENTO,TELEFONO,CORREO,DIRECCION,ESTADO FROM PACIENTE ORDER BY ID_PACIENTE DESC FETCH FIRST 200 ROWS ONLY");
    }
    public Long registrarPaciente(String tipoDocumento, String numeroDocumento, String nombres, String apellidos, java.sql.Date fechaNacimiento, String telefono, String correo, String direccion) {
        return jdbc.insertReturning("begin insert into PACIENTE(TIPO_DOCUMENTO,NUMERO_DOCUMENTO,NOMBRES,APELLIDOS,FECHA_NACIMIENTO,TELEFONO,CORREO,DIRECCION) values(?,?,?,?,?,?,?,?) returning ID_PACIENTE into ?; end;", tipoDocumento, numeroDocumento, nombres, apellidos, fechaNacimiento, telefono, correo, direccion);
    }
    public int editarPaciente(String nombres, String apellidos, String telefono, String correo, String direccion, String estado, Long idPaciente) {
        return jdbc.update("UPDATE PACIENTE SET NOMBRES=?,APELLIDOS=?,TELEFONO=?,CORREO=?,DIRECCION=?,ESTADO=? WHERE ID_PACIENTE=?", nombres, apellidos, telefono, correo, direccion, estado, idPaciente);
    }

    public List<Map<String, Object>> buscarPaciente(String tipo, String documento) {
        return jdbc.list("""
                SELECT ID_PACIENTE,TIPO_DOCUMENTO,NUMERO_DOCUMENTO,NOMBRES,APELLIDOS,
                       FECHA_NACIMIENTO,TELEFONO,CORREO,DIRECCION,ESTADO
                FROM PACIENTE WHERE TIPO_DOCUMENTO = ? AND NUMERO_DOCUMENTO = ?
                """, tipo.trim().toUpperCase(java.util.Locale.ROOT), documento.trim());
    }
}
