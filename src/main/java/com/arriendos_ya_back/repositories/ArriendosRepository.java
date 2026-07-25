package com.arriendos_ya_back.repositories;

import com.arriendos_ya_back.models.arriendo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArriendosRepository extends JpaRepository<arriendo, Long> {
    List<arriendo> findByPropiedadId(Long propiedadId);
    List<arriendo> findByArrendatarioRut(String arrendatarioRut);
}
