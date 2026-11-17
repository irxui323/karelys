package pe.edu.karely.serviceimpl;
import pe.edu.karely.repository.UsuarioRepository;
import pe.edu.karely.repository.SesionRepository;
import pe.edu.karely.service.SesionService;
import org.springframework.stereotype.Service;
import pe.edu.karely.security.JwtService;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
@Service
public class SesionServiceImpl implements SesionService {
 private final SesionRepository sesiones;
 private final UsuarioRepository db;private final PasswordEncoder encoder;private final JwtService jwt;private final LoginAttemptService attempts;
 public SesionServiceImpl(SesionRepository sesiones, UsuarioRepository db,PasswordEncoder encoder,JwtService jwt,LoginAttemptService attempts){this.sesiones=sesiones;this.db=db;this.encoder=encoder;this.jwt=jwt;this.attempts=attempts;}
 @Transactional
 public Map<String,Object> login(Map<String,Object> body){
  String username=Objects.toString(body.get("username"),"").trim().toLowerCase(java.util.Locale.ROOT);String password=Objects.toString(body.get("password"),"");
  if(username.isEmpty()||password.isEmpty())throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales incorrectas");
  Map<String,Object> user=db.buscarCredenciales(username);
  if(user==null||!"ACTIVO".equals(user.get("ESTADO"))||!encoder.matches(password,Objects.toString(user.get("PASSWORD_HASH"),""))){
   if(user!=null&&"ACTIVO".equals(user.get("ESTADO")))attempts.failed(((Number)user.get("ID_USUARIO")).longValue());
   throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales incorrectas");
  }
  long id=((Number)user.get("ID_USUARIO")).longValue();
  db.registrarAcceso(id);
  Long sid=sesiones.abrir(id);
  return Map.of("token",jwt.issue(id,sid,Objects.toString(user.get("ROL"))),"usuario",username,"rol",user.get("ROL"),"idUsuario",id);
 }
 public Map<String,Object> me(long id){Map<String,Object> u=db.buscarPerfil(id);if(u==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);return u;}
 @Transactional
 public Map<String, String> logout(long actor, String header) {
     if (header != null && header.startsWith("Bearer ")) {
         JwtService.Claims claims = jwt.verify(header.substring(7));
         if (claims.userId() == actor) {
             sesiones.cerrar(claims.sessionId(), actor);
         }
     }
     return Map.of("mensaje", "Sesión cerrada");
 }
}
