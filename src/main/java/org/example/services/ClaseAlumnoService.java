package org.example.services;

import org.example.entity.Clase;
import org.example.entity.ClaseAlumno;
import org.example.entity.Inscripcion;
import org.example.entity.TipoClase;
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

        Clase clase = claseRepository.findByNroClaseAndFechaBajaClaseIsNull(nroClase).orElseThrow(() -> new IllegalArgumentException("No existe una clase con el numero " + nroClase));
        TipoClase tipoClase = clase.getTipoClase();
        List<Inscripcion>  inscripcions = inscripcionRepository.findByTipoClaseAndFechaBajaInscripcionIsNull(tipoClase);
        for (Inscripcion inscripcion : inscripcions) {
            ClaseAlumno claseAlumno = new ClaseAlumno();
            claseAlumno.setClase(clase);
            claseAlumno.setAlumno(inscripcion.getAlumno());
            claseAlumnoRepository.save(claseAlumno);

        }

    }
}
