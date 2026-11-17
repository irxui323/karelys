package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class EspecialidadRepository {
    private final OracleJdbc jdbc;
    public EspecialidadRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarEspecialidades() {
        return jdbc.list("SELECT ID_ESPECIALIDAD,NOMBRE,PRECIO_CONSULTA,ESTADO FROM ESPECIALIDAD WHERE ESTADO='ACTIVO' ORDER BY NOMBRE");
    }
    public Long registrarEspecialidad(String nombre, java.math.BigDecimal precioConsulta) {
        return jdbc.insertReturning("begin insert into ESPECIALIDAD(NOMBRE,PRECIO_CONSULTA) values(?,?) returning ID_ESPECIALIDAD into ?; end;", nombre, precioConsulta);
    }

    public int editarEspecialidad(long id, String nombre, java.math.BigDecimal precioConsulta, String estado) {
        return jdbc.update("UPDATE ESPECIALIDAD SET NOMBRE=?, PRECIO_CONSULTA=?, ESTADO=? WHERE ID_ESPECIALIDAD=?", nombre, precioConsulta, estado, id);
    }
}
