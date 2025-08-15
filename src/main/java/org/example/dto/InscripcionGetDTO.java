package org.example.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter

public class InscripcionGetDTO {
    private Long nroInscripcion;
    private int dniAlumno;
    private  Long codTipoClase;
    private Date fechaInscripcion;
    private Date fechaBajaInscripcion;
    private String nombreAlumno;
    private String nombreTipoClase;
}
