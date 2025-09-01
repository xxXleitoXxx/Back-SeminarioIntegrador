package org.example.dto;

import lombok.*;
import org.example.entity.Dia;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class ClaseDTO {
    private Long nroClase;
    private Date fechaBajaClase;
    private Date fechaHoraClase;
    private DiaDTO diaClase;
    private TipoClaseDTO tipoClase;
    private List<ProfesorDto> profesores;

}
