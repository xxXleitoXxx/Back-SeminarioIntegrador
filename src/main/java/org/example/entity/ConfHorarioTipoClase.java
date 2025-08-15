package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ConfHorarioTipoClase")
@Data
@Getter
@Setter
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
    @JoinColumn(name = "horarioidiaxtipoclase_id", nullable = false)
    private List<HorarioiDiaxTipoClase> horarioiDiaxTipoClaseList;
}
