package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "rango_etario")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RangoEtario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codRangoEtario;
    private int edadDesde;
    private int edadHasta;
    private Date fechaBajaRangoEtario;

}
