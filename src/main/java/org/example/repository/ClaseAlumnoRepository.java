package org.example.repository;

import org.example.entity.ClaseAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseAlumnoRepository extends JpaRepository<ClaseAlumno,Long> {
}
