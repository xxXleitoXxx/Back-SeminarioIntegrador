package org.example.dto;

import lombok.*;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class CumpleañosProximosDTO {
//    id: 3,
//    nombre: "Ana",
//    apellido: "López",
//    fecha: "2024-12-30",
//    diasRestantes: 8,
//    edad: 14,
    private Long id;
    private String nombre;
    private String apellido;
    private Date fecha;
    private int diasRestantes;
    private int edad;

}
