package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import pe.edu.karely.persistence.OracleJdbc;

@Repository
public class DocumentoDoctorRepository {
    private final OracleJdbc jdbc;

    public DocumentoDoctorRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }

    public Long insertar(long doctor, String tipo, String nombre, String mime, byte[] bytes) {
        return jdbc.insertReturning("begin insert into DOCUMENTO_DOCTOR(ID_DOCTOR,TIPO_DOCUMENTO,NOMBRE_ARCHIVO,TIPO_MIME,ARCHIVO) values(?,?,?,?,?) returning ID_DOCUMENTO into ?; end;", doctor, tipo, nombre, mime, bytes);
    }

    public List<Map<String, Object>> listarDocumentos(long doctor) {
        return jdbc.list("SELECT ID_DOCUMENTO,ID_DOCTOR,TIPO_DOCUMENTO,NOMBRE_ARCHIVO,TIPO_MIME,ESTADO,FECHA_VALIDACION FROM DOCUMENTO_DOCTOR WHERE ID_DOCTOR=? ORDER BY ID_DOCUMENTO DESC", doctor);
    }

    public List<Documento> obtener(long id) {
        return jdbc.query("SELECT NOMBRE_ARCHIVO,TIPO_MIME,ARCHIVO FROM DOCUMENTO_DOCTOR WHERE ID_DOCUMENTO=?",
                (rs, row) -> new Documento(rs.getString("NOMBRE_ARCHIVO"), rs.getString("TIPO_MIME"), rs.getBytes("ARCHIVO")), id);
    }

    public record Documento(String nombre, String mime, byte[] bytes) {}
}
