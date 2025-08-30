package org.example.repository;


import org.example.entity.RangoEtario;
import org.example.entity.TipoClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoClaseRepository extends JpaRepository<TipoClase,Long> {
    Optional<TipoClase> findBycodTipoClase(Long codTipoClase);
    List<TipoClase> findByRangoEtarioAndFechaBajaTipoClaseIsNull(RangoEtario rangoEtario);
    Optional<TipoClase> findByNombreTipoClaseAndFechaBajaTipoClaseIsNull(String nombreTipoClase);
}
