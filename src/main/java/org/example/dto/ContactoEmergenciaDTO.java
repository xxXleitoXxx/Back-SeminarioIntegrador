package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class ContactoEmergenciaDTO {
    private Long id;
    private String direccionContacto;
    private String nombreContacto;
    private Long telefonoContacto;
}
