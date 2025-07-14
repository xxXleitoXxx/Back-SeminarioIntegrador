package org.example.services;

import org.example.entity.Localidad;
import org.example.repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalidadServiceImpl{
    @Autowired
    LocalidadRepository  localidadRepository;

}
