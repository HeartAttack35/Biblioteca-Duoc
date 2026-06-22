package cl.duoc.biblioteca.ms_auth.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Mínimo 32 bytes para HS256
    private static final String SECRET = "clave_super_secreta_12345678901234567890";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void generarToken_debeRetornarTokenNoNulo() {
        String token = jwtUtil.generarToken("admin", "ROLE_ADMIN");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void generarToken_tokenDebeContenerTresSecciones() {
        // Un JWT tiene siempre la forma header.payload.signature
        String token = jwtUtil.generarToken("usuario1", "ROLE_USER");
        long puntos = token.chars().filter(c -> c == '.').count();
        assertEquals(2, puntos);
    }

    @Test
    void generarRefreshToken_debeRetornarUUID() {
        String refresh = jwtUtil.generarRefreshToken();
        assertNotNull(refresh);
        // UUID tiene formato xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx (36 chars con guiones)
        assertEquals(36, refresh.length());
    }

    @Test
    void generarRefreshToken_cadaLlamadaDebeRetornarValorDistinto() {
        String r1 = jwtUtil.generarRefreshToken();
        String r2 = jwtUtil.generarRefreshToken();
        assertNotEquals(r1, r2);
    }
}
