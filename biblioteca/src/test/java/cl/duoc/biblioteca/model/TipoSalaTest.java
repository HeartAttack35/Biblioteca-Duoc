package cl.duoc.biblioteca.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios del modelo TipoSala.
 */
public class TipoSalaTest {

    // ─── Caso feliz ───────────────────────────────────────────────────

    @Test
    void dadoNombre_cuandoSeCreaConstrutor_entoncesSeAsignaCorrectamente() {
        // Given - When
        TipoSala tipo = new TipoSala("Laboratorio");
        tipo.setIdTipo(1);

        // Then
        assertEquals(1, tipo.getIdTipo());
        assertEquals("Laboratorio", tipo.getNombre());
    }

    @Test
    void dadoTipoSalaVacio_cuandoSeAsignaNombre_entoncesGetterRetornaValor() {
        // Given
        TipoSala tipo = new TipoSala();

        // When
        tipo.setNombre("Sala Multimedia");

        // Then
        assertEquals("Sala Multimedia", tipo.getNombre());
    }

    // ─── Caso de error ────────────────────────────────────────────────

    @Test
    void dadoTipoSalaNuevo_cuandoNoSeAsignaId_entoncesIdEsNulo() {
        // Given - When
        TipoSala tipo = new TipoSala("Sin ID");

        // Then
        assertNull(tipo.getIdTipo(),
                "El ID debe ser null antes de ser persistido por JPA");
    }
}
