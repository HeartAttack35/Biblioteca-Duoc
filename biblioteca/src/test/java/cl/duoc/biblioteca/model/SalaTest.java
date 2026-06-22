package cl.duoc.biblioteca.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios del modelo Sala.
 * Validan la construcción del modelo y la relación con TipoSala.
 */
public class SalaTest {

    // ─── Caso feliz ───────────────────────────────────────────────────

    @Test
    void dadosDatosCompletos_cuandoSeCreanConConstructor_entoncesSeAsignanCorrectamente() {
        // Given
        TipoSala tipo = new TipoSala("Laboratorio");
        tipo.setIdTipo(1);

        // When
        Sala sala = new Sala("Sala A101", 30, 1, tipo);
        sala.setCodigo(1);

        // Then
        assertEquals(1, sala.getCodigo());
        assertEquals("Sala A101", sala.getNombre());
        assertEquals(30, sala.getCapacidad());
        assertEquals(1, sala.getIdInstituto());
        assertNotNull(sala.getTipoSala());
        assertEquals("Laboratorio", sala.getTipoSala().getNombre());
    }

    @Test
    void dadaSalaSinTipo_cuandoSeAsignaTipoLuego_entoncesRelacionEsValida() {
        // Given
        Sala sala = new Sala();
        sala.setNombre("Sala B202");
        sala.setCapacidad(20);

        TipoSala tipo = new TipoSala("Sala de Reuniones");

        // When
        sala.setTipoSala(tipo);

        // Then
        assertNotNull(sala.getTipoSala());
        assertEquals("Sala de Reuniones", sala.getTipoSala().getNombre());
    }

    // ─── Caso de error ────────────────────────────────────────────────

    @Test
    void dadaSalaNueva_cuandoNoSeAsignaCodigo_entoncesCodigoEsNulo() {
        // Given - When
        Sala sala = new Sala();
        sala.setNombre("Sala Test");

        // Then — el código es autogenerado por JPA, debe ser null antes de persistir
        assertNull(sala.getCodigo(),
                "El código debe ser null antes de ser persistido por JPA");
    }

    @Test
    void dadaSalaSinTipoSala_cuandoSeAccedeAlTipo_entoncesRetornaNulo() {
        // Given - When
        Sala sala = new Sala();
        sala.setNombre("Sala Sin Tipo");

        // Then
        assertNull(sala.getTipoSala(),
                "El tipo de sala debe ser null si no se asigna");
    }
}
