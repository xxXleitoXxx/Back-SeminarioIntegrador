package org.example.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
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
