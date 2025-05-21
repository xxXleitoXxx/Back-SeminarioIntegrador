package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "alumno")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class Alumno extends BaseEntity{

    private int dniAlumno;
    private String domicilioAlumno;
    private Date fechaNacAlumno;
    private String nombreAlumno;
    private long telefono;
    @ManyToOne
    @JoinColumn(name = "localidad_id", nullable = false)
    private Localidad localidad;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "alumno_id", nullable = false)
    private List<ContactoEmergencia> contactosEmergencia = new ArrayList<>();



    // Getters and Setters
}
