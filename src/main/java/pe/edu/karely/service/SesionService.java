package pe.edu.karely.service;

import java.util.Map;

public interface SesionService {
    Map<String, Object> login(Map<String, Object> body);
    Map<String, Object> me(long actor);
    Map<String, String> logout(long actor, String authorization);
}
