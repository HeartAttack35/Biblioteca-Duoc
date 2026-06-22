package cl.duoc.biblioteca.ms_auth.service;

import cl.duoc.biblioteca.ms_auth.dto.AuthRequest;
import cl.duoc.biblioteca.ms_auth.dto.AuthResponse;
import cl.duoc.biblioteca.ms_auth.model.Usuario;
import cl.duoc.biblioteca.ms_auth.repository.UsuarioRepository;
import cl.duoc.biblioteca.ms_auth.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Capa de servicio de autenticación.
 *
 * Concentra toda la lógica de negocio del proceso de login:
 *  - Buscar el usuario en base de datos por username.
 *  - Validar la contraseña contra el hash almacenado.
 *  - Generar el accessToken JWT y el refreshToken.
 *
 * El controller sólo orquesta: recibe la petición, delega aquí y devuelve la respuesta HTTP.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Autentica un usuario y genera sus tokens.
     *
     * @param request DTO con username y password en texto plano.
     * @return AuthResponse con accessToken y refreshToken si las credenciales son válidas.
     * @throws CredencialesInvalidasException si el usuario no existe o la contraseña no coincide.
     */
    public AuthResponse login(AuthRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());

        // Regla de negocio: el usuario debe existir Y la contraseña debe coincidir con el hash BCrypt
        if (usuarioOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), usuarioOpt.get().getPassword())) {
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        Usuario usuario = usuarioOpt.get();
        String accessToken = jwtUtil.generarToken(usuario.getUsername(), usuario.getRole());
        String refreshToken = jwtUtil.generarRefreshToken();

        return new AuthResponse(accessToken, refreshToken);
    }

    /**
     * Excepción de dominio para credenciales inválidas.
     * Al estar definida como clase estática interna, pertenece al dominio del servicio
     * sin necesidad de un archivo separado.
     */
    public static class CredencialesInvalidasException extends RuntimeException {
        public CredencialesInvalidasException(String message) {
            super(message);
        }
    }
}
