package cl.duoc.biblioteca.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios del modelo Estudiante.
 * Validan la construcción del modelo y la relación con Carrera.
 */
public class EstudianteTest {

    // ─── Caso feliz ───────────────────────────────────────────────────

    @Test
    void dadosDatosCompletos_cuandoSeCreanConConstructor_entoncesSeAsignanCorrectamente() {
        // Given
        Carrera carrera = new Carrera();
        carrera.setCodigo("ING-INF");
        carrera.setNombre("Ingeniería en Informática");

        // When
        Estudiante estudiante = new Estudiante(
                "12345678-9", "Juan Pérez",
                "juan.perez@duoc.cl", "D", 912345678, carrera
        );

        // Then
        assertEquals("12345678-9", estudiante.getRun());
        assertEquals("Juan Pérez", estudiante.getNombres());
        assertEquals("juan.perez@duoc.cl", estudiante.getCorreo());
        assertEquals("D", estudiante.getJornada());
        assertEquals(912345678, estudiante.getTelefono());
        assertNotNull(estudiante.getCarrera());
        assertEquals("ING-INF", estudiante.getCarrera().getCodigo());
    }

    @Test
    void dadoEstudianteSinCarrera_cuandoSeAsignaLuego_entoncesRelacionEsValida() {
        // Given
        Estudiante estudiante = new Estudiante();
        estudiante.setRun("98765432-1");
        estudiante.setNombres("María González");

        Carrera carrera = new Carrera();
        carrera.setCodigo("ADM-EMP");

        // When
        estudiante.setCarrera(carrera);

        // Then
        assertNotNull(estudiante.getCarrera());
        assertEquals("ADM-EMP", estudiante.getCarrera().getCodigo());
    }

    // ─── Caso de error ────────────────────────────────────────────────

    @Test
    void dadoEstudianteNuevo_cuandoNoSeAsignaId_entoncesIdEsNulo() {
        // Given - When
        Estudiante estudiante = new Estudiante();
        estudiante.setRun("11111111-1");
        estudiante.setNombres("Test User");

        // Then — el ID es autogenerado por JPA, debe ser null antes de persistir
        assertNull(estudiante.getId(),
                "El ID debe ser null antes de ser persistido por JPA");
    }

    @Test
    void dadoEstudianteSinCarrera_cuandoSeAccedeACarrera_entoncesRetornaNulo() {
        // Given - When
        Estudiante estudiante = new Estudiante();
        estudiante.setRun("22222222-2");

        // Then
        assertNull(estudiante.getCarrera(),
                "La carrera debe ser null si no se asigna");
    }
}
