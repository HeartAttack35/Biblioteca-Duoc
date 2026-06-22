package cl.duoc.biblioteca.ms_auth.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void constructorConParametros_debeAsignarCamposCorrectamente() {
        Usuario usuario = new Usuario("juan", "hash123", "ROLE_USER");

        assertEquals("juan", usuario.getUsername());
        assertEquals("hash123", usuario.getPassword());
        assertEquals("ROLE_USER", usuario.getRole());
    }

    @Test
    void constructorVacio_debeInstanciarSinExcepcion() {
        assertDoesNotThrow(() -> new Usuario());
    }

    @Test
    void setters_debenActualizarCamposCorrectamente() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsername("maria");
        usuario.setPassword("bcrypt_hash");
        usuario.setRole("ROLE_ADMIN");

        assertEquals(1, usuario.getId());
        assertEquals("maria", usuario.getUsername());
        assertEquals("bcrypt_hash", usuario.getPassword());
        assertEquals("ROLE_ADMIN", usuario.getRole());
    }
}
