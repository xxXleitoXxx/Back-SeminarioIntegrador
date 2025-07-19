package org.example.repository;


import org.example.entity.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad,Long> {
    Optional<Localidad> findByCodLocalidadAndFechaBajaLocalidad(Long codLocalidad,Date fechaBajaLocalidad);
}


