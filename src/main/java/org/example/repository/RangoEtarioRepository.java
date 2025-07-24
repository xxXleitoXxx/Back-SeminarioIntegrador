package org.example.repository;


import org.example.entity.RangoEtario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RangoEtarioRepository extends JpaRepository<RangoEtario,Long> {

    Optional<RangoEtario>  findBycodRangoEtario(Long codRangoEtario);
}
