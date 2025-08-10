package org.example.dto;

import lombok.*;
import org.example.entity.RangoEtario;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter

public class TipoClaseDTO {

    private Long codTipoClase;
    private int cupoMaxTipoClase;
    private Date fechaBajaTipoClase;
    private String nombreTipoClase;
    private RangoEtario rangoEtario;

}
