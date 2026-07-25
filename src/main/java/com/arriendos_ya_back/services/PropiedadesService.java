package com.arriendos_ya_back.services;

import com.arriendos_ya_back.models.propiedad;
import com.arriendos_ya_back.models.propietario;
import com.arriendos_ya_back.repositories.PropiedadesRepository;
import com.arriendos_ya_back.repositories.PropietariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PropiedadesService {

    @Autowired
    private PropiedadesRepository propiedadesRepository;

    @Autowired
    private PropietariosRepository propietariosRepository;

    public List<propiedad> listarPropiedades() {
        return propiedadesRepository.findAll();
    }

    public Optional<propiedad> buscarPropiedadesPorId(Long id) {
        return propiedadesRepository.findById(id);
    }

    public Optional<propiedad> guardarPropiedades(propiedad propiedad) {
        if (propiedad.getPropietario() == null || propiedad.getPropietario().getRut() == null) {
            return Optional.empty();
        }

        Optional<propietario> propietario = propietariosRepository
                .findById(propiedad.getPropietario().getRut().trim().toUpperCase());
        if (propietario.isEmpty()) {
            return Optional.empty();
        }

        propiedad.setPropietario(propietario.get());
        return Optional.of(propiedadesRepository.save(propiedad));
    }

    public void eliminarPropiedades(Long id) {
        propiedadesRepository.deleteById(id);
    }

    public Optional<propiedad> asignarPropietario(Long propiedadId, String propietarioRut) {
        String rutLimpio = propietarioRut.trim().toUpperCase();
        Optional<propiedad> propiedadOpt = propiedadesRepository.findById(propiedadId);
        Optional<propietario> propietarioOpt = propietariosRepository.findById(rutLimpio);

        if (propiedadOpt.isPresent() && propietarioOpt.isPresent()) {
            propiedad propiedadReal = propiedadOpt.get();
            propiedadReal.setPropietario(propietarioOpt.get());
            return Optional.of(propiedadesRepository.save(propiedadReal));
        }
        return Optional.empty();
    }
    
}