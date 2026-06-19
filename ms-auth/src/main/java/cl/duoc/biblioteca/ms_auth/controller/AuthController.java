package cl.duoc.biblioteca.ms_auth.controller;

import cl.duoc.biblioteca.ms_auth.dto.ApiResponse;
import cl.duoc.biblioteca.ms_auth.dto.AuthRequest;
import cl.duoc.biblioteca.ms_auth.dto.AuthResponse;
import cl.duoc.biblioteca.ms_auth.model.Usuario;
import cl.duoc.biblioteca.ms_auth.repository.UsuarioRepository;
import cl.duoc.biblioteca.ms_auth.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());

        if (usuarioOpt.isPresent() && request.getPassword().equals(usuarioOpt.get().getPassword())) {
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