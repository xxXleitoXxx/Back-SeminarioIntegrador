package org.example.repository;

import org.example.entity.Alumno;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumnoRepository extends BaseRepository<Alumno,Long>{
}
