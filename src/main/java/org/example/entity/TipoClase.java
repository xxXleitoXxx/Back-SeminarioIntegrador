package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tipoclase")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TipoClase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codTipoClase;
    private int cupoMaxTipoClase;
    private Date fechaBajaTipoClase;
    private String nombreTipoClase;
    @ManyToOne
    @JoinColumn(name = "rangoetario_id", nullable = false)
    private RangoEtario rangoEtario;

}
