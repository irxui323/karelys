package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class GananciaRepository {
    private final OracleJdbc jdbc;
    public GananciaRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Object> cancelarReprogramada(long actor, long usuario, Long idCita, String motivo) {
        return jdbc.call("SP_CANCELAR_REPROGRAMADA", actor, usuario, idCita, motivo);
    }

}
