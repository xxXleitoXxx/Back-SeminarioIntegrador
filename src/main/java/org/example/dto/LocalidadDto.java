package org.example.dto;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class LocalidadDto {
    @Id
    private Long codLocalidad;
    private String nombreLocalidad;
    private Date fechaBajaLocalidad;
}
