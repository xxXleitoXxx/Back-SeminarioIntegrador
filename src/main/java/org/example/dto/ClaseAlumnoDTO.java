package org.example.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.example.entity.Alumno;
import org.example.entity.Clase;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class ClaseAlumnoDTO {
    private Long nroClaseAlumno;
    private Boolean presenteClaseAlumno;
    private AlumnoDto alumnodto;

}
