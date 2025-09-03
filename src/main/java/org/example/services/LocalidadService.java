package org.example.services;

import org.example.dto.LocalidadDto;
import org.example.entity.Localidad;
import org.example.entity.Profesor;
import org.example.repository.AlumnoRepository;
import org.example.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LocalidadService {
    @Autowired
    LocalidadRepository  localidadRepository;
    @Autowired
    AlumnoRepository alumnoRepository;

    //crear nueva localidad
    public LocalidadDto crearLocalidad(LocalidadDto localidadDto){
        //verificar que no exista una localidad con ese nombre y que no este dada de baja
        localidadRepository.findByNombreLocalidadAndFechaBajaLocalidadIsNull(localidadDto.getNombreLocalidad())
                .ifPresent(loc -> {
                    throw new IllegalArgumentException("Ya existe una localidad con ese nombre.");
                });

        //crear localidad
        Localidad localidad = new Localidad();
        localidad.setNombreLocalidad(localidadDto.getNombreLocalidad());
        localidad.setFechaBajaLocalidad(localidadDto.getFechaBajaLocalidad());
        localidadRepository.save(localidad);

        //crear dto para devolver
        LocalidadDto localidadDTO = new LocalidadDto();
        localidadDTO.setCodLocalidad(localidad.getCodLocalidad());
        localidadDTO.setNombreLocalidad(localidad.getNombreLocalidad());
        localidadDTO.setFechaBajaLocalidad(localidad.getFechaBajaLocalidad());
        return localidadDTO;
    }

    //modificar localidad
    public LocalidadDto  modificarLocalidad(Long codLocalidad, LocalidadDto  localidadDto){
        //verificar que exista y no este dado de baja
        Localidad localidadexistente = localidadRepository.findByCodLocalidadAndFechaBajaLocalidadIsNull(codLocalidad)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la localidad con el código proporcionado o está dada de baja."));
        //si el nombre es distinto al que ya tiene, verificar que no exista otra localidad con ese nombre
        if (!localidadexistente.getNombreLocalidad().equals(localidadDto.getNombreLocalidad())){
            localidadRepository.findByNombreLocalidadAndFechaBajaLocalidadIsNull(localidadDto.getNombreLocalidad())
                    .ifPresent(loc -> {
                        throw new IllegalArgumentException("Ya existe una localidad con ese nombre.");
                    });
        }
        //modificar
        localidadexistente.setNombreLocalidad(localidadDto.getNombreLocalidad());
        localidadRepository.save(localidadexistente);

        //crear dto para devolver
        LocalidadDto  localidadDTO = new LocalidadDto();
        localidadDTO.setCodLocalidad(localidadexistente.getCodLocalidad());
        localidadDTO.setNombreLocalidad(localidadexistente.getNombreLocalidad());
        localidadDTO.setFechaBajaLocalidad(localidadexistente.getFechaBajaLocalidad());
        return localidadDTO;

    }

    //dar de baja localidad
    public LocalidadDto  bajaLocalidad(Long codLocalidad, Date fechaBajaLoc){
        //verificar que exista y no este dado de baja
        Localidad localidadexistente = localidadRepository.findByCodLocalidadAndFechaBajaLocalidadIsNull(codLocalidad)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la localidad con el código proporcionado o ya está dada de baja."));

        //verificar que no haya alumnos  asociados a esa localidad
        if (!alumnoRepository.findByLocalidadAlumnoAndFechaBajaAlumnoIsNull(localidadexistente).isEmpty()){
            throw new IllegalArgumentException("No se puede dar de baja la localidad porque hay alumnos asociados a ella.");
        }
        //dar de baja
        localidadexistente.setFechaBajaLocalidad(fechaBajaLoc);
        localidadRepository.save(localidadexistente);

        //crear dto para devolver
        LocalidadDto localidadDTO = new LocalidadDto();
        localidadDTO.setCodLocalidad(localidadexistente.getCodLocalidad());
        localidadDTO.setNombreLocalidad(localidadexistente.getNombreLocalidad());
        localidadDTO.setFechaBajaLocalidad(localidadexistente.getFechaBajaLocalidad());
        return localidadDTO;

    }

    //traer todas las localidades
    public List<LocalidadDto> getLocalidades() {
        //traer todas las localidades
        List<Localidad> localidades = localidadRepository.findAll();

        //verificar que haya localidades
        if (localidades.isEmpty()) {
            throw new NoSuchElementException("No hay localidades registradas.");
        }

        //convertir a dto
        List<LocalidadDto> localidadDtos  = new ArrayList<>();
        for (Localidad localidad : localidades) {
            LocalidadDto localidadDTO = new LocalidadDto();
            localidadDTO.setCodLocalidad(localidad.getCodLocalidad());
            localidadDTO.setNombreLocalidad(localidad.getNombreLocalidad());
            localidadDTO.setFechaBajaLocalidad(localidad.getFechaBajaLocalidad());
            localidadDtos.add(localidadDTO);
        }
        return localidadDtos;
    }

}
