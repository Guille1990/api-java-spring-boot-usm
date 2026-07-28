package com.arriendos_ya_back.repositories;

import com.arriendos_ya_back.models.propiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropiedadesRepository extends JpaRepository<propiedad, Long> {
	List<propiedad> findByPropietarioRutOrderByIdAsc(String propietarioRut);

	List<propiedad> findAllByOrderByIdAsc();
}