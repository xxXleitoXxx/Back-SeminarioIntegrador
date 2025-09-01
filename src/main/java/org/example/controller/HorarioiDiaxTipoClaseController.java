package org.example.controller;

import org.example.repository.HorarioiDiaxTipoClaseRepository;
import org.example.services.FichaMedicaServices;
import org.example.services.HorarioiDiaxTipoClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/horario")
@CrossOrigin(origins = "*")
public class HorarioiDiaxTipoClaseController {
    @Autowired
    HorarioiDiaxTipoClaseService horarioiDiaxTipoClaseService;
    @GetMapping("/{confId}")
    public ResponseEntity<?> getHorariosPorConf () {
        try {
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            return ResponseEntity.status(HttpStatus.CREATED).body(horarioiDiaxTipoClaseService.getHorarios());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener los horarios: " + e.getMessage());
        }

    }
}
