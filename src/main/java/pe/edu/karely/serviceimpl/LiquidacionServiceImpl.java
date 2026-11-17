package pe.edu.karely.serviceimpl;

import java.util.List;
import java.util.Map;
import pe.edu.karely.service.LiquidacionService;
import pe.edu.karely.repository.*;
import pe.edu.karely.dto.ApiInput;
import pe.edu.karely.security.PermisosUsuario;
import pe.edu.karely.dto.RespuestasOperacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LiquidacionServiceImpl implements LiquidacionService {
    private final LiquidacionRepository repository;
    private final PermisosUsuario permisos;
    public LiquidacionServiceImpl(LiquidacionRepository repository, PermisosUsuario permisos) {
        this.repository = repository;
        this.permisos = permisos;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarLiquidaciones(long actor) {
        permisos.exigir(actor,"TESORERA","ADMINISTRADORA");
        return repository.listarLiquidaciones();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> listarPagos(long actor) {
        permisos.exigir(actor,"TESORERA","ADMINISTRADORA");
        return repository.listarPagos();
    }

    @Override
    @Transactional
    public Map<String, Object> generarLiquidacion(ApiInput i, long actor) {
        permisos.exigir(actor,"TESORERA");
        return RespuestasOperacion.procedimiento("SP_GENERAR_LIQUIDACION", repository.generarLiquidacion(actor, actor, i.id("idDoctor"), i.date("fechaInicio"), i.date("fechaFin")));
    }

    @Override
    @Transactional
    public Map<String, Object> pagarDoctor(ApiInput i, long actor) {
        permisos.exigir(actor,"TESORERA");
        return RespuestasOperacion.procedimiento("SP_PAGAR_DOCTOR", repository.pagarDoctor(actor, actor, i.id("idLiquidacion"), i.money("monto"), i.required("metodoPago"), i.required("numeroOperacion"), i.required("numeroBoleta")));
    }

}
