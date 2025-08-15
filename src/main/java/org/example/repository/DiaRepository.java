package org.example.repository;

import org.example.entity.Dia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaRepository extends JpaRepository<Dia,Long> {
    Optional<Dia> findByCodDia(Long codDia);
    Optional<Dia> findByNombreDia(String nombreDia);
}
