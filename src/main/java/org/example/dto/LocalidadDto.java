package org.example.dto;

import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class LocalidadDto {
    private Long codLocalidad;
    private String nombreLocalidad;
    private Date fechaBajaLocalidad;
}
