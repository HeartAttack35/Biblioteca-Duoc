package cl.duoc.biblioteca.ms_libro.service;

import cl.duoc.biblioteca.ms_libro.dto.LibroResponse;
import cl.duoc.biblioteca.ms_libro.model.Libro;
import cl.duoc.biblioteca.ms_libro.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"unchecked", "rawtypes"})
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private WebClient autorWebClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private LibroService libroService;

    private Libro libro1;

    @BeforeEach
    void setUp() {
        libroService = new LibroService(libroRepository, autorWebClient);

        libro1 = new Libro("Cien años de soledad", 1);
        libro1.setId(1);
    }

    @Test
    void listarTodosConToken_cuandoMsAutorFalla_debeRetornarFallback() {
        when(libroRepository.findAll()).thenReturn(List.of(libro1));
        when(autorWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(Class.class))).thenThrow(new RuntimeException("ms-autor no disponible"));

        List<LibroResponse> resultado = libroService.listarTodosConToken(null);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getAutor().getNombre().contains("Autor no disponible"));
    }

    @Test
    void listarTodosConToken_conListaVacia_debeRetornarListaVacia() {
        when(libroRepository.findAll()).thenReturn(List.of());

        List<LibroResponse> resultado = libroService.listarTodosConToken(null);

        assertTrue(resultado.isEmpty());
        verify(autorWebClient, never()).get();
    }

    @Test
    void crearLibroConToken_cuandoMsAutorFalla_debePersistirLibroConFallback() {
        Libro libroNuevo = new Libro("La sombra del viento", 2);
        Libro libroGuardado = new Libro("La sombra del viento", 2);
        libroGuardado.setId(5);

        when(libroRepository.save(libroNuevo)).thenReturn(libroGuardado);
        when(autorWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(Class.class))).thenThrow(new RuntimeException("ms-autor caído"));

        LibroResponse resultado = libroService.crearLibroConToken(libroNuevo, null);

        assertNotNull(resultado);
        assertEquals("La sombra del viento", resultado.getTitulo());
        assertEquals(5, resultado.getId());
        assertTrue(resultado.getAutor().getNombre().contains("Autor no disponible"));
        verify(libroRepository, times(1)).save(libroNuevo);
    }
}
