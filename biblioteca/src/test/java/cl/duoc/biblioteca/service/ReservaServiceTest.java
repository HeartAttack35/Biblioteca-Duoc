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

    @Test
    public void testFindAll() {
        Reserva reserva = crearReserva();
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));

        List<Reserva> reservas = reservaService.findAll();
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals(reserva.getId(), reservas.get(0).getId());
    }

    @Test
    public void testFindById() {
        Integer id = 1;
        Reserva reserva = crearReserva();
        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));

        Reserva found = reservaService.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testSave() {
        Reserva reserva = crearReserva();
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        Reserva saved = reservaService.save(reserva);
        assertNotNull(saved);
        assertEquals(1, saved.getEstado());
    }

    @Test
    public void testDeleteById() {
        Integer id = 1;
        doNothing().when(reservaRepository).deleteById(id);

        reservaService.deleteById(id);
        verify(reservaRepository, times(1)).deleteById(id);
    }

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
        reserva.setHoraCierre(new Date(System.currentTimeMillis() + 3600000)); // +1 hora
        reserva.setEstado(1);

        return reserva;
    }
}