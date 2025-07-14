package org.example.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AlumnoDto {
    private Long id;
    private int dniAlumno;
    private String domicilioAlumno;
    private Date fechaNacAlumno;
    private String nombreAlumno;
    private int telefono;
    private int codLocalidad;
}
