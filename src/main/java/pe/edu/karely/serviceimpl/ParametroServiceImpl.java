package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.ParametroService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParametroServiceImpl implements ParametroService {
    private final ParametroRepository repository;
    private final PermisosUsuario permisos;
    public ParametroServiceImpl(ParametroRepository repository, PermisosUsuario permisos) {
        this.repository = repository;
        this.permisos = permisos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarParametros(long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        return repository.listarParametros();
    }

    @Override
    @Transactional
    public Map<String, Object> configurarParametro(ApiInput i, long actor) {
        permisos.exigir(actor,"ADMINISTRADORA");
        return RespuestasOperacion.procedimiento("SP_CAMBIAR_PARAMETRO", repository.configurarParametro(actor, actor, i.required("nombre"), i.required("valor")));
    }

}
