package org.example.services;

import org.example.dto.ProfesorDto;
import org.example.entity.Profesor;
import org.example.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProfesorService {
    @Autowired
    ProfesorRepository profesorRepository;

    //metodo para crear un nuevo profesor
    public ProfesorDto crearProfesor(ProfesorDto nuevoprofe){
        //validamos que no exista el profesor en la base de datos
        profesorRepository.findByNroProfesor(nuevoprofe.getNroProfesor())
                .ifPresent(profesor -> {
                    if (nuevoprofe.getFechaBajaProfesor() == null){
                        throw new IllegalArgumentException("Ya existe un profesor con ese numero de profesor.");
                    }
                });
        //validamos que no exista el profesor en la base de datos por su dni
        if (!profesorRepository.findByDniProfesorAndFechaBajaProfesorIsNull(nuevoprofe.getDniProfesor()).isEmpty()){
            throw new IllegalArgumentException("Ya existe un profesor con ese dni.");
        }

        Profesor profesor= new Profesor();
        profesor.setNroProfesor(nuevoprofe.getNroProfesor());
        profesor.setDniProfesor(nuevoprofe.getDniProfesor());
        profesor.setNombreProfesor(nuevoprofe.getNombreProfesor());
        profesor.setTelefonoProfesor(nuevoprofe.getTelefonoProfesor());
        profesor.setFechaBajaProfesor(nuevoprofe.getFechaBajaProfesor());
        profesorRepository.save(profesor);
        ProfesorDto  profesorDTO = new ProfesorDto();
        profesorDTO.setNroProfesor(profesor.getNroProfesor());
        profesorDTO.setDniProfesor(profesor.getDniProfesor());
        profesorDTO.setNombreProfesor(profesor.getNombreProfesor());
        profesorDTO.setTelefonoProfesor(profesor.getTelefonoProfesor());
        profesorDTO.setFechaBajaProfesor(profesor.getFechaBajaProfesor());
        return profesorDTO;

    }

    //modificar profesor
    public ProfesorDto modificarProfesor(Long nroProfesor, ProfesorDto profeactualizado) {
        Profesor profesorexistente = profesorRepository.findByNroProfesor(nroProfesor)
                .orElseThrow(() -> new IllegalArgumentException("No existe un profesor con el numero: " + nroProfesor));

        // Validar que no esté dado de baja
        if (profesorexistente.getFechaBajaProfesor() != null) {
            throw new IllegalStateException("No se puede modificar un profesor dado de baja");
        }
        //validamos que no exista el profesor en la base de datos por su dni
        if (!profesorRepository.findByDniProfesorAndFechaBajaProfesorIsNull(profeactualizado.getDniProfesor()).isEmpty()){
            throw new IllegalArgumentException("Ya existe un profesor con ese dni.");
        }

        // Actualizar datos permitidos
        profesorexistente.setTelefonoProfesor(profeactualizado.getTelefonoProfesor());
        profesorexistente.setNombreProfesor(profeactualizado.getNombreProfesor());
        profesorexistente.setDniProfesor(profeactualizado.getDniProfesor());
        profesorRepository.save(profesorexistente);
        ProfesorDto profesorDTO = new ProfesorDto();
        profesorDTO.setNroProfesor(profesorexistente.getNroProfesor());
        profesorDTO.setDniProfesor(profesorexistente.getDniProfesor());
        profesorDTO.setNombreProfesor(profesorexistente.getNombreProfesor());
        profesorDTO.setTelefonoProfesor(profesorexistente.getTelefonoProfesor());
        profesorDTO.setFechaBajaProfesor(profesorexistente.getFechaBajaProfesor());
        return profesorDTO;
    }


    //Traer todos los profesores
    public List<ProfesorDto> getProfesores() {

        List<Profesor> profesores = profesorRepository.findAll();
        if (profesores.isEmpty()) {
            throw new IllegalArgumentException("No existen profesores registrados");
        }
        List<ProfesorDto> profesorDtos = new ArrayList<>();
        for (Profesor profesor : profesores) {
            ProfesorDto profesorDTO = new ProfesorDto();
            profesorDTO.setNroProfesor(profesor.getNroProfesor());
            profesorDTO.setDniProfesor(profesor.getDniProfesor());
            profesorDTO.setNombreProfesor(profesor.getNombreProfesor());
            profesorDTO.setTelefonoProfesor(profesor.getTelefonoProfesor());
            profesorDTO.setFechaBajaProfesor(profesor.getFechaBajaProfesor());
            profesorDtos.add(profesorDTO);
        }
        return profesorDtos;
    }
    //Metodo para dar de baja profesor
    public ProfesorDto bajaProfesor(Long nroProfesor, Date fechaBaja){
        Profesor profesorexistente= profesorRepository.findByNroProfesor(nroProfesor) .orElseThrow(() -> new IllegalArgumentException("No existe un profesor con el numero " + nroProfesor));

        // Validar que no esté dado de baja
        if (profesorexistente.getFechaBajaProfesor() != null) {
            throw new RuntimeException("El profesor ya esta dado de baja");
        }
        //Actualizar datos
        profesorexistente.setFechaBajaProfesor(fechaBaja);
        profesorRepository.save(profesorexistente);
        ProfesorDto profesorDTO = new ProfesorDto();
        profesorDTO.setNroProfesor(profesorexistente.getNroProfesor());
        profesorDTO.setDniProfesor(profesorexistente.getDniProfesor());
        profesorDTO.setNombreProfesor(profesorexistente.getNombreProfesor());
        profesorDTO.setTelefonoProfesor(profesorexistente.getTelefonoProfesor());
        profesorDTO.setFechaBajaProfesor(profesorexistente.getFechaBajaProfesor());
        return profesorDTO;

    }

}


