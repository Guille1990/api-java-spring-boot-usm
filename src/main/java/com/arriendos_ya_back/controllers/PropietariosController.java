 package com.arriendos_ya_back.controllers;

import com.arriendos_ya_back.models.propietario;
import com.arriendos_ya_back.services.PropietariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propietarios")
@CrossOrigin(origins = "*")
public class PropietariosController {

    @Autowired
    private PropietariosService propietariosRepositoryService;

    // GET http://localhost:3000/api/propietarios
    @GetMapping
    public List<propietario> obtenerTodos() {
        return propietariosRepositoryService.listarTodos();
    }

    // GET http://localhost:3000/api/propietarios/{rut}
    @GetMapping("/{rut}")
    public ResponseEntity<propietario> obtenerPorRut(@PathVariable String rut) {
        return propietariosRepositoryService.buscarPorRut(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST http://localhost:3000/api/propietarios
    @PostMapping
    public propietario crear(@RequestBody propietario propietario) {
        return propietariosRepositoryService.guardar(propietario);
    }

    // PUT http://localhost:3000/api/propietarios/{rut}
    @PutMapping("/{rut}")
    public ResponseEntity<propietario> actualizar(@PathVariable String rut, @RequestBody propietario datosActualizados) {
        return propietariosRepositoryService.buscarPorRut(rut).map(propietario -> {
            propietario.setNombre(datosActualizados.getNombre());
            propietario.setApellido(datosActualizados.getApellido());
            propietario.setTelefono(datosActualizados.getTelefono());
            propietario.setEmail(datosActualizados.getEmail());
            
            propietario guardado = propietariosRepositoryService.guardar(propietario);
            return ResponseEntity.ok(guardado);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE http://localhost:3000/api/propietarios/{rut}
    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> eliminar(@PathVariable String rut) {
        return propietariosRepositoryService.buscarPorRut(rut).map(propietario -> {
            propietariosRepositoryService.eliminar(rut);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}