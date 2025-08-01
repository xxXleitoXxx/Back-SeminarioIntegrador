package org.example.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesorDto {
    private Long codProfesor;
    private int dniProfesor;
    private String nombreProfesor;
    private int telefonoProfesor;
    private Date fechaBajaProfesor;
}

