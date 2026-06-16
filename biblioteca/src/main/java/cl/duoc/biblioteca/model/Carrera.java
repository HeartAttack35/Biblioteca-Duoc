package cl.duoc.biblioteca.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "carrera")
@Schema(description = "Entidad que representa una carrera académica en el sistema")
public class Carrera {

    @Id
    @Schema(description = "Código identificador único de la carrera", example = "ING-INF")
    @Column(name = "codigo")
    private String codigo;

    @Schema(description = "Nombre completo de la carrera", example = "Ingeniería en Informática")
    @Column(name = "nombre")
    private String nombre;
}
