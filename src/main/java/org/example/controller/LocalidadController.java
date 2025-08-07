package org.example.controller;


import org.example.entity.Localidad;
import org.example.services.LocalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/localidades")
@CrossOrigin(origins = "*")
public class LocalidadController {
    @Autowired
    private LocalidadService localidadService;

    @GetMapping
    public ResponseEntity<?> getLocalidades() {
        try{
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            List<Localidad> localidades = localidadService.getLocalidades();
            return ResponseEntity.status(HttpStatus.CREATED).body(localidades);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener las localidades: " + e.getMessage());
    }
}}
