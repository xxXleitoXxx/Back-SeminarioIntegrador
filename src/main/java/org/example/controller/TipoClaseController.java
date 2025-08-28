package org.example.controller;

import org.example.dto.LocalidadDto;
import org.example.dto.TipoClaseDTO;
import org.example.entity.Localidad;
import org.example.entity.TipoClase;
import org.example.services.LocalidadService;
import org.example.services.TipoClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/tipoclases")
@CrossOrigin(origins = "*")

public class TipoClaseController {
    @Autowired
    private TipoClaseService tipoClaseService;

    //POST Crear TipoClase
    @PostMapping
    public ResponseEntity<?> crearTipoClase(@RequestBody TipoClaseDTO tipoClaseDTO){
        try{
            //Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            TipoClaseDTO tipoClaseDTO1 = tipoClaseService.crearTipoClase(tipoClaseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(tipoClaseDTO1);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el tipoclase: " + e.getMessage());
        }
    }

    //PUT Modificar TipoClase
    @PutMapping("/{cod}")
    public ResponseEntity<?> modificarTipoClase(@PathVariable("cod") Long  codTipoClase,
                                                @RequestBody TipoClaseDTO tipoClaseDTO) {
        try {
            TipoClaseDTO tipoClase = tipoClaseService.modificarTipoClase(codTipoClase, tipoClaseDTO);
            return ResponseEntity.ok(tipoClase);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al modificar el tipoclase: " + e.getMessage());
        }
    }

    // PUT: Dar de baja localidad  por codigo
    @PutMapping("/{cod}/baja")
    public ResponseEntity<?> bajaTipoClase(@PathVariable("cod") Long codTipoClase) {
        try {
            TipoClaseDTO tipoClase = tipoClaseService.bajaTipoClase(codTipoClase, new Date());
            return ResponseEntity.ok(tipoClase);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al dar de baja el tipoclase: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getTipoClases() {
        try {
            // Esto deberia manejarse en el servicio, pero por simplicidad lo hacemos aqui
            List<TipoClaseDTO> tipoClases = tipoClaseService.getTipoClases();
            return ResponseEntity.status(HttpStatus.CREATED).body(tipoClases);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener los tipoclases: " + e.getMessage());
        }
    }
}
