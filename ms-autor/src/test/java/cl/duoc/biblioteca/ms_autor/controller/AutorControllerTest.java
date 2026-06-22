package cl.duoc.biblioteca.ms_autor.controller;

import cl.duoc.biblioteca.ms_autor.dto.ApiResponse;
import cl.duoc.biblioteca.ms_autor.dto.AutorDTO;
import cl.duoc.biblioteca.ms_autor.model.Autor;
import cl.duoc.biblioteca.ms_autor.service.AutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutorControllerTest {

    @Mock
    private AutorService autorService;

    @InjectMocks
    private AutorController autorController;

    private Autor autor1;

    @BeforeEach
    void setUp() {
        autor1 = new Autor("Isabel Allende");
        autor1.setId(1);
    }

    @Test
    void listar_debeRetornar200ConListaDeAutores() {
        when(autorService.listarTodos()).thenReturn(List.of(autor1));

        ResponseEntity<ApiResponse<List<Autor>>> response = autorController.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getData().size());
        assertEquals("Isabel Allende", response.getBody().getData().get(0).getNombre());
    }

    @Test
    void listar_conListaVacia_debeRetornar200ConListaVacia() {
        when(autorService.listarTodos()).thenReturn(List.of());

        ResponseEntity<ApiResponse<List<Autor>>> response = autorController.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData().isEmpty());
    }

    @Test
    void obtenerPorId_conIdExistente_debeRetornar200() {
        when(autorService.buscarPorId(1)).thenReturn(Optional.of(autor1));

        ResponseEntity<ApiResponse<Autor>> response = autorController.obtenerPorId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Isabel Allende", response.getBody().getData().getNombre());
    }

    @Test
    void obtenerPorId_conIdInexistente_debeRetornar404() {
        when(autorService.buscarPorId(99)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<Autor>> response = autorController.obtenerPorId(99);

        assertEquals(HttpStatus.valueOf(404), response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("NOT_FOUND", response.getBody().getError());
    }

    @Test
    void crear_debeRetornar201ConAutorCreado() {
        Autor autorGuardado = new Autor("Nuevo Autor");
        autorGuardado.setId(5);

        when(autorService.crearAutor(any(Autor.class))).thenReturn(autorGuardado);

        AutorDTO dto = new AutorDTO();
        dto.setNombre("Nuevo Autor");

        ResponseEntity<ApiResponse<Autor>> response = autorController.crear(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Nuevo Autor", response.getBody().getData().getNombre());
        assertEquals(5, response.getBody().getData().getId());
    }
}
