package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DiaDTO {
    private Long codDia;       // <-- corregido
    private Date fechaBajaDia;
    private String nombreDia;


}

