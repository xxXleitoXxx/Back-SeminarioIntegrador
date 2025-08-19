package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HorarioiDiaxTipoClase")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioiDiaxTipoClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horarioidiaxtipoclase_id")
    private Long nroHFxTC;

    private Date fechaBajaHFxTC;
    private Time horaDesde;
    private Time horaHasta;

   @ManyToOne
   @JoinColumn(name = "dia_id", nullable = false)
    private Dia dia;
    @ManyToOne
    @JoinColumn(name = "tipoclase_id", nullable = false)
    private TipoClase tipoClase;

}






