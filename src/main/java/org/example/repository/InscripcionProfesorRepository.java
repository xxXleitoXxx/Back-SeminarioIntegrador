package org.example.repository;


import org.example.entity.InscripcionProfesor;
import org.example.entity.Profesor;
import org.example.entity.TipoClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionProfesorRepository extends JpaRepository<InscripcionProfesor,Long> {
    Optional<InscripcionProfesor> findByProfesorAndTipoClaseAndFechaBajaInscripcionProfesorIsNull(Profesor profesor, TipoClase tipoClase);
    Optional<InscripcionProfesor> findByNroInscripcionProfesor(Long nroInscripcionProfesor);
    List<InscripcionProfesor> findByTipoClaseAndFechaBajaInscripcionProfesorIsNull(TipoClase tipoClase);
}
