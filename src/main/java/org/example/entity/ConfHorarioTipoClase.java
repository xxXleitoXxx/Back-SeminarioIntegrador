package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ConfHorarioTipoClase")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfHorarioTipoClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroConfTC;

    private Date fechaVigenciaConf;
    private Date fechaFinVigenciaConf;

    @OneToMany
    @JoinColumn(name = "conf_id") // esto crea la columna conf_id en HorarioiDiaxTipoClase
    private List<HorarioiDiaxTipoClase> horarioiDiaxTipoClaseList = new ArrayList<>();
}

