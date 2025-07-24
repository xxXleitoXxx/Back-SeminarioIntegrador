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

public class RangoEtarioDTO {
    private Long codRangoEtario;
    private int edadDesde;
    private int edadHasta;
    private Date fechaBajaRangoEtario;
}


