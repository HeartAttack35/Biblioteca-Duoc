package cl.duoc.biblioteca.repository;

import cl.duoc.biblioteca.model.TipoSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoSalaRepository extends JpaRepository<TipoSala, Integer> {
    TipoSala findByNombre(String nombre);
}