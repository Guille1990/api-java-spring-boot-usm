package com.arriendos_ya_back.controllers;

import com.arriendos_ya_back.models.movimiento;
import com.arriendos_ya_back.services.MovimientosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

class ErrorResponse {
    private String error;
    
    public ErrorResponse(String error) {
        this.error = error;
    }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientosController {

    @Autowired
    private MovimientosService movimientosService;

    // GET http://localhost:3000/api/movimientos
    @GetMapping
    public List<movimiento> obtenerTodos() {
        return movimientosService.listarTodos();
    }

    // GET http://localhost:3000/api/movimientos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<movimiento> obtenerPorId(@PathVariable Long id) {
        Optional<movimiento> movimiento = movimientosService.obtenerPorId(id);
        return movimiento.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // GET http://localhost:3000/api/movimientos/propiedad/{propiedadId}
    @GetMapping("/propiedad/{propiedadId}")
    public ResponseEntity<MovimientosService.ResumenMovimientosPropiedad> obtenerPorPropiedad(@PathVariable Long propiedadId) {
        MovimientosService.ResumenMovimientosPropiedad resumen = movimientosService.obtenerResumenPorPropiedad(propiedadId);
        return ResponseEntity.ok(resumen);
    }

    // POST http://localhost:3000/api/movimientos
    @PostMapping
    public ResponseEntity<movimiento> crear(@RequestBody movimiento nuevoMovimiento) {
        Optional<movimiento> guardado = movimientosService.guardar(nuevoMovimiento);
        return guardado.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    // POST http://localhost:3000/api/movimientos/con-comprobante (multipart/form-data)
    @PostMapping("/con-comprobante")
    public ResponseEntity<?> crearConComprobante(
            @RequestParam("movimiento") String movimientoJson,
            @RequestParam(value = "comprobante", required = false) MultipartFile comprobante) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            movimiento nuevoMovimiento = mapper.readValue(movimientoJson, movimiento.class);

            Optional<movimiento> guardado = movimientosService.guardarConComprobante(nuevoMovimiento, comprobante);
            if (guardado.isPresent()) {
                return ResponseEntity.ok(guardado.get());
            } else {
                return ResponseEntity.badRequest().body(
                    new ErrorResponse("Error: Propiedad no encontrada o datos inválidos"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                new ErrorResponse("Error al crear movimiento: " + e.getMessage()));
        }
    }

    // PUT http://localhost:3000/api/movimientos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<movimiento> actualizar(@PathVariable Long id, @RequestBody movimiento datos) {
        Optional<movimiento> actualizado = movimientosService.actualizar(id, datos);
        return actualizado.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // DELETE http://localhost:3000/api/movimientos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (movimientosService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // PUT http://localhost:3000/api/movimientos/{id}/comprobante
    @PutMapping("/{id}/comprobante")
    public ResponseEntity<?> actualizarComprobante(
            @PathVariable Long id,
            @RequestParam(value = "comprobante", required = false) MultipartFile comprobante) {
        try {
            if (comprobante == null || comprobante.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new ErrorResponse("El archivo de comprobante es requerido"));
            }

            java.util.Optional<movimiento> actualizado = movimientosService.actualizarComprobante(id, comprobante);
            if (actualizado.isPresent()) {
                return ResponseEntity.ok(actualizado.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                new ErrorResponse("Error al actualizar comprobante: " + e.getMessage()));
        }
    }
}
