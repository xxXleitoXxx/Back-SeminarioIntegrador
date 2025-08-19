package org.example.repository;

import org.example.entity.ConfHorarioTipoClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfHorarioTipoClaseRepository   extends JpaRepository<ConfHorarioTipoClase,Long> {
    Optional<ConfHorarioTipoClase> findByFechaFinVigenciaConfIsNull();
    Optional<ConfHorarioTipoClase> findByNroConfTC(Long nroConfTC);
}
