package cl.duoc.biblioteca.repository;

import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.service.EstudianteService;
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
 * Tests de la capa de repositorio de Estudiante a través del servicio.
 */
public class EstudianteRepositoryTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @InjectMocks
    private EstudianteService estudianteService;

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
    void dadoIdExistente_cuandoSeBuscaPorId_entoncesRetornaEstudiante() {
        // Given
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setRun("12345678-9");
        estudiante.setNombres("Juan Pérez");

        when(estudianteRepository.findById(1)).thenReturn(Optional.of(estudiante));

        // When
        Estudiante resultado = estudianteService.findById(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("12345678-9", resultado.getRun());
        verify(estudianteRepository, times(1)).findById(1);
    }

    // ─── Caso de error ────────────────────────────────────────────────

    @Test
    void dadoIdInexistente_cuandoSeBuscaPorId_entoncesRetornaNull() {
        // Given
        Integer idInexistente = 999;
        when(estudianteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // When
        Estudiante resultado = estudianteService.findById(idInexistente);

        // Then
        assertNull(resultado,
                "Debe retornar null cuando el estudiante no existe");
        verify(estudianteRepository, times(1)).findById(idInexistente);
    }
}
