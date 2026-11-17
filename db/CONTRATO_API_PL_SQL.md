# Contrato API → objetos de KARELY_BD2

El backend no ejecuta la DDL del esquema ni recrea las particiones. Se conecta a la instancia Oracle ya preparada por el usuario, con 29 tablas y los pasos 14–25 instalados.

| Acción HTTP (`POST /api/v1/operaciones/{accion}`) | PL/SQL Oracle |
|---|---|
| doctor-validar | SP_VALIDAR_DOCUMENTACION |
| disponibilidad-registrar | SP_REGISTRAR_DISPONIBILIDAD |
| prereserva-crear | SP_PRE_RESERVAR_CITA |
| cita-confirmar | SP_CONFIRMAR_CITA_PAGADA |
| cita-reprogramar | SP_REPROGRAMAR_CITA |
| asistencia-registrar | SP_REGISTRAR_ASISTENCIA |
| atencion-iniciar | SP_INICIAR_ATENCION |
| diagnostico-registrar | SP_REGISTRAR_DIAGNOSTICO |
| receta-crear | SP_CREAR_RECETA |
| medicamento-agregar | SP_AGREGAR_MEDICAMENTO |
| recibo-emitir | SP_EMITIR_RECIBO |
| recibo-entregar | SP_ENTREGAR_RECIBO |
| atencion-cerrar | SP_CERRAR_ATENCION |
| liquidacion-generar | SP_GENERAR_LIQUIDACION |
| doctor-pagar | SP_PAGAR_DOCTOR |
| reprogramada-cancelar | SP_CANCELAR_REPROGRAMADA |
| usuario-rol | SP_CAMBIAR_ROL_USUARIO |
| usuario-estado | SP_CAMBIAR_ESTADO_USUARIO |
| parametro-cambiar | SP_CAMBIAR_PARAMETRO |
| notificacion-encolar | SP_ENCOLAR_NOTIFICACION_CITA |

Los procedimientos exigen `CLIENT_IDENTIFIER`. `OracleRepository.call(...)` establece el ID autenticado en la misma conexión de la transacción y lo limpia en un bloque `finally`.

Las altas sencillas de pacientes, doctores y catálogos operan directamente con `INSERT ... RETURNING`, usando parámetros JDBC. Las vistas del paso 23 sirven reportes y listados.

`SP_CONFIRMAR_CITA_PAGADA` registra un pago marcado aprobado; solo debe llamarse cuando la verificación de la caja o pasarela externa haya terminado satisfactoriamente. Esta API **no verifica pagos reales** por sí misma.

`NOTIFICACION_SALIENTE` guarda una cola, no envía email. Un componente de backend con proveedor de correo externo todavía debe implementarse para envíos reales.

## Correo SMTP opcional

`MailQueueWorker` consume la cola Oracle mediante `SP_TOMAR_NOTIFICACION`, hace commit de la toma, envía por SMTP y registra el resultado con `SP_REGISTRAR_RESULTADO_ENVIO` en otra transacción. Está deshabilitado de forma predeterminada (`APP_MAIL_ENABLED=false`); solo funciona al configurar operador con rol `ADMINISTRADORA`, `APP_MAIL_FROM`, `MAIL_HOST`, `MAIL_USERNAME`, `MAIL_PASSWORD` y el servidor SMTP.

El envío es **al menos una vez**, no exactamente una vez: si se pierde la conexión tras aceptación de SMTP podría duplicarse un correo; el código de evento permite deduplicación donde el proveedor lo admita. No se incorporan diagnósticos ni datos clínicos en los mensajes.
