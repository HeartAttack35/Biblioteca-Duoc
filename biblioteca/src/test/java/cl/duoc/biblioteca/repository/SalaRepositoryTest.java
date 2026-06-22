package cl.duoc.biblioteca.repository;

import cl.duoc.biblioteca.model.Sala;
import cl.duoc.biblioteca.model.TipoSala;
import cl.duoc.biblioteca.service.SalaService;
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
 * Tests de la capa de repositorio de Sala a través del servicio.
 */
public class SalaRepositoryTest {

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SalaService salaService;

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
    void dadoCodigoExistente_cuandoSeBuscaPorId_entoncesRetornaSala() {
        // Given
        TipoSala tipo = new TipoSala("Laboratorio");
        Sala sala = new Sala("Sala A101", 30, 1, tipo);
        sala.setCodigo(1);

        when(salaRepository.findById(1)).thenReturn(Optional.of(sala));

        // When
        Sala resultado = salaService.findById(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getCodigo());
        assertEquals("Sala A101", resultado.getNombre());
        verify(salaRepository, times(1)).findById(1);
    }

    // ─── Caso de error ────────────────────────────────────────────────

    @Test
    void dadoCodigoInexistente_cuandoSeBuscaPorId_entoncesRetornaNull() {
        // Given
        Integer codigoInexistente = 999;
        when(salaRepository.findById(codigoInexistente)).thenReturn(Optional.empty());

        // When
        Sala resultado = salaService.findById(codigoInexistente);

        // Then
        assertNull(resultado,
                "Debe retornar null cuando la sala no existe");
        verify(salaRepository, times(1)).findById(codigoInexistente);
    }
}
