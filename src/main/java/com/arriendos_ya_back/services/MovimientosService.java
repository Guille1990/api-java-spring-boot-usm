package com.arriendos_ya_back.services;

import com.arriendos_ya_back.models.movimiento;
import com.arriendos_ya_back.models.propiedad;
import com.arriendos_ya_back.models.TipoMovimiento;
import com.arriendos_ya_back.repositories.MovimientosRepository;
import com.arriendos_ya_back.repositories.PropiedadesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovimientosService {

    public static class ResumenMovimientosPropiedad {
        private List<movimiento> movimientos;
        private double totalIngresos;
        private double totalEgresos;
        private double saldo;

        public ResumenMovimientosPropiedad(List<movimiento> movimientos, double totalIngresos, double totalEgresos) {
            this.movimientos = movimientos;
            this.totalIngresos = totalIngresos;
            this.totalEgresos = totalEgresos;
            this.saldo = totalIngresos - totalEgresos;
        }

        public List<movimiento> getMovimientos() {
            return movimientos;
        }

        public double getTotalIngresos() {
            return totalIngresos;
        }

        public double getTotalEgresos() {
            return totalEgresos;
        }

        public double getSaldo() {
            return saldo;
        }
    }

    @Autowired
    private MovimientosRepository movimientosRepository;

    @Autowired
    private PropiedadesRepository propiedadesRepository;

    @Autowired
    private AzureBlobService azureBlobService;

    public List<movimiento> listarTodos() {
        return movimientosRepository.findAll();
    }

    public Optional<movimiento> obtenerPorId(Long id) {
        return movimientosRepository.findById(id);
    }

    public List<movimiento> obtenerPorPropiedad(Long propiedadId) {
        return movimientosRepository.findByPropiedadId(propiedadId);
    }

    public ResumenMovimientosPropiedad obtenerResumenPorPropiedad(Long propiedadId) {
        List<movimiento> movimientos = movimientosRepository.findByPropiedadId(propiedadId);

        double totalIngresos = 0;
        double totalEgresos = 0;

        for (movimiento movimiento : movimientos) {
            if (movimiento.getTipo() == TipoMovimiento.INGRESO) {
                totalIngresos += movimiento.getMonto();
            } else if (movimiento.getTipo() == TipoMovimiento.EGRESO) {
                totalEgresos += movimiento.getMonto();
            }
        }

        return new ResumenMovimientosPropiedad(movimientos, totalIngresos, totalEgresos);
    }

    public Optional<movimiento> guardar(movimiento movimiento) {
        if (movimiento.getPropiedad() == null || movimiento.getPropiedad().getId() == null) {
            return Optional.empty();
        }

        Optional<propiedad> propiedadOpt = propiedadesRepository.findById(movimiento.getPropiedad().getId());
        if (propiedadOpt.isEmpty()) {
            return Optional.empty();
        }

        movimiento.setPropiedad(propiedadOpt.get());

        if (movimiento.getFecha() == null) {
            movimiento.setFecha(ZonedDateTime.now());
        }

        return Optional.of(movimientosRepository.save(movimiento));
    }

    public Optional<movimiento> guardarConComprobante(movimiento movimiento, MultipartFile comprobante) throws IOException {
        if (movimiento.getPropiedad() == null || movimiento.getPropiedad().getId() == null) {
            return Optional.empty();
        }

        Optional<propiedad> propiedadOpt = propiedadesRepository.findById(movimiento.getPropiedad().getId());
        if (propiedadOpt.isEmpty()) {
            return Optional.empty();
        }

        movimiento.setPropiedad(propiedadOpt.get());

        if (movimiento.getFecha() == null) {
            movimiento.setFecha(ZonedDateTime.now());
        }

        // Subir comprobante a Azure si se proporciona
        if (comprobante != null && !comprobante.isEmpty()) {
            String urlComprobante = azureBlobService.subirArchivo(comprobante);
            movimiento.setUrlComprobante(urlComprobante);
        }

        return Optional.of(movimientosRepository.save(movimiento));
    }

    public Optional<movimiento> actualizar(Long id, movimiento datos) {
        return movimientosRepository.findById(id).map(existente -> {
            existente.setTipo(datos.getTipo());
            existente.setConcepto(datos.getConcepto());
            existente.setMonto(datos.getMonto());
            existente.setFecha(datos.getFecha());
            existente.setUrlComprobante(datos.getUrlComprobante());
            return movimientosRepository.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        if (movimientosRepository.existsById(id)) {
            movimientosRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<movimiento> actualizarComprobante(Long id, MultipartFile comprobante) throws IOException {
        Optional<movimiento> movimientoOpt = movimientosRepository.findById(id);
        if (movimientoOpt.isEmpty() || comprobante == null || comprobante.isEmpty()) {
            return Optional.empty();
        }

        movimiento existente = movimientoOpt.get();

        // Eliminar comprobante anterior si existe
        if (existente.getUrlComprobante() != null && !existente.getUrlComprobante().isEmpty()) {
            azureBlobService.eliminarArchivo(existente.getUrlComprobante());
        }

        // Subir nuevo comprobante
        String urlNueva = azureBlobService.subirArchivo(comprobante);
        existente.setUrlComprobante(urlNueva);

        return Optional.of(movimientosRepository.save(existente));
    }
}
