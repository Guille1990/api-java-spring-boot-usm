package com.arriendos_ya_back.repositories;

import com.arriendos_ya_back.models.movimiento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientosRepository extends JpaRepository<movimiento, Long> {
    List<movimiento> findByPropiedadId(Long propiedadId);
}
