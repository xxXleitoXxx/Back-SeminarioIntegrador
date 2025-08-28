package org.example.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.example.entity.Alumno;
import org.example.entity.TipoClase;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter

public class InscripcionDTO {
    private Long nroInscripcion;
    private Date fechaBajaInscripcion;
    private Date fechaInscripcion;
    private AlumnoDto alumnoDto;
    private TipoClaseDTO tipoClaseDTO;
}
