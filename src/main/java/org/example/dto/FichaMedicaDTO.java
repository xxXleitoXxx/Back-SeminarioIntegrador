package org.example.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FichaMedicaDTO {
    private Long id;

    private Date vigenciaDesde;
    private Date vigenciaHasta;
    @Lob
    private byte[] archivo; // Aquí se guarda el PDF o imagen
}
