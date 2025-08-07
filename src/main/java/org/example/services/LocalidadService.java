package org.example.services;

import org.example.entity.Localidad;
import org.example.entity.Profesor;
import org.example.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LocalidadService {
    @Autowired
    LocalidadRepository  localidadRepository;

    public List<Localidad> getLocalidades() {
        List<Localidad> localidades = localidadRepository.findAll();
        if (localidades.isEmpty()) {
            throw new NoSuchElementException("No existen localidades registradas");
        }
        return localidades;

    }

    //buscar y retorstaticnar Localidad por codigo y fecha de baja
    public Localidad buscarLocalidadPorCodigoYFechaBaja(Long codLocalidad) {
        return localidadRepository.findByCodLocalidadAndFechaBajaLocalidad(codLocalidad, null).orElseThrow(() -> new NoSuchElementException("No se encontró la localidad con el código proporcionado o está dada de baja."));
    }
}
