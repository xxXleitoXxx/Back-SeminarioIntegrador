package org.example.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;


@Entity
@Table(name = "alumno")
@Data
@Getter
@Setter
@Builder
public class Alumno extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int dniAlumno;
    private String domicilioAlumno;
    private Date fechaNacAlumno;
    private String nombreAlumno;
    private int telefono;
    @ManyToOne
    @JoinColumn(name = "localidad_id", nullable = false)
    private Localidad localidad;
    // Getters and Setters
}
