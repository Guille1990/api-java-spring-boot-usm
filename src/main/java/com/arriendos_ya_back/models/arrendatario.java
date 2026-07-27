package com.arriendos_ya_back.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "arrendatarios")
public class arrendatario {

    @Id
    @Column(length = 12)
    private String rut;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 120)
    private String email;

    @OneToMany(mappedBy = "arrendatario")
    @JsonIgnore
    private List<arriendo> arriendos;

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<arriendo> getArriendos() { return arriendos; }
    public void setArriendos(List<arriendo> arriendos) { this.arriendos = arriendos; }
}