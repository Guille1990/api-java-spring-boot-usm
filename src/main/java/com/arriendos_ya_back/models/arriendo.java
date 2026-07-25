package com.arriendos_ya_back.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "arriendos")
public class arriendo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "propiedad_id", nullable = false)
    private propiedad propiedad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "arrendatario_rut", referencedColumnName = "rut", nullable = false)
    private arrendatario arrendatario;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_termino")
    private LocalDate fechaTermino;

    @Column(nullable = false)
    private Boolean activo = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(propiedad propiedad) { this.propiedad = propiedad; }

    public arrendatario getArrendatario() { return arrendatario; }
    public void setArrendatario(arrendatario arrendatario) { this.arrendatario = arrendatario; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaTermino() { return fechaTermino; }
    public void setFechaTermino(LocalDate fechaTermino) { this.fechaTermino = fechaTermino; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
