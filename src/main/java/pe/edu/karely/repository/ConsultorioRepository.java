package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultorioRepository {
    private final OracleJdbc jdbc;
    public ConsultorioRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarConsultorios() {
        return jdbc.list("SELECT ID_CONSULTORIO,NOMBRE,UBICACION,ESTADO FROM CONSULTORIO WHERE ESTADO='ACTIVO' ORDER BY NOMBRE");
    }
    public Long registrarConsultorio(String nombre, String ubicacion) {
        return jdbc.insertReturning("begin insert into CONSULTORIO(NOMBRE,UBICACION) values(?,?) returning ID_CONSULTORIO into ?; end;", nombre, ubicacion);
    }

    public int editarConsultorio(long id, String nombre, String ubicacion, String estado) {
        return jdbc.update("UPDATE CONSULTORIO SET NOMBRE=?, UBICACION=?, ESTADO=? WHERE ID_CONSULTORIO=?", nombre, ubicacion, estado, id);
    }
}
