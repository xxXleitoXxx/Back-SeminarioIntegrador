package org.example.repository;

import org.example.entity.Alumno;
import org.example.entity.Inscripcion;
import org.example.entity.Localidad;
import org.example.entity.TipoClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    // Buscar si ya existe una inscripción activa para un alumno en una clase (no dada de baja)
    Optional<Inscripcion> findByAlumnoAndTipoClaseAndFechaBajaInscripcionIsNull(Alumno alumno, TipoClase tipoClase);

    // Traer todas las inscripciones activas de una clase (para contar cupo)
    List<Inscripcion> findByTipoClase(TipoClase tipoClase);
    List<Inscripcion> findByTipoClaseAndFechaBajaInscripcionIsNull(TipoClase tipoClase);
    Optional<Inscripcion> findByNroInscripcion(Long nroInscripcion);

    List<Inscripcion> findByAlumnoAndFechaBajaInscripcionIsNull(Alumno alumno);
}
