package org.example.dto;


import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class AlumnoDto {
    private Long nroAlumno;
    private int dniAlumno;
    private String domicilioAlumno;
    private Date fechaNacAlumno;
    private String nombreAlumno;
    private String apellidoAlumno;
    private long telefonoAlumno;
    private String mailAlumno;
    private LocalidadDto localidadAlumno;
    private List<ContactoEmergenciaDTO> contactosEmergencia;
    private List<FichaMedicaDTO> fichaMedicaDTO;
    private Date fechaBajaAlumno;
}
