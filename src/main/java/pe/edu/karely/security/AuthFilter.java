package pe.edu.karely.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import pe.edu.karely.repository.SesionRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
public class AuthFilter extends OncePerRequestFilter {
 private final JwtService jwt;private final SesionRepository sesiones;
 public AuthFilter(JwtService jwt,SesionRepository sesiones){this.jwt=jwt;this.sesiones=sesiones;}
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException {
  String auth=req.getHeader("Authorization");
  if(auth!=null&&auth.startsWith("Bearer ")) {
   try {
    JwtService.Claims c=jwt.verify(auth.substring(7));
    List<Map<String,Object>> row=sesiones.buscarSesionActiva(c.sessionId(), c.userId());
    if(!row.isEmpty()&&c.role().equals(row.getFirst().get("ROL"))){
     var token=new UsernamePasswordAuthenticationToken(c.userId(),null,List.of(new SimpleGrantedAuthority("ROLE_"+c.role())));
     SecurityContextHolder.getContext().setAuthentication(token);
     sesiones.actualizarActividad(c.sessionId());
    }
   }catch(Exception ignored){SecurityContextHolder.clearContext();}
  }
  try{chain.doFilter(req,res);}finally{SecurityContextHolder.clearContext();}
 }
}
