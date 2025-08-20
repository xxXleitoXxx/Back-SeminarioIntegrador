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
@ManyToMany
    @JoinTable(
            name = "profesor_tipoclase",
            joinColumns = @JoinColumn(name = "cod_tipoclase"),
            inverseJoinColumns = @JoinColumn(name = "nro_profesor")
    )
    private List<Profesor> profesores = new ArrayList<>();

    // Otros atributos y métodos según sea necesario
}
