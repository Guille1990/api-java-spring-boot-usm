package com.arriendos_ya_back.controllers;

import com.arriendos_ya_back.models.arriendo;
import com.arriendos_ya_back.services.ArriendosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/arriendos")
@CrossOrigin(origins = "*")
public class ArriendosController {

    @Autowired
    private ArriendosService arriendosService;

    @GetMapping
    public List<arriendo> obtenerTodos() {
        return arriendosService.listarTodos();
    }

    @GetMapping("/propiedad/{propiedadId}")
    public List<arriendo> obtenerPorPropiedad(@PathVariable Long propiedadId) {
        return arriendosService.listarPorPropiedad(propiedadId);
    }

    @GetMapping("/arrendatario/{arrendatarioRut}")
    public List<arriendo> obtenerPorArrendatario(@PathVariable String arrendatarioRut) {
        return arriendosService.listarPorArrendatario(arrendatarioRut);
    }

    @PostMapping
    public ResponseEntity<Object> crear(@Valid @RequestBody arriendo nuevoArriendo) {
        var guardado = arriendosService.guardar(nuevoArriendo);
        if (guardado.isPresent()) {
            return ResponseEntity.ok(guardado.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizar(@PathVariable Long id, @Valid @RequestBody arriendo datos) {
        var actualizado = arriendosService.actualizar(id, datos);
        if (actualizado.isPresent()) {
            return ResponseEntity.ok(actualizado.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<arriendo> finalizar(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaTermino) {
        return arriendosService.finalizar(id, fechaTermino)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
