package org.example.controller;


import org.example.dto.ClaseDTO;
import org.example.dto.DiaDTO;
import org.example.services.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/clases")
@CrossOrigin(origins = "*")
public class ClaseController {
    @Autowired
    ClaseService c;


    @GetMapping
    public ResponseEntity<?> getDias() {
        try {
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            List<ClaseDTO> claseDTOS = c.getClases();
            return ResponseEntity.status(HttpStatus.CREATED).body(claseDTOS);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener las clases: " + e.getMessage());
        }
    }
}
