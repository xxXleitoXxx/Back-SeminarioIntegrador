package org.example.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Profesor;
import org.example.entity.TipoClase;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class InscripcionProfesorDTO {
    private Long nroInscripcionProfesor;
    private Date fechaBajaInscripcionProfesor;
    private Date fechaInscripcionProfesor;
    private ProfesorDto profesor;
    private TipoClaseDTO tipoClase;
}
