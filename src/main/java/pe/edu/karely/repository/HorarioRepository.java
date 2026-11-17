package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class HorarioRepository {
    private final OracleJdbc jdbc;
    public HorarioRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarHorariosDelMedico(long usuario) {
        return jdbc.list("SELECT DD.ID_DISPONIBILIDAD,DD.ID_DOCTOR,DD.FECHA_TURNO,DD.ID_FRANJA,F.HORA_INICIO,F.HORA_FIN,DD.ESTADO FROM DISPONIBILIDAD_DOCTOR DD JOIN DOCTOR D ON D.ID_DOCTOR=DD.ID_DOCTOR JOIN FRANJA_HORARIA F ON F.ID_FRANJA=DD.ID_FRANJA WHERE D.ID_USUARIO=? AND DD.FECHA_TURNO>=TRUNC(SYSDATE)-7 ORDER BY DD.FECHA_TURNO,F.HORA_INICIO FETCH FIRST 400 ROWS ONLY", usuario);
    }
    public List<Map<String, Object>> listarHorariosGeneral() {
        return jdbc.list("SELECT DD.ID_DISPONIBILIDAD,DD.ID_DOCTOR,D.NOMBRES||' '||D.APELLIDOS NOMBRE_DOCTOR,DD.FECHA_TURNO,DD.ID_FRANJA,F.HORA_INICIO,F.HORA_FIN,DD.ESTADO FROM DISPONIBILIDAD_DOCTOR DD JOIN DOCTOR D ON D.ID_DOCTOR=DD.ID_DOCTOR JOIN FRANJA_HORARIA F ON F.ID_FRANJA=DD.ID_FRANJA WHERE DD.FECHA_TURNO>=TRUNC(SYSDATE)-7 ORDER BY DD.FECHA_TURNO,F.HORA_INICIO FETCH FIRST 400 ROWS ONLY");
    }
    public List<Object> registrarHorario(long actor, long usuario, Long idDoctor, java.time.LocalDate fecha, Long idFranja) {
        return jdbc.call("SP_REGISTRAR_DISPONIBILIDAD", actor, usuario, idDoctor, fecha, idFranja, OracleJdbc.outNumber());
    }

}
