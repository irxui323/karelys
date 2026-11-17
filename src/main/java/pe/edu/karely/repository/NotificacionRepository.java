package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class NotificacionRepository {
    private final OracleJdbc jdbc;
    public NotificacionRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarNotificaciones() {
        return jdbc.list("SELECT * FROM VW_CONTROL_NOTIFICACIONES FETCH FIRST 200 ROWS ONLY");
    }
    public List<Object> encolarNotificacion(long actor, long usuario, Long idCita, String tipo) {
        return jdbc.call("SP_ENCOLAR_NOTIFICACION_CITA", actor, usuario, idCita, tipo, OracleJdbc.outNumber());
    }


    public List<Object> reclamar(long operador) {
        return jdbc.call("SP_TOMAR_NOTIFICACION", operador, operador,
                OracleJdbc.outNumber(), OracleJdbc.outText(), OracleJdbc.outText(),
                OracleJdbc.outText(), OracleJdbc.outNumber());
    }

    public void registrarResultado(long operador, long id, int intento, String exito, String detalle) {
        jdbc.call("SP_REGISTRAR_RESULTADO_ENVIO", operador, operador, id, intento, exito, detalle);
    }
}
