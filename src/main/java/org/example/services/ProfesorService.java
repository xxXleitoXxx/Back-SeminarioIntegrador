package org.example.services;

import org.example.dto.ProfesorDto;
import org.example.entity.Profesor;
import org.example.repository.InscripcionProfesorRepository;
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
    @Autowired
    InscripcionProfesorRepository inscripcionProfesorRepository;

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

        //creamos el profesor
        Profesor profesor= new Profesor();
        profesor.setNroProfesor(nuevoprofe.getNroProfesor());
        profesor.setDniProfesor(nuevoprofe.getDniProfesor());
        profesor.setNombreProfesor(nuevoprofe.getNombreProfesor());
        profesor.setTelefonoProfesor(nuevoprofe.getTelefonoProfesor());
        profesor.setFechaBajaProfesor(nuevoprofe.getFechaBajaProfesor());
        profesor.setEmailProfesor(nuevoprofe.getEmailProfesor());
        profesorRepository.save(profesor);

        //creamos el dto para devolver
        ProfesorDto  profesorDTO = new ProfesorDto();
        profesorDTO.setNroProfesor(profesor.getNroProfesor());
        profesorDTO.setDniProfesor(profesor.getDniProfesor());
        profesorDTO.setNombreProfesor(profesor.getNombreProfesor());
        profesorDTO.setTelefonoProfesor(profesor.getTelefonoProfesor());
        profesorDTO.setFechaBajaProfesor(profesor.getFechaBajaProfesor());
        profesorDTO.setEmailProfesor(profesor.getEmailProfesor());
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

        //Si el dni es distinto al que ya tiene, verificar que no exista otro profesor con ese dni
        if (profesorexistente.getDniProfesor() != (profeactualizado.getDniProfesor())){
            if (!profesorRepository.findByDniProfesorAndFechaBajaProfesorIsNull(profeactualizado.getDniProfesor()).isEmpty()){
                throw new IllegalArgumentException("Ya existe un profesor con ese dni.");
            }
        }

        // Actualizar datos permitidos
        profesorexistente.setTelefonoProfesor(profeactualizado.getTelefonoProfesor());
        profesorexistente.setNombreProfesor(profeactualizado.getNombreProfesor());
        profesorexistente.setDniProfesor(profeactualizado.getDniProfesor());
        profesorexistente.setEmailProfesor(profeactualizado.getEmailProfesor());
        profesorRepository.save(profesorexistente);

        // Crear y devolver el DTO actualizado
        ProfesorDto profesorDTO = new ProfesorDto();
        profesorDTO.setNroProfesor(profesorexistente.getNroProfesor());
        profesorDTO.setDniProfesor(profesorexistente.getDniProfesor());
        profesorDTO.setNombreProfesor(profesorexistente.getNombreProfesor());
        profesorDTO.setTelefonoProfesor(profesorexistente.getTelefonoProfesor());
        profesorDTO.setFechaBajaProfesor(profesorexistente.getFechaBajaProfesor());
        profesorDTO.setEmailProfesor(profesorexistente.getEmailProfesor());
        return profesorDTO;
    }


    //Traer todos los profesores
    public List<ProfesorDto> getProfesores() {
        // Traer todos los profesores
        List<Profesor> profesores = profesorRepository.findAll();

        // Verificar que no esté vacío
        if (profesores.isEmpty()) {
            throw new IllegalArgumentException("No existen profesores registrados");
        }

        // Convertir a DTO
        List<ProfesorDto> profesorDtos = new ArrayList<>();
        for (Profesor profesor : profesores) {
            ProfesorDto profesorDTO = new ProfesorDto();
            profesorDTO.setNroProfesor(profesor.getNroProfesor());
            profesorDTO.setDniProfesor(profesor.getDniProfesor());
            profesorDTO.setNombreProfesor(profesor.getNombreProfesor());
            profesorDTO.setTelefonoProfesor(profesor.getTelefonoProfesor());
            profesorDTO.setFechaBajaProfesor(profesor.getFechaBajaProfesor());
            profesorDTO.setEmailProfesor(profesor.getEmailProfesor());
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

        //Validar que no este relacionado a una inscripcion activa
        if (!inscripcionProfesorRepository.findByProfesorAndFechaBajaInscripcionProfesorIsNull(profesorexistente).isEmpty()){
            throw new IllegalStateException("No se puede dar de baja el profesor porque tiene inscripciones activas");
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
        profesorDTO.setEmailProfesor(profesorexistente.getEmailProfesor());
        return profesorDTO;

    }

}


