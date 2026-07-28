package com.arriendos_ya_back.repositories;

import com.arriendos_ya_back.models.TipoMovimiento;
import com.arriendos_ya_back.models.movimiento;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientosRepository extends JpaRepository<movimiento, Long> {
    List<movimiento> findByPropiedadId(Long propiedadId);

    @Query("SELECT m FROM movimiento m " +
        "WHERE m.propiedad.id = :propiedadId " +
        "AND m.fecha >= :inicio " +
        "AND m.fecha < :fin " +
        "ORDER BY m.fecha DESC")
    List<movimiento> findByPropiedadIdAndFechaRango(
         @Param("propiedadId") Long propiedadId,
         @Param("inicio") ZonedDateTime inicio,
         @Param("fin") ZonedDateTime fin);

    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM movimiento m " +
        "WHERE m.propiedad.id = :propiedadId " +
        "AND m.tipo = :tipo " +
        "AND m.fecha >= :inicio " +
        "AND m.fecha < :fin")
    Double sumMontoByPropiedadAndTipoAndFechaRango(
         @Param("propiedadId") Long propiedadId,
         @Param("tipo") TipoMovimiento tipo,
         @Param("inicio") ZonedDateTime inicio,
         @Param("fin") ZonedDateTime fin);

    @Query("SELECT m.propiedad.id, m.tipo, COALESCE(SUM(m.monto), 0) " +
        "FROM movimiento m " +
        "WHERE m.propiedad.id IN :propiedadIds " +
        "AND m.fecha >= :inicio " +
        "AND m.fecha < :fin " +
        "GROUP BY m.propiedad.id, m.tipo")
    List<Object[]> sumMontosByPropiedadesAndFechaRango(
         @Param("propiedadIds") List<Long> propiedadIds,
         @Param("inicio") ZonedDateTime inicio,
         @Param("fin") ZonedDateTime fin);

    @Query("SELECT m FROM movimiento m " +
        "WHERE m.propiedad.id IN :propiedadIds " +
        "AND m.fecha >= :inicio " +
        "AND m.fecha < :fin")
    List<movimiento> findByPropiedadIdsAndFechaRango(
         @Param("propiedadIds") List<Long> propiedadIds,
         @Param("inicio") ZonedDateTime inicio,
         @Param("fin") ZonedDateTime fin);
}
