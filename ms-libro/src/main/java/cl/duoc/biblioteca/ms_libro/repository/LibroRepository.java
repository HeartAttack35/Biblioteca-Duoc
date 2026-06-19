package cl.duoc.biblioteca.ms_libro.repository;

import cl.duoc.biblioteca.ms_libro.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {
}