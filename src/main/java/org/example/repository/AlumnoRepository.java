package org.example.repository;

import org.example.entity.Alumno;
import org.example.entity.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno,Long> {

    Optional<Alumno> findByDniAlumno(int dniAlumno);
    List<Alumno> findByLocalidadAlumnoAndFechaBajaAlumnoIsNull(Localidad localidadAlumno);
}
