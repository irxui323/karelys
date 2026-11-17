package pe.edu.karely.serviceimpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.karely.repository.UsuarioRepository;
@Service
public class LoginAttemptService {
 private final UsuarioRepository db;
 public LoginAttemptService(UsuarioRepository db){this.db=db;}
 @Transactional(propagation=Propagation.REQUIRES_NEW)
 public void failed(long userId){db.registrarIntentoFallido(userId);}
}
