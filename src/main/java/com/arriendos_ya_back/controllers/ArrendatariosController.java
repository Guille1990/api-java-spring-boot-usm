package com.arriendos_ya_back.controllers;

import com.arriendos_ya_back.models.arrendatario;
import com.arriendos_ya_back.services.ArrendatariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arrendatarios")
@CrossOrigin(origins = "*")
public class ArrendatariosController {

    @Autowired
    private ArrendatariosService arrendatariosService;

    // GET http://localhost:3000/api/arrendatarios
    @GetMapping
    public List<arrendatario> obtenerArrendatarios() {
        return arrendatariosService.listarTodos();
    }

    // GET http://localhost:3000/api/arrendatarios/{rut}
    @GetMapping("/{rut}")
    public ResponseEntity<arrendatario> obtenerArrendatarioPorRut(@PathVariable String rut) {
        return arrendatariosService.buscarPorRut(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST http://localhost:3000/api/arrendatarios
    @PostMapping
    public arrendatario crear(@RequestBody arrendatario arrendatario) {
        return arrendatariosService.guardar(arrendatario);
    }

    // PUT http://localhost:3000/api/arrendatarios/{rut}
    @PutMapping("/{rut}")
    public ResponseEntity<arrendatario> actualizarArrendatario(@PathVariable String rut, @RequestBody arrendatario datosActualizados) {
        return arrendatariosService.buscarPorRut(rut).map(arrendatario -> {
            arrendatario.setNombre(datosActualizados.getNombre());
            arrendatario.setApellido(datosActualizados.getApellido());
            arrendatario.setTelefono(datosActualizados.getTelefono());
            arrendatario.setEmail(datosActualizados.getEmail());
            
            arrendatario guardado = arrendatariosService.guardar(arrendatario);
            return ResponseEntity.ok(guardado);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE http://localhost:3000/api/arrendatarios/{rut}
    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> eliminarArrendatario(@PathVariable String rut) {
        return arrendatariosService.buscarPorRut(rut).map(arrendatario -> {
            arrendatariosService.eliminar(rut);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}