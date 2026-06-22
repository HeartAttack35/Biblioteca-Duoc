package cl.duoc.biblioteca.model;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios del modelo Reserva.
 * Validan la construcción del modelo y las relaciones con Estudiante y Sala.
 */
public class ReservaTest {

    // ─── Caso feliz ───────────────────────────────────────────────────

    @Test
    void dadosDatosCompletos_cuandoSeCreanConConstructor_entoncesSeAsignanCorrectamente() {
        // Given
        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setRun("12345678-9");

        Sala sala = new Sala();
        sala.setCodigo(1);
        sala.setNombre("Sala A101");

        Date fecha = new Date();
        Date horaInicio = new Date();
        Date horaCierre = new Date(System.currentTimeMillis() + 3_600_000); // +1 hora

        // When
        Reserva reserva = new Reserva(estudiante, sala, fecha, horaInicio, horaCierre, 1);
        reserva.setId(10);

        // Then
        assertEquals(10, reserva.getId());
        assertNotNull(reserva.getEstudiante());
        assertEquals("12345678-9", reserva.getEstudiante().getRun());
        assertNotNull(reserva.getSala());
        assertEquals("Sala A101", reserva.getSala().getNombre());
        assertEquals(1, reserva.getEstado());
        assertNotNull(reserva.getFechaSolicitada());
        assertTrue(reserva.getHoraCierre().after(reserva.getHoraSolicitada()),
                "La hora de cierre debe ser posterior a la hora de inicio");
    }

    @Test
    void dadaReservaActiva_cuandoSeCambiEstado_entoncesCambioEsReflejado() {
        // Given
        Reserva reserva = new Reserva();
        reserva.setEstado(1); // Activa

        // When
        reserva.setEstado(0); // Cancelada

        // Then
        assertEquals(0, reserva.getEstado());
    }

    // ─── Caso de error ────────────────────────────────────────────────

    @Test
    void dadaReservaNueva_cuandoNoSeAsignaId_entoncesIdEsNulo() {
        // Given - When
        Reserva reserva = new Reserva();

        // Then
        assertNull(reserva.getId(),
                "El ID debe ser null antes de ser persistido por JPA");
    }

    @Test
    void dadaReservaSinEstudiante_cuandoSeAccedeAlEstudiante_entoncesRetornaNulo() {
        // Given - When
        Reserva reserva = new Reserva();
        reserva.setEstado(1);

        // Then
        assertNull(reserva.getEstudiante(),
                "El estudiante debe ser null si no se asigna");
    }
}
