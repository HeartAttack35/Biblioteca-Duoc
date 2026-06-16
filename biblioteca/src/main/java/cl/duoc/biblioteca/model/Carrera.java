package cl.duoc.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "carrera")
public class Carrera {
    @Id
    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nombre")
    private String nombre;
}
