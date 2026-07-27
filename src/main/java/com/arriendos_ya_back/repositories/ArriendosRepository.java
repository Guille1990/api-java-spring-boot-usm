package com.arriendos_ya_back.repositories;

import com.arriendos_ya_back.models.arriendo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ArriendosRepository extends JpaRepository<arriendo, Long> {
    List<arriendo> findByPropiedadId(Long propiedadId);
    List<arriendo> findByArrendatarioRut(String arrendatarioRut);

    @Query("SELECT a FROM arriendo a " +
           "WHERE a.propiedad.id = :propiedadId " +
           "AND a.fechaInicio <= :fin " +
           "AND (a.fechaTermino IS NULL OR a.fechaTermino >= :inicio)")
    List<arriendo> findActivosEnPeriodo(
            @Param("propiedadId") Long propiedadId,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin);
}
