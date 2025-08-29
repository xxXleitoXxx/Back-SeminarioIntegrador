package org.example.repository;

import org.example.entity.Clase;
import org.example.entity.TipoClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClaseRepository extends JpaRepository<Clase,Long> {
    List<Clase> findByFechaBajaClaseIsNullAndFechaHoraClase(Date fecha);
    Optional <Clase> findByNroClaseAndFechaBajaClaseIsNull(Long nroClase);
    List<Clase> findByFechaBajaClaseIsNullAndFechaHoraClaseAfter(Date fechaHoy);
    List<Clase> findByTipoClaseAndFechaBajaClaseIsNull(TipoClase tipoClase);
}
