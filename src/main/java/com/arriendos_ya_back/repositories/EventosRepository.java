package com.arriendos_ya_back.repositories;

import com.arriendos_ya_back.models.evento;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventosRepository extends JpaRepository<evento, Long> {
    List<evento> findByPropiedadId(Long propiedadId);

    @Query("SELECT e FROM evento e " +
           "WHERE e.propiedad.id = :propiedadId " +
           "AND e.fecha >= :inicio " +
           "AND e.fecha < :fin " +
           "ORDER BY e.fecha DESC")
    List<evento> findByPropiedadIdAndFechaRango(
            @Param("propiedadId") Long propiedadId,
            @Param("inicio") ZonedDateTime inicio,
            @Param("fin") ZonedDateTime fin);
}