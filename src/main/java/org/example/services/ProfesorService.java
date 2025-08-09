package org.example.services;

import org.example.dto.ProfesorDto;
import org.example.entity.Profesor;
import org.example.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProfesorService {
    @Autowired
    ProfesorRepository profesorRepository;

    //metodo para crear un nuevo profesor
    public Profesor crearProfesor(ProfesorDto nuevoprofe){
        //validamos que no exista el profesor en la base de datos
        existeProfesorPorDni(nuevoprofe.getDniProfesor());

        Profesor profesor= new Profesor();
        profesor.setNroProfesor(nuevoprofe.getNroProfesor());
        profesor.setDniProfesor(nuevoprofe.getDniProfesor());
        profesor.setNombreProfesor(nuevoprofe.getNombreProfesor());
        profesor.setTelefonoProfesor(nuevoprofe.getTelefonoProfesor());
        profesor.setFechaBajaProfesor(nuevoprofe.getFechaBajaProfesor());
        return profesorRepository.save(profesor);

    }

    //Metodo para validar si un profesor existe por su DNI
    public void existeProfesorPorDni(int dniProfesor) {
        profesorRepository.findByDniProfesor(dniProfesor).ifPresent(profesor -> {
            if (profesor.getFechaBajaProfesor() == null) {
                throw new IllegalArgumentException("Ya existe un profesor activo con el DNI proporcionado");
            }
        });
    }

    //modificar profesor
    public Profesor modificarProfesor(int dniProfesor, ProfesorDto profeactualizado) {
        Profesor profesorexistente = profesorRepository.findByDniProfesor(dniProfesor)
                .orElseThrow(() -> new IllegalArgumentException("No existe un profesor con el DNI " + dniProfesor));

        // Validar que no esté dado de baja
        if (profesorexistente.getFechaBajaProfesor() != null) {
            throw new IllegalStateException("No se puede modificar un profesor dado de baja");
        }

        // Actualizar datos permitidos
        profesorexistente.setTelefonoProfesor(profeactualizado.getTelefonoProfesor());
        profesorexistente.setNombreProfesor(profeactualizado.getNombreProfesor());
        // No actualizar DNI ni fecha de baja desde aquí

        return profesorRepository.save(profesorexistente);
    }


    //Traer todos los profesores
    public List<Profesor> getProfesores() {

        List<Profesor> profesores = profesorRepository.findAll();
        if (profesores.isEmpty()) {
            throw new IllegalArgumentException("No existen profesores registrados");
        }
        return profesores;
    }
    //Metodo para dar de baja profesor
    public Profesor bajaProfesor(int dniProfesor, Date fechaBaja){
        Profesor profesorexistente= profesorRepository.findByDniProfesor(dniProfesor) .orElseThrow(() -> new IllegalArgumentException("No existe un profesor con el dni " + dniProfesor));

        // Validar que no esté dado de baja
        if (profesorexistente.getFechaBajaProfesor() != null) {
            throw new RuntimeException("El profesor ya esta dado de baja");
        }
        //Actualizar datos
        profesorexistente.setFechaBajaProfesor(fechaBaja);

        return profesorRepository.save(profesorexistente);
    }

}


