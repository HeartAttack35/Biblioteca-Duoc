package cl.duoc.biblioteca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_sala")
public class TipoSala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Integer idTipo;

    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    // Constructor vacío requerido por JPA
    public TipoSala() {
    }

    // Constructor con parámetros
    public TipoSala(String nombre) {
        this.nombre = nombre;
    }

    // --- Getters y Setters ---

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}