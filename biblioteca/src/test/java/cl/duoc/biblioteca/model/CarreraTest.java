package cl.duoc.biblioteca.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios del modelo Carrera.
 * Validan que el modelo se construye correctamente y que los
 * getters/setters funcionan según lo esperado.
 */
public class CarreraTest {

    // ─── Caso feliz ───────────────────────────────────────────────────

    @Test
    void dadoCodigoYNombre_cuandoSeCrean_entoncesSeAsignanCorrectamente() {
        // Given
        String codigo = "ING-INF";
        String nombre = "Ingeniería en Informática";

        // When
        Carrera carrera = new Carrera();
        carrera.setCodigo(codigo);
        carrera.setNombre(nombre);

        // Then
        assertEquals(codigo, carrera.getCodigo());
        assertEquals(nombre, carrera.getNombre());
    }

    @Test
    void dadosDosCodigos_cuandoSonIguales_entoncesCarrerasSonIguales() {
        // Given
        Carrera c1 = new Carrera();
        c1.setCodigo("ING-INF");
        c1.setNombre("Ingeniería en Informática");

        Carrera c2 = new Carrera();
        c2.setCodigo("ING-INF");
        c2.setNombre("Ingeniería en Informática");

        // Then — Lombok @Data genera equals basado en todos los campos
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    // ─── Caso de error ────────────────────────────────────────────────

    @Test
    void dadoNombreNulo_cuandoSeAsigna_entoncesGetterRetornaNulo() {
        // Given
        Carrera carrera = new Carrera();
        carrera.setCodigo("TEST-001");

        // When — se deja nombre sin asignar
        // Then
        assertNull(carrera.getNombre(),
                "El nombre debe ser null si no se asigna");
    }

    @Test
    void dadosDosCodigos_cuandoSonDistintos_entoncesCarrerasNoSonIguales() {
        // Given
        Carrera c1 = new Carrera();
        c1.setCodigo("ING-INF");
        c1.setNombre("Ingeniería en Informática");

        Carrera c2 = new Carrera();
        c2.setCodigo("ADM-EMP");
        c2.setNombre("Administración de Empresas");

        // Then
        assertNotEquals(c1, c2);
    }
}
