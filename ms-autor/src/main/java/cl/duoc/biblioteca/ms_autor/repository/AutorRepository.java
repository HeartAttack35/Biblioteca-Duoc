package cl.duoc.biblioteca.ms_autor.repository;

import cl.duoc.biblioteca.ms_autor.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer> {
}