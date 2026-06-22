package cl.duoc.biblioteca.ms_autor.service;

import cl.duoc.biblioteca.ms_autor.model.Autor;
import cl.duoc.biblioteca.ms_autor.repository.AutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Transactional(readOnly = true)
    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Autor> buscarPorId(Integer id) {
        return autorRepository.findById(id);
    }

    @Transactional
    public Autor crearAutor(Autor autor) {
        return autorRepository.save(autor);
    }
}