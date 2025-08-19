package org.example.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.example.entity.Dia;
import org.example.entity.TipoClase;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter

public class HorarioiDiaxTipoClaseDTO {
    private Long nroHFxTC;
    private Date fechaBajaHFxTC;
    private Time horaDesde;
    private Time horaHasta;
    private DiaDTO diaDTO;
    private TipoClaseDTO tipoClase;
}
