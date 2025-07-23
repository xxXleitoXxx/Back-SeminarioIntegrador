package org.example.services;

import org.example.dto.ProfesorDto;
import org.example.entity.Profesor;
import org.example.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfesorService {
    @Autowired
    ProfesorRepository profesorRepository;

    //metodo para crear un nuevo profesor
    public Profesor crearProfesor(ProfesorDto nuevoprofe){
        //validamos que no exista el profesor en la base de datos
        existeProfesorPorDni(nuevoprofe.getDniProfesor());

        Profesor profesor= new Profesor();
        profesor.setCodProfesor(nuevoprofe.getCodProfesor());
        profesor.setDniProfesor(nuevoprofe.getDniProfesor());
        profesor.setNombreProfesor(nuevoprofe.getNombreProfesor());
        profesor.setTelefonoProfesor(nuevoprofe.getTelefonoProfesor());
        profesor.setFechaBajaProfesor(nuevoprofe.getFechaBajaProfesor());
        return profesorRepository.save(profesor);

    }

    //Metodo para validar si un profesor existe por su DNI
    public void existeProfesorPorDni(int dniProfesor) {
        if(profesorRepository.findByDniProfesor(dniProfesor).isPresent()) {;
            throw new IllegalArgumentException("Ya existe un profesor con el DNI proporcionado");
        }
    }

    //Metodo para modificar profesor
    public Profesor modificarProfesor(int dniProfesor, ProfesorDto profeactualizado){

        //Buscar profesor
        Profesor profesorexistente= profesorRepository.findByDniProfesor(dniProfesor) .orElseThrow(() -> new IllegalArgumentException("No existe un profesor con el dni " + dniProfesor));

        //Actualizar datos
        profesorexistente.setTelefonoProfesor(profeactualizado.getTelefonoProfesor());
//        profesorexistente.setFechaBajaProfesor(profeactualizado.getFechaBajaProfesor());
        profesorexistente.setNombreProfesor(profeactualizado.getNombreProfesor());
        profesorexistente.setDniProfesor(profeactualizado.getDniProfesor());

        return profesorRepository.save(profesorexistente);

    }

}
