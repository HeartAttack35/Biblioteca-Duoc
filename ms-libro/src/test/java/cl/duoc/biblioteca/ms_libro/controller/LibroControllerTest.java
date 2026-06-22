package cl.duoc.biblioteca.ms_libro.controller;

import cl.duoc.biblioteca.ms_libro.dto.ApiResponse;
import cl.duoc.biblioteca.ms_libro.dto.AutorDTO;
import cl.duoc.biblioteca.ms_libro.dto.LibroDTO;
import cl.duoc.biblioteca.ms_libro.dto.LibroResponse;
import cl.duoc.biblioteca.ms_libro.service.LibroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibroControllerTest {

    @Mock
    private LibroService libroService;

    @InjectMocks
    private LibroController libroController;

    private LibroResponse libroResponse;
    private static final String TOKEN = "Bearer token.falso";

    @BeforeEach
    void setUp() {
        AutorDTO autor = new AutorDTO(1, "Isabel Allende");
        libroResponse = new LibroResponse(1, "La casa de los espíritus", autor);
    }

    @Test
    void listar_debeRetornar200ConListaDeLibros() {
        when(libroService.listarTodosConToken(TOKEN)).thenReturn(List.of(libroResponse));

        ResponseEntity<ApiResponse<List<LibroResponse>>> response = libroController.listar(TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getData().size());
        assertEquals("La casa de los espíritus", response.getBody().getData().get(0).getTitulo());
        assertEquals("Isabel Allende", response.getBody().getData().get(0).getAutor().getNombre());
    }

    @Test
    void listar_conListaVacia_debeRetornar200ConListaVacia() {
        when(libroService.listarTodosConToken(TOKEN)).thenReturn(List.of());

        ResponseEntity<ApiResponse<List<LibroResponse>>> response = libroController.listar(TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData().isEmpty());
    }

    @Test
    void crear_debeRetornar201ConLibroCreado() {
        AutorDTO autor = new AutorDTO(1, "Mario Vargas Llosa");
        LibroResponse libroCreado = new LibroResponse(5, "La ciudad y los perros", autor);

        when(libroService.crearLibroConToken(any(), eq(TOKEN))).thenReturn(libroCreado);

        LibroDTO dto = new LibroDTO();
        dto.setTitulo("La ciudad y los perros");
        dto.setIdAutor(1);

        ResponseEntity<ApiResponse<LibroResponse>> response = libroController.crear(dto, TOKEN);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("La ciudad y los perros", response.getBody().getData().getTitulo());
        assertEquals(5, response.getBody().getData().getId());
    }

    @Test
    void crear_debeInvocarServicioConElTokenCorrecto() {
        when(libroService.crearLibroConToken(any(), eq(TOKEN))).thenReturn(libroResponse);

        LibroDTO dto = new LibroDTO();
        dto.setTitulo("Cualquier titulo");
        dto.setIdAutor(1);

        libroController.crear(dto, TOKEN);

        // Verifica que el token se propaga correctamente al servicio
        verify(libroService, times(1)).crearLibroConToken(any(), eq(TOKEN));
    }
}
