package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FichaMedica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long NroFichaMedica;
    private Date vigenciaDesde;
    private Date vigenciaHasta;
    @Column(columnDefinition = "LONGBLOB")
    @Lob
    private byte[] archivo;
    // getters y setters
}