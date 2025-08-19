package org.example.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.example.entity.HorarioiDiaxTipoClase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class ConfHorarioTipoClaseDTO {
    private Long nroConfTC;

    private Date fechaVigenciaConf;
    private Date fechaFinVigenciaConf;
    private List<HorarioiDiaxTipoClaseDTO> horarioiDiaxTipoClaseList;
}

