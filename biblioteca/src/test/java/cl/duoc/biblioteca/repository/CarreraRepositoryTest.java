package cl.duoc.biblioteca.repository;

import cl.duoc.biblioteca.model.Carrera;
import cl.duoc.biblioteca.service.CarreraService;
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
 * Tests de la capa de repositorio de Carrera a través del servicio.
 * Validan que el servicio delega correctamente al repositorio
 * y maneja los casos de "no encontrado".
 */
public class CarreraRepositoryTest {

    @Mock
    private CarreraRepository carreraRepository;

    @InjectMocks
    private CarreraService carreraService;

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
    void dadoCodigoExistente_cuandoSeBuscaPorId_entoncesRetornaCarrera() {
        // Given
        String codigo = "ING-INF";
        Carrera carrera = new Carrera();
        carrera.setCodigo(codigo);
        carrera.setNombre("Ingeniería en Informática");

        when(carreraRepository.findById(codigo)).thenReturn(Optional.of(carrera));

        // When
        Carrera resultado = carreraService.findById(codigo);

        // Then
        assertNotNull(resultado);
        assertEquals(codigo, resultado.getCodigo());
        verify(carreraRepository, times(1)).findById(codigo);
    }

    // ─── Caso de error ────────────────────────────────────────────────

    @Test
    void dadoCodigoInexistente_cuandoSeBuscaPorId_entoncesRetornaNull() {
        // Given
        String codigoInexistente = "NO-EXISTE";
        when(carreraRepository.findById(codigoInexistente)).thenReturn(Optional.empty());

        // When
        Carrera resultado = carreraService.findById(codigoInexistente);

        // Then — el servicio usa orElse(null), debe retornar null
        assertNull(resultado,
                "Debe retornar null cuando la carrera no existe en el repositorio");
        verify(carreraRepository, times(1)).findById(codigoInexistente);
    }

    @Test
    void dadaCarreraExistente_cuandoSeElimina_entoncesRepositorioEsInvocado() {
        // Given
        String codigo = "ING-INF";
        doNothing().when(carreraRepository).deleteById(codigo);

        // When
        carreraService.delete(codigo);

        // Then
        verify(carreraRepository, times(1)).deleteById(codigo);
    }
}
