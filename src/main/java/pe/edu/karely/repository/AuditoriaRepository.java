package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class AuditoriaRepository {
    private final OracleJdbc jdbc;
    public AuditoriaRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarAuditoria() {
        return jdbc.list("SELECT ID_AUDITORIA,ID_USUARIO,TABLA_AFECTADA,ID_REGISTRO,ACCION,DETALLE,FECHA_OPERACION,ORIGEN FROM AUDITORIA ORDER BY FECHA_OPERACION DESC FETCH FIRST 200 ROWS ONLY");
    }
    public void registrar(long actor, String tabla, Object id, String accion, String detalle) {
        jdbc.update("INSERT INTO AUDITORIA (ID_USUARIO,TABLA_AFECTADA,ID_REGISTRO,ACCION,DETALLE,ORIGEN) VALUES (?,?,?,?,?,'USUARIO')", actor, tabla, id, accion, detalle);
    }

}
