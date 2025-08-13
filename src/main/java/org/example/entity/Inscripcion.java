package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "inscripcion")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroInscripcion;
    private Date fechaBajaInscripcion;
    private Date fechaInscripcion;
    @ManyToOne
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;
    @ManyToOne
    @JoinColumn(name = "tipoclase_id", nullable = false)
    private TipoClase tipoClase;

}
