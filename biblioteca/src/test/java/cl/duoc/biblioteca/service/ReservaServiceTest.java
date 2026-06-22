package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.model.Reserva;
import cl.duoc.biblioteca.model.Sala;
import cl.duoc.biblioteca.repository.ReservaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    // ─── Casos felices ────────────────────────────────────────────────

    @Test
    public void testFindAll() {
        // Given
        Reserva reserva = crearReserva();
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));

        // When
        List<Reserva> reservas = reservaService.findAll();

        // Then
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals(reserva.getId(), reservas.get(0).getId());
    }

    @Test
    public void testFindById() {
        // Given
        Integer id = 1;
        Reserva reserva = crearReserva();
        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));

        // When
        Reserva found = reservaService.findById(id);

        // Then
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testSave() {
        // Given
        Reserva reserva = crearReserva();
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        // When
        Reserva saved = reservaService.save(reserva);

        // Then
        assertNotNull(saved);
        assertEquals(1, saved.getEstado());
    }

    @Test
    public void testDeleteById() {
        // Given
        Integer id = 1;
        doNothing().when(reservaRepository).deleteById(id);

        // When
        reservaService.deleteById(id);

        // Then
        verify(reservaRepository, times(1)).deleteById(id);
    }

    // ─── Casos de error ───────────────────────────────────────────────

    @Test
    public void testFindById_Inexistente_RetornaNull() {
        // Given — ID que no existe en el repositorio
        when(reservaRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Reserva resultado = reservaService.findById(999);

        // Then
        assertNull(resultado,
                "El servicio debe retornar null cuando la reserva no existe");
        verify(reservaRepository, times(1)).findById(999);
    }

    @Test
    public void testFindBySalaCodigo_SinReservas_RetornaListaVacia() {
        // Given — sala sin reservas registradas
        when(reservaRepository.findBySalaCodigo(99)).thenReturn(List.of());

        // When
        List<Reserva> resultado = reservaService.findBySalaCodigo(99);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(),
                "Debe retornar lista vacía cuando la sala no tiene reservas");
    }

    @Test
    public void testCountByEstudianteId_SinReservas_RetornaCero() {
        // Given — estudiante que no tiene reservas
        when(reservaRepository.countByEstudianteId(999)).thenReturn(0L);

        // When
        Long total = reservaService.countByEstudianteId(999);

        // Then
        assertEquals(0L, total,
                "Debe retornar 0 cuando el estudiante no tiene reservas registradas");
    }

    @Test
    public void testFindAll_SinReservas_RetornaListaVacia() {
        // Given
        when(reservaRepository.findAll()).thenReturn(List.of());

        // When
        List<Reserva> resultado = reservaService.findAll();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(),
                "Debe retornar lista vacía si no hay reservas registradas");
    }

    // ─── Helper ───────────────────────────────────────────────────────

    private Reserva crearReserva() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setRun("12345678-9");
        estudiante.setNombres("Juan Pérez");

        Sala sala = new Sala();
        sala.setCodigo(1);
        sala.setNombre("Sala 101");

        Reserva reserva = new Reserva();
        reserva.setId(1);
        reserva.setEstudiante(estudiante);
        reserva.setSala(sala);
        reserva.setFechaSolicitada(new Date());
        reserva.setHoraSolicitada(new Date());
        reserva.setHoraCierre(new Date(System.currentTimeMillis() + 3_600_000));
        reserva.setEstado(1);

        return reserva;
    }
}
