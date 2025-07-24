package org.example.controller;


import org.example.dto.RangoEtarioDTO;

import org.example.entity.RangoEtario;

import org.example.services.RangoEtarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/rangosetarios")
@CrossOrigin(origins = "*")

public class RangoEtarioController {

    @Autowired
    private RangoEtarioService rangoEtarioService;

    // POST: Crear nuevo rango etario
    @PostMapping
    public ResponseEntity<?> crearRangoEtario(@RequestBody RangoEtarioDTO rangoEtarioDTO){
        try{
            RangoEtario rangoetariocreado = rangoEtarioService.crearRangoEtario(rangoEtarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(rangoetariocreado);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el rango etario: " + e.getMessage());
        }
    }

}
