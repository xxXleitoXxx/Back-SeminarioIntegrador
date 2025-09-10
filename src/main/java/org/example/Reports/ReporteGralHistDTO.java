package org.example.Reports;


import lombok.*;
import org.example.dto.TipoClaseDTO;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class ReporteGralHistDTO {
    private int alumnoTotalesInscriptos;
    private int alumnoTotalesNoInscriptos;
    private int alumnoTotalesActivos;
    private int alumnoTotalesInactivos;
    private List<ReporteGralHistTipoClaseDTO> reporteXTipoClase;
    private List<ReporteGralHistLocalidadDTO> reporteXLocalidad;

}
