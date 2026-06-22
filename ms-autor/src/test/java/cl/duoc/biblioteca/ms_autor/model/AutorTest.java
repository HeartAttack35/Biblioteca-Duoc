package cl.duoc.biblioteca.ms_autor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutorTest {

    @Test
    void constructorConNombre_debeAsignarNombreCorrectamente() {
        Autor autor = new Autor("Gabriel García Márquez");
        assertEquals("Gabriel García Márquez", autor.getNombre());
    }

    @Test
    void constructorVacio_debeInstanciarSinExcepcion() {
        assertDoesNotThrow(() -> new Autor());
    }

    @Test
    void setters_debenActualizarCamposCorrectamente() {
        Autor autor = new Autor();
        autor.setId(5);
        autor.setNombre("Pablo Neruda");

        assertEquals(5, autor.getId());
        assertEquals("Pablo Neruda", autor.getNombre());
    }
}
