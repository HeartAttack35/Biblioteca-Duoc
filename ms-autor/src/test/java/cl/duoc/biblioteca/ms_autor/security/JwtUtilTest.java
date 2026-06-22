package cl.duoc.biblioteca.ms_autor.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String SECRET = "clave_super_secreta_12345678901234567890";
    private SecretKey key;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
        key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    private String crearToken(String username, String role, long expirationMs) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    @Test
    void esValido_conTokenBienFormado_debeRetornarTrue() {
        String token = crearToken("usuario1", "ROLE_USER", 3600000);
        assertTrue(jwtUtil.esValido(token));
    }

    @Test
    void esValido_conTokenExpirado_debeRetornarFalse() {
        // Expira en el pasado (-1 ms)
        String token = crearToken("usuario1", "ROLE_USER", -1000);
        assertFalse(jwtUtil.esValido(token));
    }

    @Test
    void esValido_conTokenMalformado_debeRetornarFalse() {
        assertFalse(jwtUtil.esValido("esto.no.es.un.jwt.valido"));
    }

    @Test
    void obtenerUsuario_debeRetornarSubjectDelToken() {
        String token = crearToken("maria", "ROLE_ADMIN", 3600000);
        assertEquals("maria", jwtUtil.obtenerUsuario(token));
    }

    @Test
    void obtenerRole_debeRetornarRoleDelToken() {
        String token = crearToken("carlos", "ROLE_USER", 3600000);
        assertEquals("ROLE_USER", jwtUtil.obtenerRole(token));
    }
}
