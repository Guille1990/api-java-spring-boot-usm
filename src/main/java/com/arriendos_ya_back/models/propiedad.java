package com.arriendos_ya_back.models;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "propiedades")
public class propiedad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String comuna;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String region;

    @Column(name = "numero_habitaciones")
    private Integer numeroHabitaciones;

    @Column(name = "numero_banos")
    private Integer numeroBanos;

    @Column(name = "precio_arriendo", nullable = false)
    private double precioArriendo;

    private Boolean disponible = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "propietario_rut", referencedColumnName = "rut", nullable = false)
    private propietario propietario;

    @OneToMany(mappedBy = "propiedad")
    @JsonIgnore
    private List<arriendo> arriendos;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<evento> eventos;
    
    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public Integer getNumeroHabitaciones() { return numeroHabitaciones; }
    public void setNumeroHabitaciones(Integer numeroHabitaciones) { this.numeroHabitaciones = numeroHabitaciones; }

    public Integer getNumeroBanos() { return numeroBanos; }
    public void setNumeroBanos(Integer numeroBanos) { this.numeroBanos = numeroBanos; }

    public double getPrecioArriendo() { return precioArriendo; }
    public void setPrecioArriendo(double precioArriendo) { this.precioArriendo = precioArriendo; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public propietario getPropietario() { return propietario; }
    public void setPropietario(propietario propietario) { this.propietario = propietario; }

    public List<arriendo> getArriendos() { return arriendos; }
    public void setArriendos(List<arriendo> arriendos) { this.arriendos = arriendos; }

    public List<evento> getEventos() { return eventos; }
    public void setEventos(List<evento> eventos) { this.eventos = eventos; }
}