package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class ParametroRepository {
    private final OracleJdbc jdbc;
    public ParametroRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarParametros() {
        return jdbc.list("SELECT ID_PARAMETRO,NOMBRE,VALOR,DESCRIPCION,ESTADO FROM PARAMETRO_SISTEMA ORDER BY NOMBRE");
    }
    public List<Object> configurarParametro(long actor, long usuario, String nombre, String valor) {
        return jdbc.call("SP_CAMBIAR_PARAMETRO", actor, usuario, nombre, valor);
    }

}
