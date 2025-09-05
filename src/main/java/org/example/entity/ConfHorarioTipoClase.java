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

    // ConfHorarioTipoClase
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "conf_id") // columna creada en la tabla HorarioiDiaxTipoClase
    private List<HorarioiDiaxTipoClase> horarioiDiaxTipoClaseList = new ArrayList<>();


}

