package org.example.dto;

import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class FichaMedicaDTO {
    private Long id;

    private Date fechaBajaFichaMedica;

    @Lob
    private byte[] archivo; // Aquí se guarda el PDF o imagen
}
