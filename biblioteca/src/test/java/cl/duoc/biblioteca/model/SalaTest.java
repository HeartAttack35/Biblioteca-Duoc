package cl.duoc.biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sala")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaTest {
    @Id
    private Integer codigo;
    private String nombre;
    private Integer capacidad;
    private Integer idInstituo;

    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private TipoSala tipoSala;

    // Getters and Setters
}