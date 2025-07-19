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
    private Long id;
    private int dniAlumno;
    private String domicilioAlumno;
    private Date fechaNacAlumno;
    private String nombreAlumno;
    private String apellidoAlumno;
    private int telefonoAlumno;
    private String MailAlumno;
    private LocalidadDto localidadAlumno;
    private List<ContactoEmergenciaDTO> contactoEmergenciaDTO;
    private FichaMedicaDTO fichaMedicaDTO;
}
