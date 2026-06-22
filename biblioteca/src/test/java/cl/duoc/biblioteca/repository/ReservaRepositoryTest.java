package cl.duoc.biblioteca.repository;

import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.model.Reserva;
import cl.duoc.biblioteca.model.Sala;
import cl.duoc.biblioteca.service.ReservaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests de la capa de repositorio de Reserva a través del servicio.
 * Cubre casos felices y de error de las consultas del dominio.
 */
public class ReservaRepositoryTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private AutoCloseable closeable;
    private Reserva reservaMock;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setRun("12345678-9");

        Sala sala = new Sala();
        sala.setCodigo(1);
        sala.setNombre("Sala A101");

        reservaMock = new Reserva();
        reservaMock.setId(1);
        reservaMock.setEstudiante(estudiante);
        reservaMock.setSala(sala);
        reservaMock.setFechaSolicitada(new Date());
        reservaMock.setHoraSolicitada(new Date());
        reservaMock.setHoraCierre(new Date(System.currentTimeMillis() + 3_600_000));
        reservaMock.setEstado(1);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    // ─── Caso feliz ───────────────────────────────────────────────────

    @Test
    void dadoIdExistente_cuandoSeBuscaPorId_entoncesRetornaReserva() {
        // Given
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reservaMock));

        // When
        Reserva resultado = reservaService.findById(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(1, resultado.getEstado());
        verify(reservaRepository, times(1)).findById(1);
    }

    @Test
    void dadoCodigoSalaExistente_cuandoSeBuscanReservasPorSala_entoncesRetornaLista() {
        // Given
        when(reservaRepository.findBySalaCodigo(1)).thenReturn(List.of(reservaMock));

        // When
        List<Reserva> resultado = reservaService.findBySalaCodigo(1);

        // Then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.get(0).getSala().getCodigo());
        verify(reservaRepository, times(1)).findBySalaCodigo(1);
    }

    @Test
    void dadoIdEstudianteExistente_cuandoSeCuentanReservas_entoncesRetornaCantidad() {
        // Given
        when(reservaRepository.countByEstudianteId(1)).thenReturn(3L);

        // When
        Long total = reservaService.countByEstudianteId(1);

        // Then
        assertEquals(3L, total);
        verify(reservaRepository, times(1)).countByEstudianteId(1);
    }

    // ─── Caso de error ────────────────────────────────────────────────

    @Test
    void dadoIdInexistente_cuandoSeBuscaPorId_entoncesRetornaNull() {
        // Given
        when(reservaRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Reserva resultado = reservaService.findById(999);

        // Then
        assertNull(resultado,
                "Debe retornar null cuando la reserva no existe");
        verify(reservaRepository, times(1)).findById(999);
    }

    @Test
    void dadoCodigoSalaSinReservas_cuandoSeBuscan_entoncesRetornaListaVacia() {
        // Given
        when(reservaRepository.findBySalaCodigo(99)).thenReturn(List.of());

        // When
        List<Reserva> resultado = reservaService.findBySalaCodigo(99);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(),
                "Debe retornar lista vacía cuando la sala no tiene reservas");
    }

    @Test
    void dadoIdEstudianteSinReservas_cuandoSeCuentan_entoncesRetornaCero() {
        // Given
        when(reservaRepository.countByEstudianteId(999)).thenReturn(0L);

        // When
        Long total = reservaService.countByEstudianteId(999);

        // Then
        assertEquals(0L, total,
                "Debe retornar 0 cuando el estudiante no tiene reservas");
    }
}
