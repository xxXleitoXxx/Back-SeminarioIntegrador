package org.example.Reports;

import org.example.dto.ProfesorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/reporteGralHist")
@CrossOrigin(origins = "*")
public class ReporteGralHistController {
    @Autowired
    ReporteGralHistService reporteGralHistService;
    @GetMapping
    public ResponseEntity<?> getReporteGralHist(){
        try{
            ReporteGralHistDTO reporteGralHistDTO= reporteGralHistService.reporteGral();
            return ResponseEntity.status(HttpStatus.CREATED).body(reporteGralHistDTO);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al Obtener Reporte: " + e.getMessage());
        }
    }}
