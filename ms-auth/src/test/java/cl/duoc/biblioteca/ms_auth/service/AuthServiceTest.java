package cl.duoc.biblioteca.ms_auth.service;

import cl.duoc.biblioteca.ms_auth.dto.AuthRequest;
import cl.duoc.biblioteca.ms_auth.dto.AuthResponse;
import cl.duoc.biblioteca.ms_auth.model.Usuario;
import cl.duoc.biblioteca.ms_auth.repository.UsuarioRepository;
import cl.duoc.biblioteca.ms_auth.security.JwtUtil;
import cl.duoc.biblioteca.ms_auth.service.AuthService.CredencialesInvalidasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del AuthService.
 *
 * Valida las reglas de negocio del proceso de autenticación:
 *  - El usuario debe existir en base de datos.
 *  - La contraseña en texto plano debe coincidir con el hash BCrypt almacenado.
 *  - Si ambas condiciones se cumplen, se generan accessToken y refreshToken.
 *  - Si alguna falla, se lanza CredencialesInvalidasException.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtUtil jwtUtil;

    // PasswordEncoder real: la lógica de BCrypt es parte de la regla de negocio que se testea
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(usuarioRepository, jwtUtil, passwordEncoder);
    }

    // ─── Caso feliz ───────────────────────────────────────────────────────────

    @Test
    void login_conCredencialesValidas_debeRetornarTokens() {
        // Given
        String rawPassword = "password123";
        String hashedPassword = passwordEncoder.encode(rawPassword);
        Usuario usuario = new Usuario("admin", hashedPassword, "ROLE_ADMIN");

        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(jwtUtil.generarToken("admin", "ROLE_ADMIN")).thenReturn("access.token.jwt");
        when(jwtUtil.generarRefreshToken()).thenReturn("refresh-uuid-1234");

        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword(rawPassword);

        // When
        AuthResponse resultado = authService.login(request);

        // Then
        assertNotNull(resultado);
        assertEquals("access.token.jwt", resultado.getAccessToken());
        assertEquals("refresh-uuid-1234", resultado.getRefreshToken());
        verify(jwtUtil, times(1)).generarToken("admin", "ROLE_ADMIN");
        verify(jwtUtil, times(1)).generarRefreshToken();
    }

    @Test
    void login_conCredencialesValidas_debeUsarElRolDelUsuario() {
        // Given: usuario con rol USER (no ADMIN)
        String rawPassword = "pass";
        Usuario usuario = new Usuario("user1", passwordEncoder.encode(rawPassword), "ROLE_USER");

        when(usuarioRepository.findByUsername("user1")).thenReturn(Optional.of(usuario));
        when(jwtUtil.generarToken("user1", "ROLE_USER")).thenReturn("token.user");
        when(jwtUtil.generarRefreshToken()).thenReturn("refresh");

        AuthRequest request = new AuthRequest();
        request.setUsername("user1");
        request.setPassword(rawPassword);

        // When
        AuthResponse resultado = authService.login(request);

        // Then: el token se genera con el rol correcto del usuario
        assertNotNull(resultado);
        verify(jwtUtil).generarToken("user1", "ROLE_USER");
    }

    // ─── Casos de error ───────────────────────────────────────────────────────

    @Test
    void login_conUsuarioInexistente_debeLanzarCredencialesInvalidasException() {
        // Given
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        AuthRequest request = new AuthRequest();
        request.setUsername("noexiste");
        request.setPassword("cualquiera");

        // When - Then
        CredencialesInvalidasException ex = assertThrows(
            CredencialesInvalidasException.class,
            () -> authService.login(request)
        );
        assertEquals("Credenciales inválidas", ex.getMessage());
        // JwtUtil no debe invocarse si el usuario no existe
        verify(jwtUtil, never()).generarToken(any(), any());
    }

    @Test
    void login_conPasswordIncorrecta_debeLanzarCredencialesInvalidasException() {
        // Given
        String hashedPassword = passwordEncoder.encode("passwordCorrecto");
        Usuario usuario = new Usuario("admin", hashedPassword, "ROLE_ADMIN");

        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("passwordErronea");

        // When - Then
        assertThrows(
            CredencialesInvalidasException.class,
            () -> authService.login(request)
        );
        // JwtUtil no debe invocarse si la contraseña no coincide
        verify(jwtUtil, never()).generarToken(any(), any());
    }

    @Test
    void login_conPasswordVacia_debeLanzarCredencialesInvalidasException() {
        // Given: contraseña vacía no puede coincidir con ningún hash válido
        String hashedPassword = passwordEncoder.encode("passwordReal");
        Usuario usuario = new Usuario("admin", hashedPassword, "ROLE_ADMIN");

        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("");

        // When - Then
        assertThrows(
            CredencialesInvalidasException.class,
            () -> authService.login(request)
        );
    }
}
