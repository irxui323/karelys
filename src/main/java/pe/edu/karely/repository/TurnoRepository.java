package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class TurnoRepository {
    private final OracleJdbc jdbc;
    public TurnoRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarTurnos() {
        return jdbc.list("SELECT ID_FRANJA,HORA_INICIO,HORA_FIN,ESTADO FROM FRANJA_HORARIA WHERE ESTADO='ACTIVO' ORDER BY HORA_INICIO");
    }
    public Long registrarTurno(String horaInicio, String horaFin) {
        return jdbc.insertReturning("begin insert into FRANJA_HORARIA(HORA_INICIO,HORA_FIN) values(?,?) returning ID_FRANJA into ?; end;", horaInicio, horaFin);
    }

    public int editarTurno(long id, String horaInicio, String horaFin, String estado) {
        return jdbc.update("UPDATE FRANJA_HORARIA SET HORA_INICIO=?, HORA_FIN=?, ESTADO=? WHERE ID_FRANJA=?", horaInicio, horaFin, estado, id);
    }
}
