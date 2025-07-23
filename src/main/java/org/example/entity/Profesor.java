package org.example.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    private Long codProfesor;
    private int dniProfesor;
    private String nombreProfesor;
    private int telefonoProfesor;
    private Date fechaBajaProfesor;
}
