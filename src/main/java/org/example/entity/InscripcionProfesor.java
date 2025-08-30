package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "inscripcion_profesor")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class InscripcionProfesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroInscripcionProfesor;
    private Date fechaBajaInscripcionProfesor;
    private Date fechaInscripcionProfesor;
    @ManyToOne
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;
    @ManyToOne
    @JoinColumn(name = "tipoclase_id", nullable = false)
    private TipoClase tipoClase;
}
