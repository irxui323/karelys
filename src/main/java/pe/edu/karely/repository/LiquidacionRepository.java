package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class LiquidacionRepository {
    private final OracleJdbc jdbc;
    public LiquidacionRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarLiquidaciones() {
        return jdbc.list("SELECT * FROM VW_SALDOS_LIQUIDACIONES ORDER BY ID_LIQUIDACION DESC FETCH FIRST 200 ROWS ONLY");
    }
    public List<Map<String, Object>> listarPagos() {
        return jdbc.list("SELECT ID_PAGO_DOCTOR,ID_LIQUIDACION,MONTO,METODO_PAGO,NUMERO_OPERACION,NUMERO_BOLETA,FECHA_PAGO FROM PAGO_DOCTOR ORDER BY ID_PAGO_DOCTOR DESC FETCH FIRST 200 ROWS ONLY");
    }
    public List<Object> generarLiquidacion(long actor, long usuario, Long idDoctor, java.time.LocalDate fechaInicio, java.time.LocalDate fechaFin) {
        return jdbc.call("SP_GENERAR_LIQUIDACION", actor, usuario, idDoctor, fechaInicio, fechaFin, OracleJdbc.outNumber());
    }
    public List<Object> pagarDoctor(long actor, long usuario, Long idLiquidacion, java.math.BigDecimal monto, String metodoPago, String numeroOperacion, String numeroBoleta) {
        return jdbc.call("SP_PAGAR_DOCTOR", actor, usuario, idLiquidacion, monto, metodoPago, numeroOperacion, numeroBoleta, OracleJdbc.outNumber());
    }

}
