package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class ReporteRepository {
    private final OracleJdbc jdbc;
    public ReporteRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> generarResumen() {
        return jdbc.list("SELECT * FROM VW_RESUMEN_CITAS_DIARIAS ORDER BY FECHA_CITA DESC FETCH FIRST 100 ROWS ONLY");
    }

    public List<Map<String, Object>> generarReporte(java.time.LocalDate desde, java.time.LocalDate hasta, String estado) {
        return jdbc.list("""
                SELECT * FROM VW_AGENDA_CITAS
                WHERE FECHA_CITA >= ? AND FECHA_CITA <= ? AND (? IS NULL OR ESTADO_CITA = ?)
                ORDER BY FECHA_CITA DESC, HORA_INICIO, ID_CITA
                """, desde, hasta, estado, estado);
    }
}
