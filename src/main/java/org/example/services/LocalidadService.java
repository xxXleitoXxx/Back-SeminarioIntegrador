package org.example.services;

import org.example.dto.LocalidadDto;
import org.example.entity.Localidad;
import org.example.entity.Profesor;
import org.example.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LocalidadService {
    @Autowired
    LocalidadRepository  localidadRepository;

    //crear nueva localidad
    public Localidad crearLocalidad(LocalidadDto localidadDto){
        //verificar que no existe o esta dado de baja
        existeLocalidad(localidadDto.getCodLocalidad());

        Localidad localidad = new Localidad();
        localidad.setCodLocalidad(localidadDto.getCodLocalidad());
        localidad.setNombreLocalidad(localidad.getNombreLocalidad());
        localidad.setFechaBajaLocalidad(localidad.getFechaBajaLocalidad());

        return localidadRepository.save(localidad);
    }

    public void existeLocalidad(Long codLocalidad) {
        localidadRepository.findByCodLocalidadAndFechaBajaLocalidad(codLocalidad, null)
                .ifPresent(localidad -> {
                    throw new IllegalArgumentException("Ya existe una localidad activa con el código proporcionado");
                });
    }



    //modificar localidad
    public Localidad  modificarLocalidad(Long codLocalidad, LocalidadDto  localidadDto){
        //verificar que exista y no este dado de baja
        Localidad localidadexistente = buscarLocalidadPorCodigoYFechaBaja(codLocalidad);
        localidadexistente.setNombreLocalidad(localidadDto.getNombreLocalidad());
        return localidadRepository.save(localidadexistente);
    }

    //dar de baja localidad
    public Localidad  bajaLocalidad(Long codLocalidad, Date fechaBajaLoc){
        //verificar que exista y no este dado de baja
        Localidad localidadexistente = buscarLocalidadPorCodigoYFechaBaja(codLocalidad);
        localidadexistente.setFechaBajaLocalidad(fechaBajaLoc);
        return localidadRepository.save(localidadexistente);
    }

    //traer todas las localidades
    public List<Localidad> getLocalidades() {
        List<Localidad> localidades = localidadRepository.findAll();

        return localidades;

    }

    //buscar y retorstaticnar Localidad por codigo y fecha de baja
    public Localidad buscarLocalidadPorCodigoYFechaBaja(Long codLocalidad) {
        return localidadRepository.findByCodLocalidadAndFechaBajaLocalidad(codLocalidad, null).orElseThrow(() -> new NoSuchElementException("No se encontró la localidad con el código proporcionado o está dada de baja."));
    }
}
