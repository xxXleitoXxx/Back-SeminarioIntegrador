package org.example.repository;

import org.example.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor,Long> {

    Optional<Profesor> findByDniProfesor(int dniProfesor);

}
