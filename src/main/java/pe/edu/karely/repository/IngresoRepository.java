package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class IngresoRepository {
    private final OracleJdbc jdbc;
    public IngresoRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarIngresos() {
        return jdbc.list("SELECT * FROM VW_INGRESOS_GESTORA ORDER BY FECHA_REGISTRO DESC FETCH FIRST 200 ROWS ONLY");
    }

    public List<Map<String, Object>> sumarIngresos(long doctor) {
        return jdbc.list("""
                SELECT NVL(SUM(MONTO_INGRESO), 0) AS TOTAL
                FROM VW_INGRESOS_GESTORA WHERE ID_DOCTOR = ?
                """, doctor);
    }
}
