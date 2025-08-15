package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HorarioiDiaxTipoClase")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HorarioiDiaxTipoClase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroHFxTC;
    private Date fechaBajaHFxTC;
    private Time horaDesde;
    private Time horaHasta;
    @ManyToMany
    @JoinColumn(name = "dia_id", nullable = false)
    private List<Dia> dias;
    @ManyToOne
    @JoinColumn(name = "tipoclase_id", nullable = false)
    private TipoClase tipoClase;

}
