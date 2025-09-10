package org.example.repository;

import org.example.entity.Clase;
import org.example.entity.ClaseAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaseAlumnoRepository extends JpaRepository<ClaseAlumno,Long> {
    List<ClaseAlumno> findByClase(Clase clase);
}
