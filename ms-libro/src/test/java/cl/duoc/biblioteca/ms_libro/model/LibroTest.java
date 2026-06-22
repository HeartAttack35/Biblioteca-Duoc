package cl.duoc.biblioteca.ms_libro.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibroTest {

    @Test
    void constructorConParametros_debeAsignarCamposCorrectamente() {
        Libro libro = new Libro("Cien años de soledad", 1);

        assertEquals("Cien años de soledad", libro.getTitulo());
        assertEquals(1, libro.getIdAutor());
    }

    @Test
    void constructorVacio_debeInstanciarSinExcepcion() {
        assertDoesNotThrow(() -> new Libro());
    }

    @Test
    void setters_debenActualizarCamposCorrectamente() {
        Libro libro = new Libro();
        libro.setId(10);
        libro.setTitulo("El amor en los tiempos del cólera");
        libro.setIdAutor(2);

        assertEquals(10, libro.getId());
        assertEquals("El amor en los tiempos del cólera", libro.getTitulo());
        assertEquals(2, libro.getIdAutor());
    }
}
