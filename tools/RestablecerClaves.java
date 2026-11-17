import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class RestablecerClaves {
    private static final String[][] CUENTAS = {
        {"administradora", "HASH_BCRYPT_ADMIN"},
        {"recepcionista", "HASH_BCRYPT_RECEPCION"},
        {"doctor", "HASH_BCRYPT_DOCTOR"},
        {"cajera", "HASH_BCRYPT_CAJA"},
        {"tesorera", "HASH_BCRYPT_TESORERIA"}
    };

    public static void main(String[] args) {
        var bcrypt = new BCryptPasswordEncoder(12);
        if (Arrays.equals(args, new String[]{"--prueba"})) {
            String ejemplo = "SoloPruebaLocal_2026";
            String hash = bcrypt.encode(ejemplo);
            if (!bcrypt.matches(ejemplo, hash) || !hash.startsWith("$2a$12$")) {
                throw new IllegalStateException("BCrypt no pasó la verificación");
            }
            System.out.println("BCrypt verificado.");
            return;
        }

        boolean sinAdministradora = Arrays.equals(args, new String[]{"--sin-administradora"});
        if (!sinAdministradora && (args.length > 1 || (args.length == 1 &&
                Arrays.stream(CUENTAS).noneMatch(cuenta -> cuenta[0].equals(args[0]))))) {
            throw new IllegalArgumentException("Indica un usuario de la lista de cuentas de ejemplo.");
        }

        Console terminal = System.console();
        if (terminal == null) {
            throw new IllegalStateException("Ejecuta el comando en una ventana de PowerShell, sin redirigir la salida.");
        }

        var sql = new StringBuilder("WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK\n");
        for (String[] cuenta : CUENTAS) {
            if (sinAdministradora ? cuenta[0].equals("administradora")
                    : args.length == 1 && !cuenta[0].equals(args[0])) {
                continue;
            }
            String hash = leerHash(terminal, bcrypt, cuenta[0]);
            sql.append("UPDATE usuario SET password_hash = '").append(hash)
                .append("', intentos_fallidos = 0, estado = CASE WHEN estado = 'BLOQUEADO' ")
                .append("THEN 'ACTIVO' ELSE estado END\n")
                .append("WHERE username = '").append(cuenta[0]).append("'");
            if (!sinAdministradora) {
                sql.append(" AND password_hash = '").append(cuenta[1]).append("'");
            }
            sql.append(";\n");
        }
        sql.append("COMMIT;\n");
        terminal.printf("%nCopia las siguientes sentencias a SQL Developer.%n%n%s", sql);
    }

    private static String leerHash(Console terminal, BCryptPasswordEncoder bcrypt, String usuario) {
        while (true) {
            char[] entrada = terminal.readPassword("Nueva clave para %s: ", usuario);
            if (entrada == null) {
                throw new IllegalStateException("Entrada cancelada.");
            }
            try {
                String clave = new String(entrada);
                if (clave.isBlank() || clave.length() < 12 || clave.getBytes(StandardCharsets.UTF_8).length > 72) {
                    terminal.printf("Usa al menos 12 caracteres y como máximo 72 bytes UTF-8.%n");
                    continue;
                }
                char[] confirmacion = terminal.readPassword("Repítela: ");
                if (confirmacion == null) {
                    throw new IllegalStateException("Entrada cancelada.");
                }
                try {
                    if (Arrays.equals(entrada, confirmacion)) {
                        return bcrypt.encode(clave);
                    }
                    terminal.printf("Las claves difieren. Inténtalo otra vez.%n");
                } finally {
                    Arrays.fill(confirmacion, '\0');
                }
            } finally {
                Arrays.fill(entrada, '\0');
            }
        }
    }
}
