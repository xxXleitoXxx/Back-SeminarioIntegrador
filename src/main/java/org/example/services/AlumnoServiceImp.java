package org.example.services;

import org.example.entity.Alumno;
import org.example.entity.ContactoEmergencia;
import org.example.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AlumnoServiceImp {
    @Autowired
    AlumnoRepository alumnoRepository;

   

    //Añadir otro contacto de Emergencia a un Alumno
    public ContactoEmergencia agregarContactoEmergencia(Long alumnoId, ContactoEmergencia nuevoContacto) {
        Alumno alumno = alumnoRepository.findById(alumnoId).orElseThrow(() -> new NoSuchElementException("Alumno no encontrado"));

        alumno.getContactosEmergencia().add(nuevoContacto);
        alumnoRepository.save(alumno); // CascadeType.ALL guarda el contacto también

        return nuevoContacto;
    }

}
