package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clase")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroClase;
    private Date fechaBajaClase;
    private Date fechaHoraClase;
    @ManyToMany
    @JoinTable(
            name = "profesor_clase",
            joinColumns = @JoinColumn(name = "cod_clase"),
            inverseJoinColumns = @JoinColumn(name = "nro_profesor")
    )
    private List<Profesor> profesores = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "tipoclase_id", nullable = false)
    private TipoClase tipoClase;
    @ManyToOne
    @JoinColumn(name = "dia_id", nullable = false)
    private Dia diaClase;
}
