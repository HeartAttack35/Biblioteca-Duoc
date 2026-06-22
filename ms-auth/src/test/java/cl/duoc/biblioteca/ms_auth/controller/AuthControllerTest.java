package cl.duoc.biblioteca.ms_auth.controller;

import cl.duoc.biblioteca.ms_auth.dto.ApiResponse;
import cl.duoc.biblioteca.ms_auth.dto.AuthRequest;
import cl.duoc.biblioteca.ms_auth.dto.AuthResponse;
import cl.duoc.biblioteca.ms_auth.service.AuthService;
import cl.duoc.biblioteca.ms_auth.service.AuthService.CredencialesInvalidasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del AuthController.
 *
 * El controller solo orquesta: delega en AuthService y mapea el resultado a HTTP.
 * Por eso se mockea AuthService y se valida únicamente el comportamiento HTTP.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);
    }

    // ─── Caso feliz ───────────────────────────────────────────────────────────

    @Test
    void login_conCredencialesValidas_debeRetornar200ConTokens() {
        // Given
        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("password123");

        AuthResponse tokensEsperados = new AuthResponse("token.jwt.falso", "refresh-uuid");
        when(authService.login(request)).thenReturn(tokensEsperados);

        // When
        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Login exitoso", response.getBody().getMessage());
        assertEquals("token.jwt.falso", response.getBody().getData().getAccessToken());
        assertEquals("refresh-uuid", response.getBody().getData().getRefreshToken());
        verify(authService, times(1)).login(request);
    }

    // ─── Casos de error ───────────────────────────────────────────────────────

    @Test
    void login_conPasswordIncorrecta_debeRetornar401() {
        // Given
        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("passwordErronea");

        when(authService.login(request)).thenThrow(new CredencialesInvalidasException("Credenciales inválidas"));

        // When
        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Credenciales inválidas", response.getBody().getMessage());
        assertEquals("Unauthorized", response.getBody().getError());
    }

    @Test
    void login_conUsuarioInexistente_debeRetornar401() {
        // Given
        AuthRequest request = new AuthRequest();
        request.setUsername("noexiste");
        request.setPassword("cualquiera");

        when(authService.login(request)).thenThrow(new CredencialesInvalidasException("Credenciales inválidas"));

        // When
        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertNull(response.getBody().getData());
    }

    @Test
    void login_conCredencialesValidas_noDebeExponer_detallesInternos() {
        // Given: el error no debe filtrar si fue usuario o contraseña el problema
        AuthRequest request = new AuthRequest();
        request.setUsername("admin");
        request.setPassword("wrong");

        when(authService.login(request)).thenThrow(new CredencialesInvalidasException("Credenciales inválidas"));

        // When
        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        // Then: el mensaje es genérico, no dice si falló usuario o contraseña
        assertEquals("Credenciales inválidas", response.getBody().getMessage());
    }
}
