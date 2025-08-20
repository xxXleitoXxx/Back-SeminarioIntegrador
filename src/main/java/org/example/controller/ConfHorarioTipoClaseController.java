package org.example.controller;


import org.example.dto.ConfHorarioTipoClaseDTO;
import org.example.dto.DiaDTO;
import org.example.dto.HorarioiDiaxTipoClaseDTO;
import org.example.entity.ConfHorarioTipoClase;
import org.example.entity.ContactoEmergencia;
import org.example.entity.Dia;
import org.example.entity.HorarioiDiaxTipoClase;
import org.example.services.ConfHorarioTipoClaseService;
import org.example.services.HorarioiDiaxTipoClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "api/v1/cronograma")
@CrossOrigin(origins = "*")
public class ConfHorarioTipoClaseController {
    @Autowired
 ConfHorarioTipoClaseService confHorarioTipoClaseService;
    @Autowired
    HorarioiDiaxTipoClaseService horarioiDiaxTipoClaseService;

    @PostMapping
    public ResponseEntity<?> crearConf(@RequestBody ConfHorarioTipoClaseDTO confHorarioTipoClaseDTO){
        try{
            //Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            ConfHorarioTipoClase confHorarioTipoClase = confHorarioTipoClaseService.crearConfHorarioTipoClase(confHorarioTipoClaseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(confHorarioTipoClase);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear la configuracion: " + e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<?> obtenerConfigs() {
        try {
            List<ConfHorarioTipoClase> configuraciones = confHorarioTipoClaseService.getConfHorarioTipoClase();
            return ResponseEntity.ok(configuraciones); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al obtener las configuraciones: " + e.getMessage());
        }
    }


}
