package org.example.services;

import org.example.entity.*;
import org.example.repository.ClaseAlumnoRepository;
import org.example.repository.ClaseRepository;
import org.example.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaseAlumnoService {
    @Autowired
    ClaseAlumnoRepository claseAlumnoRepository;
    @Autowired
    ClaseRepository claseRepository;
    @Autowired
    InscripcionRepository inscripcionRepository;

    public void generarClaseAlumno(Long nroClase){
        //busco la clase
        Clase clase = claseRepository.findByNroClaseAndFechaBajaClaseIsNull(nroClase).orElseThrow(() -> new IllegalArgumentException("No existe una clase con el numero " + nroClase));
        TipoClase tipoClase = clase.getTipoClase();

        //busco las inscripciones activas del tipo de clase
        List<Inscripcion>  inscripcions = inscripcionRepository.findByTipoClaseAndFechaBajaInscripcionIsNull(tipoClase);
        for (Inscripcion inscripcion : inscripcions) {
            ClaseAlumno claseAlumno = new ClaseAlumno();
            claseAlumno.setClase(clase);
            claseAlumno.setAlumno(inscripcion.getAlumno());
            claseAlumnoRepository.save(claseAlumno);

        }

    }
    public void generarUnClaseAlumno(TipoClase tipoClase, Alumno alumno){
            //busco las clases activas del tipo de clase
            List<Clase> clases = claseRepository.findByTipoClaseAndFechaBajaClaseIsNull(tipoClase);
            //crear una clase alumno por cada clase activa
            for (Clase clase : clases) {
                ClaseAlumno claseAlumno = new ClaseAlumno();
                claseAlumno.setClase(clase);
                claseAlumno.setAlumno(alumno);
                claseAlumnoRepository.save(claseAlumno);

            }
    }
}
