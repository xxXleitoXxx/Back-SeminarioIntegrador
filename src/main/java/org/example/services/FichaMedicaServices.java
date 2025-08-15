package org.example.services;

import org.example.dto.FichaMedicaDTO;
import org.example.entity.Alumno;
import org.example.entity.FichaMedica;
import org.example.repository.AlumnoRepository;
import org.example.repository.FichaMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FichaMedicaServices {

    @Autowired
    FichaMedicaRepository fichaMedicaRepository;
    @Autowired
    AlumnoRepository alumnoRepository;

    //Metodo Para Agregar una ficha medica a un Alumno
    public void agregarFichaMedica(Long AlumnoId,FichaMedicaDTO fichaMedicaDTO) {
        Alumno alumno = alumnoRepository.findById(AlumnoId)
                .orElseThrow(() -> new NoSuchElementException("Alumno no encontrado con ID: " + AlumnoId));

        FichaMedica fichaMedica= new FichaMedica();
        fichaMedica.setArchivo(fichaMedicaDTO.getArchivo());
        List<FichaMedica> fichaMedicaList= new ArrayList<>();
        fichaMedicaList.add(fichaMedica);
        // Buscamos el alumno por su ID

        alumno.getFichasMedicas().add(fichaMedica);
        alumnoRepository.save(alumno);
    // Lógica para agregar la ficha médica
        // Aquí se podría llamar a un repositorio para guardar la ficha médica en la base de datos
    }

    public List<FichaMedica> getFichaMedicasPorAlumno(Long nroAlumno) {

        Alumno alumno =alumnoRepository.findById(nroAlumno).orElseThrow(() -> new NoSuchElementException("Alumno no encontrado con nroAlumno: " + nroAlumno));
        List<FichaMedica> fichasMedicas = alumno.getFichasMedicas();
        return fichasMedicas;
    }
}
