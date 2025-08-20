package org.example.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "profesor")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroProfesor;
    private int dniProfesor;
    private String nombreProfesor;
    private int telefonoProfesor;
    private Date fechaBajaProfesor;
    @ManyToMany(mappedBy = "profesores")
    private List<TipoClase> tipoClase = new ArrayList<>();

}
