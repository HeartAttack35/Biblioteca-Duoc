package cl.duoc.biblioteca.ms_libro.service;

import cl.duoc.biblioteca.ms_libro.dto.AutorDTO;
import cl.duoc.biblioteca.ms_libro.dto.LibroResponse;
import cl.duoc.biblioteca.ms_libro.model.Libro;
import cl.duoc.biblioteca.ms_libro.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Transactional(readOnly = true)
    public List<LibroResponse> listarTodos() {
        return libroRepository.findAll().stream().map(libro -> {
            // Nota: Aquí idealmente se consulta a ms-autor vía RestTemplate/FeignClient.
            AutorDTO autorDTO = new AutorDTO(libro.getIdAutor(), "Autor Relacionado " + libro.getIdAutor());
            return new LibroResponse(libro.getId(), libro.getTitulo(), autorDTO);
        }).collect(Collectors.toList());
    }

    @Transactional
    public LibroResponse crearLibro(Libro libro) {
        Libro libroGuardado = libroRepository.save(libro);
        AutorDTO autorDTO = new AutorDTO(libroGuardado.getIdAutor(), "Autor Relacionado " + libroGuardado.getIdAutor());
        return new LibroResponse(libroGuardado.getId(), libroGuardado.getTitulo(), autorDTO);
    }
}