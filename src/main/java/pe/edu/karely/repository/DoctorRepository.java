package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorRepository {
    private final OracleJdbc jdbc;
    public DoctorRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarDoctoresDelMedico(long usuario) {
        return jdbc.list("SELECT ID_DOCTOR,ID_USUARIO,NOMBRES,APELLIDOS,ESTADO_DOCUMENTACION,ESTADO FROM DOCTOR WHERE ID_USUARIO=?", usuario);
    }
    public List<Map<String, Object>> listarDoctoresParaTesoreria() {
        return jdbc.list("SELECT ID_DOCTOR,NOMBRES,APELLIDOS,ESTADO FROM DOCTOR ORDER BY NOMBRES,APELLIDOS");
    }
    public List<Map<String, Object>> listarDoctoresGeneral() {
        return jdbc.list("SELECT D.ID_DOCTOR,D.ID_USUARIO,D.TIPO_DOCUMENTO,D.NUMERO_DOCUMENTO,D.NOMBRES,D.APELLIDOS,D.TELEFONO,D.CORREO,D.ESTADO_DOCUMENTACION,D.ESTADO FROM DOCTOR D ORDER BY D.ID_DOCTOR DESC FETCH FIRST 200 ROWS ONLY");
    }
    public Long registrarDoctor(String tipoDocumento, String numeroDocumento, String nombres, String apellidos, String telefono, String correo, Long idUsuario) {
        return jdbc.insertReturning("begin insert into DOCTOR(TIPO_DOCUMENTO,NUMERO_DOCUMENTO,NOMBRES,APELLIDOS,TELEFONO,CORREO,ID_USUARIO) values(?,?,?,?,?,?,?) returning ID_DOCTOR into ?; end;", tipoDocumento, numeroDocumento, nombres, apellidos, telefono, correo, idUsuario);
    }
    public int editarDoctor(String nombres, String apellidos, String telefono, String correo, String estado, Long idDoctor) {
        return jdbc.update("UPDATE DOCTOR SET NOMBRES=?,APELLIDOS=?,TELEFONO=?,CORREO=?,ESTADO=? WHERE ID_DOCTOR=?", nombres, apellidos, telefono, correo, estado, idDoctor);
    }
    public int vincularUsuarioEnTabla(Long idUsuario, Long idDoctor) {
        return jdbc.update("UPDATE DOCTOR SET ID_USUARIO=? WHERE ID_DOCTOR=?", idUsuario, idDoctor);
    }
    public int asignarEspecialidad(Long idDoctor, Long idEspecialidad) {
        return jdbc.update("INSERT INTO DOCTOR_ESPECIALIDAD(ID_DOCTOR,ID_ESPECIALIDAD) VALUES (?,?)", idDoctor, idEspecialidad);
    }
    public List<Object> validarDocumentacion(long actor, long usuario, Long idDoctor) {
        return jdbc.call("SP_VALIDAR_DOCUMENTACION", actor, usuario, idDoctor);
    }


    public void marcarDocumentacionIncompleta(long idDoctor) {
        jdbc.update("UPDATE DOCTOR SET ESTADO_DOCUMENTACION='INCOMPLETO' WHERE ID_DOCTOR=?", idDoctor);
    }

    public List<Map<String, Object>> buscarPorDocumento(String documento) {
        return jdbc.list("""
                SELECT ID_DOCTOR,TIPO_DOCUMENTO,NUMERO_DOCUMENTO,NOMBRES,APELLIDOS,ESTADO_DOCUMENTACION,ESTADO
                FROM DOCTOR WHERE NUMERO_DOCUMENTO = ? ORDER BY ID_DOCTOR
                """, documento.trim());
    }

    public List<Map<String, Object>> listarPendientes() {
        return jdbc.list("""
                SELECT ID_DOCTOR,NUMERO_DOCUMENTO,NOMBRES,APELLIDOS,ESTADO_DOCUMENTACION
                FROM DOCTOR WHERE ESTADO = 'ACTIVO' AND ESTADO_DOCUMENTACION IN ('INCOMPLETO','RECHAZADO')
                ORDER BY APELLIDOS, NOMBRES, ID_DOCTOR
                """);
    }

    public List<Map<String, Object>> listarConDocumentacionCompleta() {
        return jdbc.list("""
                SELECT ID_DOCTOR, NUMERO_DOCUMENTO, NOMBRES, APELLIDOS
                FROM DOCTOR WHERE ESTADO = 'ACTIVO' AND ESTADO_DOCUMENTACION = 'COMPLETO'
                ORDER BY APELLIDOS, NOMBRES, ID_DOCTOR
                """);
    }

    public int rechazarDocumentacion(long idDoctor) {
        return jdbc.update("""
                UPDATE DOCTOR SET ESTADO_DOCUMENTACION = 'RECHAZADO'
                WHERE ID_DOCTOR = ? AND ESTADO = 'ACTIVO'
                """, idDoctor);
    }
}
