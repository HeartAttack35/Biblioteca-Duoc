package cl.duoc.biblioteca.repository;

import cl.duoc.biblioteca.model.TipoSala;
import cl.duoc.biblioteca.service.TipoSalaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests de la capa de repositorio de TipoSala a través del servicio.
 */
public class TipoSalaRepositoryTest {

    @Mock
    private TipoSalaRepository tipoSalaRepository;

    @InjectMocks
    private TipoSalaService tipoSalaService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    // ─── Caso feliz ───────────────────────────────────────────────────

    @Test
    void dadoIdExistente_cuandoSeBusca_entoncesRetornaTipoSala() {
        // Given
        TipoSala tipo = new TipoSala("Laboratorio");
        tipo.setIdTipo(1);

        when(tipoSalaRepository.findById(1)).thenReturn(Optional.of(tipo));

        // When
        TipoSala resultado = tipoSalaService.findById(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdTipo());
        assertEquals("Laboratorio", resultado.getNombre());
        verify(tipoSalaRepository, times(1)).findById(1);
    }

    // ─── Caso de error ────────────────────────────────────────────────

    @Test
    void dadoIdInexistente_cuandoSeBusca_entoncesRetornaNull() {
        // Given
        when(tipoSalaRepository.findById(999)).thenReturn(Optional.empty());

        // When
        TipoSala resultado = tipoSalaService.findById(999);

        // Then
        assertNull(resultado,
                "Debe retornar null cuando el tipo de sala no existe");
    }

    // ─── Verificación de interacción ──────────────────────────────────

    @Test
    void dadoIdExistente_cuandoSeGuarda_entoncesRepositorioEsInvocado() {
        // Given
        TipoSala tipo = new TipoSala("Sala de Reuniones");
        tipo.setIdTipo(2);

        when(tipoSalaRepository.save(tipo)).thenReturn(tipo);

        // When
        TipoSala guardado = tipoSalaService.save(tipo);

        // Then
        assertNotNull(guardado);
        assertEquals("Sala de Reuniones", guardado.getNombre());
        verify(tipoSalaRepository, times(1)).save(tipo);
    }
}
