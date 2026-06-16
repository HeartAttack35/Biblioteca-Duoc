package cl.duoc.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.biblioteca.model.Carrera;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, String> {
}