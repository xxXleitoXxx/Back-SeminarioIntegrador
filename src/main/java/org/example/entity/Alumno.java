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

public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroAlumno;
    private int dniAlumno;
    private String domicilioAlumno;
    private Date fechaNacAlumno;
    private String nombreAlumno;
    private String apellidoAlumno;
    private long telefonoAlumno;
    private Date fechaBajaAlumno;
    private String mailAlumno;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "alumno_id") // Esto crea una columna en FichaMedica
    private List<FichaMedica> fichasMedicas = new ArrayList<>();
//tengo que poner una ficha medica por pdf, lo cargo a la base de datos como un blob?
    @ManyToOne
    @JoinColumn(name = "localidad_id", nullable = false)
    private Localidad localidadAlumno;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "alumno_id", nullable = false)
    private List<ContactoEmergencia> contactosEmergencia = new ArrayList<>();



    // Getters and Setters
}
