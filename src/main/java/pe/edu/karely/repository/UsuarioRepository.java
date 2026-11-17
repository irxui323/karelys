package pe.edu.karely.repository;

import java.util.List;
import java.util.Map;
import pe.edu.karely.persistence.OracleJdbc;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository {
    private final OracleJdbc jdbc;
    public UsuarioRepository(OracleJdbc jdbc) {
        this.jdbc = jdbc;
    }
    public List<Map<String, Object>> listarRoles() {
        return jdbc.list("SELECT ID_ROL,NOMBRE,ESTADO FROM ROL ORDER BY ID_ROL");
    }
    public List<Map<String, Object>> listarUsuarios() {
        return jdbc.list("SELECT U.ID_USUARIO,U.USERNAME,U.CORREO,U.ESTADO,U.ULTIMO_ACCESO,R.NOMBRE AS ROL FROM USUARIO U JOIN ROL R ON R.ID_ROL=U.ID_ROL ORDER BY U.ID_USUARIO DESC FETCH FIRST 200 ROWS ONLY");
    }
    public List<Object> asignarRol(long actor, long usuario, Long idUsuario, Long idRol) {
        return jdbc.call("SP_CAMBIAR_ROL_USUARIO", actor, usuario, idUsuario, idRol);
    }
    public List<Object> cambiarEstado(long actor, long usuario, Long idUsuario, String estado) {
        return jdbc.call("SP_CAMBIAR_ESTADO_USUARIO", actor, usuario, idUsuario, estado);
    }
    public Map<String, Object> rolActivo(long actor) {
        return jdbc.one("SELECT R.NOMBRE AS ROL FROM USUARIO U JOIN ROL R ON R.ID_ROL=U.ID_ROL WHERE U.ID_USUARIO=? AND U.ESTADO='ACTIVO' AND R.ESTADO='ACTIVO'", actor);
    }
    public Map<String, Object> buscarDoctorActivo(long usuario) {
        return jdbc.one("SELECT U.ID_USUARIO FROM USUARIO U JOIN ROL R ON R.ID_ROL=U.ID_ROL WHERE U.ID_USUARIO=? AND U.ESTADO='ACTIVO' AND R.NOMBRE='DOCTOR'", usuario);
    }


    public Map<String, Object> buscarCredenciales(String username) {
        return jdbc.one("SELECT U.ID_USUARIO,U.PASSWORD_HASH,U.ESTADO,U.INTENTOS_FALLIDOS,R.NOMBRE AS ROL FROM USUARIO U JOIN ROL R ON R.ID_ROL=U.ID_ROL WHERE U.USERNAME=? AND R.ESTADO='ACTIVO'", username);
    }


    public void registrarAcceso(long id) {
        jdbc.update("UPDATE USUARIO SET INTENTOS_FALLIDOS=0,ULTIMO_ACCESO=SYSTIMESTAMP WHERE ID_USUARIO=?", id);
    }


    public Map<String, Object> buscarPerfil(long id) {
        return jdbc.one("SELECT U.ID_USUARIO,U.USERNAME,U.CORREO,U.ESTADO,R.NOMBRE AS ROL FROM USUARIO U JOIN ROL R ON R.ID_ROL=U.ID_ROL WHERE U.ID_USUARIO=?", id);
    }


    public void registrarIntentoFallido(long userId) {
        jdbc.update("UPDATE USUARIO SET INTENTOS_FALLIDOS=INTENTOS_FALLIDOS+1,ESTADO=CASE WHEN INTENTOS_FALLIDOS+1>=5 THEN 'BLOQUEADO' ELSE ESTADO END WHERE ID_USUARIO=? AND ESTADO='ACTIVO'", userId);
    }


    public Map<String, Object> contarAdministradorasActivas() {
        return jdbc.one("SELECT COUNT(*) AS TOTAL FROM USUARIO U JOIN ROL R ON R.ID_ROL=U.ID_ROL WHERE R.NOMBRE='ADMINISTRADORA' AND U.ESTADO='ACTIVO'");
    }

    public Map<String, Object> buscarRolAdministradora() {
        return jdbc.one("SELECT ID_ROL FROM ROL WHERE NOMBRE='ADMINISTRADORA' AND ESTADO='ACTIVO'");
    }

    public Long crearAdministradoraInicial(Object rol, String usuario, String correo, String hash) {
        return jdbc.insertReturning("begin insert into USUARIO(ID_ROL,USERNAME,CORREO,PASSWORD_HASH) values(?,?,?,?) returning ID_USUARIO into ?; end;", rol, usuario, correo, hash);
    }

    public Object crearUsuario(long actor, long rol, String nombre, String correo, String hash) {
        return jdbc.call("SP_CREAR_USUARIO", actor, actor, rol, nombre, correo, hash, OracleJdbc.outNumber()).getFirst();
    }
}
