package com.arriendos_ya_back.services;

import com.arriendos_ya_back.models.arrendatario;
import com.arriendos_ya_back.models.arriendo;
import com.arriendos_ya_back.models.propiedad;
import com.arriendos_ya_back.repositories.ArrendatariosRepository;
import com.arriendos_ya_back.repositories.ArriendosRepository;
import com.arriendos_ya_back.repositories.PropiedadesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ArriendosService {

    @Autowired
    private ArriendosRepository arriendosRepository;

    @Autowired
    private PropiedadesRepository propiedadesRepository;

    @Autowired
    private ArrendatariosRepository arrendatariosRepository;

    public List<arriendo> listarTodos() {
        return arriendosRepository.findAll();
    }

    public List<arriendo> listarPorPropiedad(Long propiedadId) {
        return arriendosRepository.findByPropiedadId(propiedadId);
    }

    public List<arriendo> listarPorArrendatario(String arrendatarioRut) {
        return arriendosRepository.findByArrendatarioRut(arrendatarioRut.trim().toUpperCase());
    }

    public Optional<arriendo> guardar(arriendo arriendo) {
        if (arriendo.getPropiedad() == null || arriendo.getPropiedad().getId() == null
                || arriendo.getArrendatario() == null || arriendo.getArrendatario().getRut() == null) {
            return Optional.empty();
        }

        Optional<propiedad> propiedad = propiedadesRepository.findById(arriendo.getPropiedad().getId());
        Optional<arrendatario> arrendatario = arrendatariosRepository
                .findById(arriendo.getArrendatario().getRut().trim().toUpperCase());

        if (propiedad.isEmpty() || arrendatario.isEmpty()) {
            return Optional.empty();
        }

        arriendo.setPropiedad(propiedad.get());
        arriendo.setArrendatario(arrendatario.get());
        if (arriendo.getFechaInicio() == null) {
            arriendo.setFechaInicio(LocalDate.now());
        }
        if (arriendo.getActivo() == null) {
            arriendo.setActivo(true);
        }
        return Optional.of(arriendosRepository.save(arriendo));
    }

    public Optional<arriendo> finalizar(Long id, LocalDate fechaTermino) {
        return arriendosRepository.findById(id).map(arriendo -> {
            arriendo.setFechaTermino(fechaTermino == null ? LocalDate.now() : fechaTermino);
            arriendo.setActivo(false);
            return arriendosRepository.save(arriendo);
        });
    }

    public Optional<arriendo> actualizar(Long id, arriendo datos) {
        return arriendosRepository.findById(id).map(existente -> {
            if (datos.getDiaPago() != null) {
                existente.setDiaPago(datos.getDiaPago());
            }
            if (datos.getReajusteSemestral() != null) {
                existente.setReajusteSemestral(datos.getReajusteSemestral());
            }
            if (datos.getActivo() != null) {
                existente.setActivo(datos.getActivo());
            }
            if (datos.getFechaTermino() != null) {
                existente.setFechaTermino(datos.getFechaTermino());
            }
            return arriendosRepository.save(existente);
        });
    }
