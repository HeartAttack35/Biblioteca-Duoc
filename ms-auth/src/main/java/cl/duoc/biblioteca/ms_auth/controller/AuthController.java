package cl.duoc.biblioteca.ms_auth.controller;

import cl.duoc.biblioteca.ms_auth.dto.ApiResponse;
import cl.duoc.biblioteca.ms_auth.dto.AuthRequest;
import cl.duoc.biblioteca.ms_auth.dto.AuthResponse;
import cl.duoc.biblioteca.ms_auth.model.Usuario;
import cl.duoc.biblioteca.ms_auth.repository.UsuarioRepository;
import cl.duoc.biblioteca.ms_auth.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = """
        Autenticación de usuarios mediante JWT.
        El endpoint de login es público y no requiere token previo.
        El accessToken retornado debe incluirse como 'Bearer {token}' en el header Authorization
        de todos los demás microservicios.
        """)
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(
        summary = "Iniciar sesión",
        description = """
            Valida las credenciales del usuario y retorna un accessToken JWT y un refreshToken.
            El accessToken debe usarse como 'Bearer {token}' en el header Authorization
            de los endpoints protegidos de ms-autor y ms-libro.
            **Credenciales de prueba:** usuario `admin` / contraseña `admin123` con rol ADMIN.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Credenciales del usuario",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthRequest.class),
                examples = @ExampleObject(
                    name = "Login con usuario ADMIN",
                    value = """
                        {
                          "username": "admin",
                          "password": "admin123"
                        }
                        """
                )
            )
        )
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Login exitoso. El campo accessToken es el JWT para usar en otros servicios.",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "Login exitoso",
                      "data": {
                        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiJ9...",
                        "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
                      },
                      "error": null
                    }
                    """)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Credenciales incorrectas: usuario no existe o contraseña inválida",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "success": false,
                      "message": "Credenciales inválidas",
                      "data": null,
                      "error": "Unauthorized"
                    }
                    """)
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());

        if (usuarioOpt.isPresent() && passwordEncoder.matches(request.getPassword(), usuarioOpt.get().getPassword())) {
            Usuario usuario = usuarioOpt.get();

            String accessToken = jwtUtil.generarToken(usuario.getUsername(), usuario.getRole());
            String refreshToken = jwtUtil.generarRefreshToken();

            AuthResponse authResponse = new AuthResponse(accessToken, refreshToken);
            ApiResponse<AuthResponse> response = new ApiResponse<>(true, "Login exitoso", authResponse, null);

            return ResponseEntity.ok(response);
        } else {
            ApiResponse<AuthResponse> response = new ApiResponse<>(false, "Credenciales inválidas", null, "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
