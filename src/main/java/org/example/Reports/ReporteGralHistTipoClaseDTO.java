package org.example.Reports;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class ReporteGralHistTipoClaseDTO {
    private int inscriptos;
    private String nombreTipoClase;
    private int presenteTotalClases;
    private int ausenteTotalClases;
    private double porcentajeAsistencia;
}
