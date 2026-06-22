package cl.duoc.biblioteca.ms_auth.controller;

import cl.duoc.biblioteca.ms_auth.dto.AuthRequest;
import cl.duoc.biblioteca.ms_auth.dto.AuthResponse;
import cl.duoc.biblioteca.ms_auth.dto.ApiResponse;
import cl.duoc.biblioteca.ms_auth.model.Usuario;
import cl.duoc.biblioteca.ms_auth.repository.UsuarioRepository;
import cl.duoc.biblioteca.ms_auth.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtUtil jwtUtil;

    // PasswordEncoder real — no se mockea porque la lógica BCrypt es parte del test
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private AuthController authController;

    @BeforeEach
    void setUp() {
        // Inyección manual porque el PasswordEncoder es una instancia real, no un mock
        authController = new AuthController(usuarioRepository, jwtUtil, passwordEncoder);
    }

    @Test
    void login_conCredencialesValidas_debeRetornar200ConTokens() {
        String rawPassword = "password123";
        String hashedPassword = passwordEncoder.encode(rawPassword);
        Usuario usuario = new Usuario("admin", hashedPassword, "ROLE_ADMIN");

        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(jwtUtil.generarToken("admin", "ROLE_ADMIN")).thenReturn("token.jwt.falso");
        when(jwtUtil.generarRefreshToken()).thenReturn("refresh-uuid");

        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword(rawPassword);

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Login exitoso", response.getBody().getMessage());
        assertEquals("token.jwt.falso", response.getBody().getData().getAccessToken());
        assertEquals("refresh-uuid", response.getBody().getData().getRefreshToken());
    }

    @Test
    void login_conPasswordIncorrecta_debeRetornar401() {
        String hashedPassword = passwordEncoder.encode("passwordCorrecto");
        Usuario usuario = new Usuario("admin", hashedPassword, "ROLE_ADMIN");

        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("passwordErronea");

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Unauthorized", response.getBody().getError());
    }

    @Test
    void login_conUsuarioInexistente_debeRetornar401() {
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        AuthRequest request = new AuthRequest();
        request.setUsername("noexiste");
        request.setPassword("cualquiera");

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        // JwtUtil no debe ser invocado si el usuario no existe
        verify(jwtUtil, never()).generarToken(any(), any());
    }
}
