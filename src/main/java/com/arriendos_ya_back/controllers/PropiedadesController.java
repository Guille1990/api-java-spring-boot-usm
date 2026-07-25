package com.arriendos_ya_back.controllers;

import com.arriendos_ya_back.models.propiedad;
import com.arriendos_ya_back.services.PropiedadesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propiedades")
@CrossOrigin(origins = "*")
public class PropiedadesController {

    @Autowired
    private PropiedadesService propiedadesService;

    // GET http://localhost:3000/api/propiedades
    @GetMapping
    public List<propiedad> obtenerTodas() {
        return propiedadesService.listarPropiedades();
    }

    // GET http://localhost:3000/api/propiedades/{id}
    @GetMapping("/{id}")
    public ResponseEntity<propiedad> obtenerPorId(@PathVariable Long id) {
        return propiedadesService.buscarPropiedadesPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST http://localhost:3000/api/propiedades
    @PostMapping
    public ResponseEntity<propiedad> crear(@RequestBody propiedad propiedad) {
        return propiedadesService.guardarPropiedades(propiedad)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    // PUT http://localhost:3000/api/propiedades/{id}
    @PutMapping("/{id}")
    public ResponseEntity<propiedad> actualizar(@PathVariable Long id, @RequestBody propiedad datosActualizados) {
        return propiedadesService.buscarPropiedadesPorId(id).map(propiedad -> {
            propiedad.setDireccion(datosActualizados.getDireccion());
            propiedad.setComuna(datosActualizados.getComuna());
            propiedad.setCiudad(datosActualizados.getCiudad());
            propiedad.setRegion(datosActualizados.getRegion());
            propiedad.setNumeroHabitaciones(datosActualizados.getNumeroHabitaciones());
            propiedad.setNumeroBanos(datosActualizados.getNumeroBanos());
            propiedad.setPrecioArriendo(datosActualizados.getPrecioArriendo());
            propiedad.setDisponible(datosActualizados.getDisponible());

            return propiedadesService.guardarPropiedades(propiedad)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.badRequest().build());
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE http://localhost:3000/api/propiedades/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return propiedadesService.buscarPropiedadesPorId(id).map(propiedad -> {
            propiedadesService.eliminarPropiedades(id);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{propiedadId}/asignar-propietario/{propietarioRut}")
    public ResponseEntity<propiedad> asignarPropietario(
            @PathVariable Long propiedadId,
            @PathVariable String propietarioRut) {
        java.util.Optional<propiedad> propiedadActualizada = propiedadesService.asignarPropietario(propiedadId, propietarioRut);

        if (propiedadActualizada.isPresent()) {
            return ResponseEntity.ok(propiedadActualizada.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}