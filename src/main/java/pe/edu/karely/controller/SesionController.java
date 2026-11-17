package pe.edu.karely.controller;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.karely.service.SesionService;

@RestController
@RequestMapping("/api/v1/auth")
public class SesionController {
    private final SesionService service;

    public SesionController(SesionService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> body) {
        return service.login(body);
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        return service.me((Long) auth.getPrincipal());
    }

    @PostMapping("/logout")
    public Map<String, String> logout(Authentication auth, HttpServletRequest request) {
        return service.logout((Long) auth.getPrincipal(), request.getHeader("Authorization"));
    }
}
