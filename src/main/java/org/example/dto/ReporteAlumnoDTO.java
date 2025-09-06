package org.example.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReporteAlumnoDTO {
    //atributos de alumnoDto
    //atributos de ficha medica
    //atributos de asistencia
    Long nroAlumno;
    String nombre;
    String apellido;
    String email;
    String telefono;
    String direccion;
    ContactoEmergenciaDTO contactoEmergencia;
    FichaMedicaDTO fichaMedica;
}
