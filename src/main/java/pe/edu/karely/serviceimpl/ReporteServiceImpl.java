package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.ReporteService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReporteServiceImpl implements ReporteService {
    private final ReporteRepository repository;
    private final PermisosUsuario permisos;
    public ReporteServiceImpl(ReporteRepository repository, PermisosUsuario permisos) {
        this.repository = repository;
        this.permisos = permisos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> generarResumen(long actor) {
        permisos.exigir(actor,"ADMINISTRADORA","TESORERA","RECEPCIONISTA");
        return repository.generarResumen();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> generarReporte(java.time.LocalDate desde, java.time.LocalDate hasta, String estado, long actor) {
        permisos.exigir(actor, "ADMINISTRADORA", "RECEPCIONISTA", "TESORERA");
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("Rango de fechas inválido");
        }
        return repository.generarReporte(desde, hasta, estado);
    }
}
