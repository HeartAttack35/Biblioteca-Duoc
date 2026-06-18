package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.model.Reserva;
import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.model.Sala;
import cl.duoc.biblioteca.repository.ReservaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReservaServiceTest {

    @Autowired
    private ReservaService reservaService;

    @MockitoBean
    private ReservaRepository reservaRepository;

    @Test
    @DisplayName("Debe listar todas las reservas del sistema")
    public void testFindAll() {
        Reserva reserva = new Reserva();
        reserva.setId(1);
        reserva.setEstado(1); // 1 = Activa
        reserva.setFechaSolicitada(new Date());
        
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));

        List<Reserva> reservas = reservaService.findAll();

        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
        assertEquals(1, reservas.get(0).getEstado());
    }

    @Test
    @DisplayName("Debe registrar exitosamente una nueva reserva")
    public void testSave() {
        Estudiante estudianteMock = new Estudiante();
        estudianteMock.setId(1);
        
        Sala salaMock = new Sala();
        salaMock.setCodigo(1);

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setId(2);
        nuevaReserva.setEstudiante(estudianteMock);
        nuevaReserva.setSala(salaMock);
        nuevaReserva.setEstado(0); // 0 = Pendiente

        when(reservaRepository.save(any(Reserva.class))).thenReturn(nuevaReserva);

        Reserva guardada = reservaService.save(nuevaReserva);

        assertNotNull(guardada);
        assertEquals(2, guardada.getId());
        assertEquals(0, guardada.getEstado());
        assertEquals(1, guardada.getEstudiante().getId());
    }
}