package org.example.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesorDto {
    private Long nroProfesor;
    private int dniProfesor;
    private String nombreProfesor;
    private int telefonoProfesor;
    private Date fechaBajaProfesor;
    private List<TipoClaseDTO> tipoClaseDTO;

}

