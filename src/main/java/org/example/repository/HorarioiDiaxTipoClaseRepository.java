package org.example.repository;

import org.example.entity.Dia;
import org.example.entity.HorarioiDiaxTipoClase;
import org.example.entity.TipoClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioiDiaxTipoClaseRepository extends JpaRepository<HorarioiDiaxTipoClase, Long> {

    boolean existsByDiaAndFechaBajaHFxTCIsNullAndHoraDesdeLessThanAndHoraHastaGreaterThan(
            Dia dia, Time nuevaHoraHasta, Time nuevaHoraDesde);

    Optional<HorarioiDiaxTipoClase> findByNroHFxTC(Long nroHFxTC);
    List<HorarioiDiaxTipoClase>  findByTipoClaseAndFechaBajaHFxTCIsNull(TipoClase tipoClase);
    boolean existsByDiaAndFechaBajaHFxTCIsNull(Dia dia);
}


