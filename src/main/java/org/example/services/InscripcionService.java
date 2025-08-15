package org.example.services;

import org.example.entity.*;
import org.example.repository.AlumnoRepository;
import org.example.repository.InscripcionRepository;
import org.example.repository.TipoClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class InscripcionService {
    @Autowired
    InscripcionRepository inscripcionRepository;
    @Autowired
    AlumnoRepository alumnoRepository;
    @Autowired
    TipoClaseRepository tipoClaseRepository;
    public Inscripcion inscribirAlumno(int dniAlumno, Long codTipoClase) {
        // Buscar alumno y validar que no esté dado de baja
        Alumno alumno = alumnoRepository.findByDniAlumno(dniAlumno)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
        if (alumno.getFechaBajaAlumno() != null) {
            throw new IllegalArgumentException("Alumno dado de baja");
        }

        // Buscar tipo de clase y validar que no esté dado de baja
        TipoClase tipoClase = tipoClaseRepository.findBycodTipoClase(codTipoClase)
                .orElseThrow(() -> new IllegalArgumentException("TipoClase no encontrada"));
        if (tipoClase.getFechaBajaTipoClase() != null) {
            throw new IllegalArgumentException("TipoClase dada de baja");
        }

        // Validaciones de negocio
        existeInscripcion(alumno, tipoClase); // Ya inscripto
        edadAdecuada(alumno, tipoClase);      // Rango de edad permitido
        cupoClase(tipoClase);                 // Cupo disponible

        // Crear inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setFechaInscripcion(new Date()); // Fecha y hora actual
        inscripcion.setFechaBajaInscripcion(null);
        inscripcion.setNroInscripcion(null); // Si es autogenerado por la DB, lo podés omitir
        inscripcion.setAlumno(alumno);
        inscripcion.setTipoClase(tipoClase);

        return inscripcionRepository.save(inscripcion);
    }


    public void existeInscripcion(Alumno alumno,TipoClase tipoClase){
        inscripcionRepository.findByAlumnoAndTipoClase(alumno,tipoClase).ifPresent(inscripcion -> {
            if (inscripcion.getFechaBajaInscripcion() == null){
                throw new IllegalArgumentException("Ya esta inscripto");
            }
        });
    }
    public void edadAdecuada(Alumno alumno,TipoClase tipoClase){
        int edadAlumno = calcularEdad(alumno.getFechaNacAlumno());

        RangoEtario rangoEtario = tipoClase.getRangoEtario();

        if (edadAlumno >= rangoEtario.getEdadDesde() && edadAlumno <= rangoEtario.getEdadHasta()) {
            // Edad adecuada, continuar
        } else {
            throw new IllegalArgumentException("El alumno no cumple con la edad requerida para esta clase.");
        }
    }



    public int calcularEdad(Date fechaNacimiento) {
        LocalDate fechaNac = fechaNacimiento.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return Period.between(fechaNac, LocalDate.now()).getYears();
    }

    public void cupoClase(TipoClase tipoClase) {
        int cupoActual = 0;

        // Traer inscripciones activas para este tipo de clase
        List<Inscripcion> inscripcionesActivas =
                inscripcionRepository.findByTipoClase(tipoClase);

        // Contar las inscripciones
        for (Inscripcion inscripcion : inscripcionesActivas) {
            cupoActual++;
        }

        // Validar contra el cupo máximo
        if (cupoActual >= tipoClase.getCupoMaxTipoClase()) {
            throw new IllegalStateException("Ya no hay cupos para esta clase");
        }
    }


    public Inscripcion bajaInscripcion(Long nroInscripcion){
        Inscripcion bajaInscripto = inscripcionRepository.findByNroInscripcion(nroInscripcion).orElseThrow(() -> new IllegalArgumentException("Inscripcion no encontrada"));
        if (bajaInscripto.getFechaBajaInscripcion()  != null){
            throw new IllegalArgumentException("Inscripcion y  dada de baja");
        }

        bajaInscripto.setFechaBajaInscripcion(new Date());
        return inscripcionRepository.save(bajaInscripto);

    }

    public List<Inscripcion> getInscripciones() {

        List<Inscripcion> inscripciones = inscripcionRepository.findAll();

        return inscripciones;
    }










}
